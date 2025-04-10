package com.wsyong11.cc_ae2_support;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.wsyong11.cc_ae2_support.ae2.AEInit;
import com.wsyong11.cc_ae2_support.block.Blocks;
import com.wsyong11.cc_ae2_support.block.entity.BlocksEntity;
import com.wsyong11.cc_ae2_support.cc.ComputerCraftInit;
import com.wsyong11.cc_ae2_support.item.Items;
import com.wsyong11.cc_ae2_support.tab.CreativeModeTabs;
import com.wsyong11.core_mod.register.Registers;

import static com.wsyong11.cc_ae2_support.Constains.MOD_ID;

@SuppressWarnings("removal")
@Mod(MOD_ID)
public class CC_AE2_Support {
	public static Registers REGISTERS = null;

	public CC_AE2_Support() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		REGISTERS = Registers.create(MOD_ID, modEventBus);

		this.initRegister();

		AEInit.init();
		ComputerCraftInit.init();
	}

	private void initRegister() {
		Blocks.init();
		Items.init();
		BlocksEntity.init();
		CreativeModeTabs.init();
	}
}
