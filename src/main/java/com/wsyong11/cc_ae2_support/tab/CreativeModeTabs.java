package com.wsyong11.cc_ae2_support.tab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import com.wsyong11.cc_ae2_support.block.Blocks;
import com.wsyong11.core_mod.register.creativemodetab.CreativeModeTabRegisterObject;

import static com.wsyong11.cc_ae2_support.CC_AE2_Support.REGISTERS;

public final class CreativeModeTabs {
	public static final CreativeModeTabRegisterObject MAIN = REGISTERS
		.tab("tab")
		.title(Component.translatable("itemGroup.cc_ae2_support.main"))
		.icon(()->Blocks.COMPUTER_INTERFACE)
		.build();

	public static void init() { /* no-op */ }

	private CreativeModeTabs() { /* no-op */ }
}
