package com.wsyong11.cc_ae2_support.cc;

import dan200.computercraft.api.ComputerCraftAPI;

import com.wsyong11.cc_ae2_support.cc.lua.AEUtil;

public final class ComputerCraftInit {
	public static void init() {
		ComputerCraftAPI.registerAPIFactory(AEUtil::new);
	}

	private ComputerCraftInit() { /* no-op */ }
}
