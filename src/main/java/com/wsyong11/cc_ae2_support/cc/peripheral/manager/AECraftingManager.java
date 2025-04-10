package com.wsyong11.cc_ae2_support.cc.peripheral.manager;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.*;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import com.mojang.logging.LogUtils;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.StreamSupport;

// TODO: 2025/4/3 Lua Document
public class AECraftingManager extends LuaManagerInterface<ICraftingService> {
	private static final Logger LOGGER = LogUtils.getLogger();

	public AECraftingManager(@Nonnull IManagedGridNode node) {
		super(node);
	}

	@Nullable
	@Override
	protected ICraftingService getManagerInstance(@Nonnull IGrid grid) {
		return grid.getCraftingService();
	}

	@Nullable
	@LuaFunction
	public CraftingRequest createCraftingRequest(@Nonnull String id, int amount) throws LuaException {
		if (amount <= 0) throw new LuaException("The amount cannot be less than or equal to 0", 2);

		ICraftingService manager = this.getManager();
		if (manager == null) return null;

		ResourceLocation resourceId = ResourceLocation.parse(id);

		AEKey itemKey;
		if (ForgeRegistries.ITEMS.containsKey(resourceId)) {
			Item item = ForgeRegistries.ITEMS.getValue(resourceId);
			if (item == null) throw new LuaException("Cannot get item data with id '" + id + "'", 2);
			itemKey = AEItemKey.of(item);
		} else if (ForgeRegistries.FLUIDS.containsKey(resourceId)) {
			Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceId);
			if (fluid == null) throw new LuaException("Cannot get fluid data with id '" + id + "'", 2);
			itemKey = AEFluidKey.of(fluid);
		} else {
			throw new LuaException("Unknown id '" + id + "'", 2);
		}

