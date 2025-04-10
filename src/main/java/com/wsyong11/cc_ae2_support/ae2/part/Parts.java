package com.wsyong11.cc_ae2_support.ae2.part;

import com.wsyong11.cc_ae2_support.tab.CreativeModeTabs;
import com.wsyong11.core_mod.register.Registers;
import com.wsyong11.core_mod.register.item.ItemRegisterObject;

import static com.wsyong11.cc_ae2_support.CC_AE2_Support.REGISTERS;

@SuppressWarnings("unused")
public final class Parts {
	public static final ItemRegisterObject<PartItem<ComputerInterfacePart>> COMPUTER_INTERFACE = REGISTERS
		.item("cable_computer_interface", new Registers.Type<PartItem<ComputerInterfacePart>>())
		.builder(p -> new PartItem<>(p, ComputerInterfacePart.class, ComputerInterfacePart::new))
		.tab(CreativeModeTabs.MAIN)
		.build();

	public static void init() { /* no-op */ }

	private Parts() { /* no-op */ }
}
