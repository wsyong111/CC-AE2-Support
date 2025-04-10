package com.wsyong11.cc_ae2_support.cc.peripheral;

import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import com.mojang.logging.LogUtils;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.slf4j.Logger;

import com.wsyong11.cc_ae2_support.ae2.IAENetworkObject;
import com.wsyong11.cc_ae2_support.cc.peripheral.manager.AECraftingManager;
import com.wsyong11.cc_ae2_support.cc.peripheral.manager.AEEnergyManager;
import com.wsyong11.cc_ae2_support.cc.peripheral.manager.AEStorageManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class ComputerInterfacePeripheral implements IPeripheral {
	private static final Logger LOGGER = LogUtils.getLogger();

	private final IAENetworkObject networkObject;

	private final AEEnergyManager energyManager;
	private final AEStorageManager storageManager;
	private final AECraftingManager craftingManager;

	public ComputerInterfacePeripheral(@Nonnull IAENetworkObject networkObject) {
		Objects.requireNonNull(networkObject, "networkObject is null");
		this.networkObject = networkObject;

		IManagedGridNode node = networkObject.getMainNode();
		this.energyManager = new AEEnergyManager(node);
		this.storageManager = new AEStorageManager(node);
		this.craftingManager = new AECraftingManager(node);
	}

	@Override
	public String getType() {
		return "ae_interface";
	}

	public void reset() {
		LOGGER.debug("Reset");
		this.energyManager.reset();
		this.storageManager.reset();
		this.craftingManager.reset();
	}

	@Nullable
	private IGrid getGrid() {
		return this.networkObject.isActive() ? this.networkObject.getMainNode().getGrid() : null;
	}

	@LuaFunction
	public boolean isActive() {
		return this.networkObject.isActive();
	}

	@LuaFunction
	public boolean isPowered() {
		return this.networkObject.isPowered();
	}

	@LuaFunction
	public boolean isMissingChannel() {
		return this.networkObject.isMissingChannel();
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Nonnull
	@LuaFunction
	public AEEnergyManager getEnergyManager() {
		return this.energyManager;
	}

	@Nonnull
	@LuaFunction
	public AEStorageManager getStorageManager() {
		return this.storageManager;
	}

	@Nonnull
	@LuaFunction
	public AECraftingManager getCraftingManager() {
		return this.craftingManager;
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@LuaFunction
	public long getOnlineMachineCount() {
		IGrid grid = this.getGrid();
		if (grid == null) return -1;

		return StreamSupport
			.stream(grid.getMachineClasses().spliterator(), true)
			.map(grid::getActiveMachines)
			.mapToLong(Collection::size)
			.sum();
	}

	@LuaFunction
	public long getMachineCount() {
		IGrid grid = this.getGrid();
		if (grid == null) return -1;

		return StreamSupport
			.stream(grid.getMachineClasses().spliterator(), true)
			.map(grid::getMachines)
			.mapToLong(Collection::size)
			.sum();
	}

	@LuaFunction
	public int getUsedChannelCount() {
		IGrid grid = this.getGrid();
		if (grid == null) return -1;

		return grid.getPathingService().getUsedChannels();
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		return this == other;
	}
}
