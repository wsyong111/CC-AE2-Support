package com.wsyong11.cc_ae2_support.block.entity;

import com.wsyong11.cc_ae2_support.block.Blocks;
import com.wsyong11.core_mod.register.blockentity.BlockEntityRegisterObject;

import static com.wsyong11.cc_ae2_support.CC_AE2_Support.REGISTERS;

public final class BlocksEntity {
	public static final BlockEntityRegisterObject<ComputerInterfaceBlockEntity> COMPUTER_INTERFACE = REGISTERS
		.blockEntity("computer_interface", ComputerInterfaceBlockEntity.class)
		.builder(ComputerInterfaceBlockEntity::new)
		.validBlock(Blocks.COMPUTER_INTERFACE)
		.build();

	public static void init() { /* no-op */ }

	private BlocksEntity() { /* no-op */ }
}
