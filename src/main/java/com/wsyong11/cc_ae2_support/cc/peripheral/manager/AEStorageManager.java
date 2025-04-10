package com.wsyong11.cc_ae2_support.cc.peripheral.manager;

import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import com.wsyong11.cc_ae2_support.cc.NbtUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class AEStorageManager extends LuaManagerInterface<IStorageService> {
	public AEStorageManager(@Nonnull IManagedGridNode node) {
		super(node);
	}

	@Nullable
	@Override
	protected IStorageService getManagerInstance(@Nonnull IGrid grid) {
		return grid.getStorageService();
	}

	@Nullable
	private MEStorage getInventory() {
		IStorageService manager = this.getManager();
		return manager == null ? null : manager.getInventory();
	}

	@Nullable
	@LuaFunction
	public Map<String, Object> getTypeCount() {
		Map<String, Object> result = new HashMap<>();

		int item = 0;
		int fluid = 0;

		MEStorage inventory = this.getInventory();
		if (inventory != null) {

			for (Object2LongMap.Entry<AEKey> entry : inventory.getAvailableStacks()) {
				AEKey key = entry.getKey();
				if (AEItemKey.is(key)) item++;
				if (AEFluidKey.is(key)) fluid++;
			}
		}

		result.put("item", item);
		result.put("fluid", fluid);
		return result;
	}

	@Nullable
	@LuaFunction
	public Map<String, List<ItemDetail>> getDetails() {
		MEStorage inventory = this.getInventory();
		if (inventory == null) return null;

		KeyCounter keyCounter = inventory.getAvailableStacks();

		Map<String, List<ItemDetail>> result = new HashMap<>(keyCounter.size());
		for (Object2LongMap.Entry<AEKey> entry : keyCounter) {
			AEKey key = entry.getKey();
			long count = entry.getLongValue();

			ResourceLocation id = key.getId();
			result.computeIfAbsent(id.toString(), k -> new ArrayList<>())
			      .add(new ItemDetail(key, count));
		}

		return result;
	}

	@LuaFunction
	public long getItemCount(@Nonnull String id) throws LuaException {
		MEStorage inventory = this.getInventory();
		if (inventory == null) return -1L;

		ResourceLocation resourceId = ResourceLocation.parse(id);

		if (!ForgeRegistries.ITEMS.containsKey(resourceId))
			throw new LuaException("Unknown item id '" + id + "'", 2);

		Item item = ForgeRegistries.ITEMS.getValue(resourceId);
		if (item == null) throw new LuaException("Cannot get item data with id '" + id + "'", 2);

		KeyCounter keyCounter = inventory.getAvailableStacks();
		return keyCounter.get(AEItemKey.of(item));
	}

	@LuaFunction
	public long getFluidAmountMb(@Nonnull String id) throws LuaException {
		MEStorage inventory = this.getInventory();
		if (inventory == null) return -1L;

		ResourceLocation resourceId = ResourceLocation.parse(id);

		if (!ForgeRegistries.FLUIDS.containsKey(resourceId))
			throw new LuaException("Unknown fluid id '" + id + "'", 2);

		Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceId);
		if (fluid == null)
			throw new LuaException("Cannot get fluid data with id '" + id + "'", 2);

		KeyCounter keyCounter = inventory.getAvailableStacks();
		return keyCounter.get(AEFluidKey.of(fluid));
	}

	public static class ItemDetail {
		private final AEKey key;
		private final long count;

		public ItemDetail(@Nonnull AEKey key, long count) {
			this.key = key;
			this.count = count;
			Objects.requireNonNull(key, "key is null");
		}

		@LuaFunction
		public long getCount() {
			return this.count;
		}

		@Nonnull
		@LuaFunction
		public String getId() {
			return this.key.getId().toString();
		}

		@LuaFunction
		public boolean isItem() {
			return AEItemKey.is(this.key);
		}

		@LuaFunction
		public boolean isFluid() {
			return AEFluidKey.is(this.key);
		}

		@Nonnull
		@LuaFunction
		public Map<String, Object> getTag() {
			CompoundTag tag;
			if (AEItemKey.is(this.key)) tag = ((AEItemKey) this.key).getReadOnlyStack().getTag();
			else if (AEFluidKey.is(this.key)) tag = ((AEFluidKey) this.key).getTag();
			else tag = null;

			return tag == null ? Collections.emptyMap() : NbtUtil.encode(tag);
		}
	}
}
