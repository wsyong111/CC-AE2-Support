package com.wsyong11.cc_ae2_support.block.entity;

import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IManagedGridNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import com.wsyong11.cc_ae2_support.ae2.IAENetworkObject;
import com.wsyong11.cc_ae2_support.block.Blocks;
import com.wsyong11.cc_ae2_support.cc.ComputerCraftCapability;
import com.wsyong11.cc_ae2_support.cc.peripheral.ComputerInterfacePeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ComputerInterfaceBlockEntity extends AENetworkBlockEntity implements ICapabilityProvider {
	private final LazyOptional<ComputerInterfacePeripheral> peripheralLazyOptional;

	public ComputerInterfaceBlockEntity(@Nonnull BlockEntityType<?> entityType, @Nonnull BlockPos pos, @Nonnull BlockState blockState) {
		super(entityType, pos, blockState);

		this.peripheralLazyOptional = LazyOptional.of(() -> new ComputerInterfacePeripheral(this));
	}

	@Override
	protected void setupMainNode(@Nonnull IManagedGridNode node) {
		super.setupMainNode(node);
		node.setVisualRepresentation(Blocks.COMPUTER_INTERFACE)
			.setIdlePowerUsage(10.0D);
	}

	@Override
	protected void onNodeStateChanged(@Nonnull IGridNodeListener.State state) {
		super.onNodeStateChanged(state);
		if (state != IGridNodeListener.State.POWER) return;

		this.peripheralLazyOptional.ifPresent(ComputerInterfacePeripheral::reset);
	}

	@Override
	public void onChunkUnloaded() {
		super.onChunkUnloaded();
		this.peripheralLazyOptional.invalidate();
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		this.peripheralLazyOptional.invalidate();
	}

	@Override
	public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ComputerCraftCapability.PERIPHERAL)
			return this.peripheralLazyOptional.cast();

		return super.getCapability(cap, side);
	}
}
