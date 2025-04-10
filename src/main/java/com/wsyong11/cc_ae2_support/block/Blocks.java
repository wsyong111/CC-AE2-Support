package com.wsyong11.cc_ae2_support.block;

import com.wsyong11.cc_ae2_support.tab.CreativeModeTabs;
import com.wsyong11.core_mod.register.block.BlockRegisterObject;

import static com.wsyong11.cc_ae2_support.CC_AE2_Support.REGISTERS;

public final class Blocks {
	public static final BlockRegisterObject<ComputerInterfaceBlock> COMPUTER_INTERFACE = REGISTERS
		.block("computer_interface", ComputerInterfaceBlock.class)
		.builder(ComputerInterfaceBlock::new)
		.tab(CreativeModeTabs.MAIN)
		.build();

	public static void init() { /* no-op */ }

	private Blocks() { /* no-op */ }
}
