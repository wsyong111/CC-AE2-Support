package com.wsyong11.cc_ae2_support.cc.lua;

import appeng.api.config.PowerUnits;
import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;

public class AEUtil implements ILuaAPI {
	public AEUtil(@Nonnull IComputerSystem computer) { /* no-op */ }

	@Override
	public String[] getNames() {
		return new String[]{"aeUtil"};
	}

	@Nullable
	@Override
	public String getModuleName() {
		return "aeUtil";
	}

	@LuaFunction
	public double AEtoFE(double energy) {
		return PowerUnits.AE.convertTo(PowerUnits.FE, energy);
	}

	@LuaFunction
	public double FEtoAE(double energy) {
		return PowerUnits.FE.convertTo(PowerUnits.AE, energy);
	}
}
