package com.wsyong11.cc_ae2_support.ae2.part;

import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import com.wsyong11.cc_ae2_support.ae2.IAENetworkObject;
import com.wsyong11.cc_ae2_support.ae2.NetworkStatus;
import com.wsyong11.cc_ae2_support.ae2.part.model.PartModel;
import com.wsyong11.cc_ae2_support.ae2.part.model.PartModelRegistry;
import com.wsyong11.cc_ae2_support.cc.ComputerCraftCapability;
import com.wsyong11.cc_ae2_support.cc.peripheral.ComputerInterfacePeripheral;

import javax.annotation.Nonnull;

// FIXME: 2025/4/8 Model display error
public class ComputerInterfacePart extends BasePart implements IAENetworkObject {
	private static final PartModel MODEL_ON = new PartModel(PartModelRegistry.COMPUTER_INTERFACE_BASE, PartModelRegistry.COMPUTER_INTERFACE_ON);
	private static final PartModel MODEL_OFF = new PartModel(PartModelRegistry.COMPUTER_INTERFACE_BASE, PartModelRegistry.COMPUTER_INTERFACE_OFF);
	private static final PartModel MODEL_HAS_CHANNEL = new PartModel(PartModelRegistry.COMPUTER_INTERFACE_BASE, PartModelRegistry.COMPUTER_INTERFACE_HAS_CHANNEL);

	private final LazyOptional<ComputerInterfacePeripheral> peripheralLazyOptional;

	public ComputerInterfacePart(@Nonnull IPartItem<?> partItem) {
		super(partItem);

		this.peripheralLazyOptional = LazyOptional.of(() -> new ComputerInterfacePeripheral(this));
	}

	@Override
	public IPartModel getStaticModels() {
		NetworkStatus status = NetworkStatus.withNode(this);
		return switch (status){
			case ON -> MODEL_ON;
			case OFF -> MODEL_OFF;
			case HAS_CHANNEL -> MODEL_HAS_CHANNEL;
		};
	}

	@Override
	public void getBoxes(@Nonnull IPartCollisionHelper bch) {
		bch.addBox(2, 2, 14, 14, 14, 16);
		bch.addBox(5, 5, 12, 11, 11, 14);
	}

	@Override
	public void removeFromWorld() {
		super.removeFromWorld();
		this.peripheralLazyOptional.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap) {
		if (cap == ComputerCraftCapability.PERIPHERAL)
			return this.peripheralLazyOptional.cast();

		return super.getCapability(cap);
	}
}
