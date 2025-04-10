package com.wsyong11.cc_ae2_support.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ComputerCraftCapability {
	public static final Capability<IPeripheral> PERIPHERAL = CapabilityManager.get(new CapabilityToken<>() {
	});
}
