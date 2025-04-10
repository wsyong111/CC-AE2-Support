package com.wsyong11.cc_ae2_support.cc.peripheral.manager;

import appeng.api.config.PowerUnits;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.energy.IEnergyService;
import dan200.computercraft.api.lua.LuaFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AEEnergyManager extends LuaManagerInterface<IEnergyService> {
	public AEEnergyManager(@Nonnull IManagedGridNode node) {
		super(node);
	}

	@Nullable
	@Override
	protected IEnergyService getManagerInstance(@Nonnull IGrid grid) {
		return null;
	}

	@LuaFunction
	public double getIdlePowerUsage() {
		IEnergyService manager = this.getManager();
		if (manager == null) return -1L;
		return PowerUnits.AE.convertTo(PowerUnits.FE, manager.getIdlePowerUsage());
	}

	@LuaFunction
	public double getChannelPowerUsage() {
		IEnergyService manager = this.getManager();
		if (manager == null) return -1L;
		return PowerUnits.AE.convertTo(PowerUnits.FE, manager.getChannelPowerUsage());
	}

	@LuaFunction
	public double getAvgPowerUsage() {
		IEnergyService manager = this.getManager();
		if (manager == null) return -1L;
		return PowerUnits.AE.convertTo(PowerUnits.FE, manager.getAvgPowerUsage());
	}

	@LuaFunction
	public double getAvgPowerInput() {
		IEnergyService manager = this.getManager();
		if (manager == null) return -1L;
		return PowerUnits.AE.convertTo(PowerUnits.FE, manager.getAvgPowerInjection());
	}

	@LuaFunction
	public double getStoredPower() {
		IEnergyService manager = this.getManager();
		if (manager == null) return -1L;
		return PowerUnits.AE.convertTo(PowerUnits.FE, manager.getStoredPower());
	}

	@LuaFunction
	public double getMaxStoredPower() {
		IEnergyService manager = this.getManager();
		if (manager == null) return -1L;
		return PowerUnits.AE.convertTo(PowerUnits.FE, manager.getMaxStoredPower());
	}
}