		return new CraftingRequest(manager, itemKey, amount, this.getNode());
	}

	@LuaFunction
	public int getCpuCount() {
		ICraftingService manager = this.getManager();
		if (manager == null) return -1;
		return manager.getCpus().size();
	}

	@LuaFunction
	public long getIdleCPUCount() {
		ICraftingService manager = this.getManager();
		if (manager == null) return -1;
		return manager.getCpus()
		              .stream()
		              .filter(cpu -> !cpu.isBusy())
		              .count();
	}

	@Nullable
	@LuaFunction
	public List<CpuInfo> getCpus() {
		ICraftingService manager = this.getManager();
		if (manager == null) return null;
		return manager.getCpus()
		              .stream()
		              .map(CpuInfo::new)
		              .toList();
	}

	public static class CraftingRequest {
		private final ICraftingService service;
		private final AEKey item;

		private final ICraftingSimulationRequester simulationRequester;
		private final Future<ICraftingPlan> craftingPlanFuture;

		private ICraftingPlan craftingPlan;

		public CraftingRequest(@Nonnull ICraftingService service, @Nonnull AEKey item, int amount, @Nonnull IManagedGridNode mainNode) throws LuaException {
			Objects.requireNonNull(service, "service is null");
			Objects.requireNonNull(item, "item is null");
			Objects.requireNonNull(mainNode, "mainNode is null");

			this.service = service;
			this.craftingPlan = null;
			this.item = item;

			IGridNode node = mainNode.getNode();
			IGrid grid = mainNode.getGrid();
			if (node == null
			    || grid == null)
				throw new LuaException("Cannot create crafting request", 2);

			ServerLevel level = node.getLevel();

			this.simulationRequester = () -> IActionSource.ofMachine(grid::getPivot);

			this.craftingPlanFuture = service.beginCraftingCalculation(
				level,
				this.simulationRequester,
				item,
				amount,
				CalculationStrategy.REPORT_MISSING_ITEMS);
		}

		private boolean checkFutureDone() {
			if (this.craftingPlan != null) return true;

			if (this.craftingPlanFuture.isDone()) {
				try {
					this.craftingPlan = this.craftingPlanFuture.get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					LOGGER.error("Thread interrupted, crafting item '{}'", this.item, e);
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					LOGGER.error("Cannot get crafting plan with item '{}'", this.item, e);
					throw new RuntimeException(e);
				}

				return true;
			}
			return false;
		}

		@LuaFunction
		public boolean isReady() {
			return this.checkFutureDone();
		}

		@LuaFunction
		public boolean isMissingItems() {
			if (!this.checkFutureDone()) return false;
			return this.craftingPlan.simulation();
		}

		@Nonnull
		@LuaFunction
		public List<AEStorageManager.ItemDetail> getMissingItems() {
			if (!this.checkFutureDone()) return Collections.emptyList();
			if (!this.craftingPlan.simulation()) return Collections.emptyList();

			KeyCounter keyCounter = this.craftingPlan.missingItems();
			return StreamSupport.stream(keyCounter.spliterator(), true)
			                    .map(e -> new AEStorageManager.ItemDetail(e.getKey(), e.getLongValue()))
			                    .toList();
		}

		@LuaFunction
		public CraftingResult start() throws LuaException {
			if (!this.checkFutureDone()) throw new LuaException("Crafting plan doesn't calculate complete", 2);

			ICraftingSubmitResult result = this.service.submitJob(
				this.craftingPlan,
				null,
				null,
				true,
				this.simulationRequester.getActionSource());

			return new CraftingResult(result);
		}

		public static class CraftingResult {
			private final ICraftingSubmitResult result;

			public CraftingResult(@Nonnull ICraftingSubmitResult result) {
				Objects.requireNonNull(result, "result is null");
				this.result = result;
			}

			@Nullable
			private ICraftingLink getLink() {
				return this.result.link();
			}

			@LuaFunction
			public boolean isSuccess() {
				return this.result.successful();
			}

			@Nullable
			@LuaFunction
			public String getId() {
				ICraftingLink link = this.getLink();
				if (link == null) return null;

				return link.getCraftingID().toString();
			}

			@LuaFunction
			public void cancel() throws LuaException {
				ICraftingLink link = this.getLink();
				if (link == null) throw new LuaException("Cannot cancel a crafting job", 2);

				link.cancel();
			}

			@LuaFunction
			public boolean isCanceled() {
				ICraftingLink link = this.getLink();
				if (link == null) return false;

				return link.isCanceled();
			}
		}
	}

	public static class CpuInfo {
		private final ICraftingCPU cpu;

		public CpuInfo(@Nonnull ICraftingCPU cpu) {
			Objects.requireNonNull(cpu, "cpu is null");
			this.cpu = cpu;
		}

		@LuaFunction
		public boolean isBusy() {
			return this.cpu.isBusy();
		}

		@LuaFunction
		public void cancelJob() {
			this.cpu.cancelJob();
		}

		@LuaFunction
		public int getCoProcessors() {
			return this.cpu.getCoProcessors();
		}

		@LuaFunction
		@Nullable
		public CpuInfo.CpuJobStatus getJobStatus() {
			CraftingJobStatus status = this.cpu.getJobStatus();
			return status == null ? null : new CpuInfo.CpuJobStatus(status);
		}

		@LuaFunction
		@Nullable
		public String getName() {
			Component name = this.cpu.getName();
			return name == null ? null : name.getString();
		}

		public static class CpuJobStatus {
			private final CraftingJobStatus status;

			public CpuJobStatus(@Nonnull CraftingJobStatus status) {
				Objects.requireNonNull(status, "status is null");
				this.status = status;
			}

			@Nonnull
			@LuaFunction
			public String getCraftingItemId() {
				return this.status.crafting().what().getId().toString();
			}

			@LuaFunction
			public long getCraftAmount() {
				return this.status.crafting().amount();
			}

			@LuaFunction
			public long getTotalItems() {
				return this.status.totalItems();
			}

			@LuaFunction
			public long getProgress() {
				return this.status.progress();
			}

			@LuaFunction
			public long getElapsedTimeNanos() {
				return this.status.elapsedTimeNanos();
			}
		}
	}
}
