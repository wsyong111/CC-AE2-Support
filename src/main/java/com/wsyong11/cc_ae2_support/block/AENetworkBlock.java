package com.wsyong11.cc_ae2_support.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import com.wsyong11.cc_ae2_support.ae2.NetworkStatus;
import com.wsyong11.cc_ae2_support.block.entity.AENetworkBlockEntity;

import javax.annotation.Nonnull;

public abstract class AENetworkBlock<T extends AENetworkBlockEntity> extends AbstractEntityBlock<T> {
	public static final EnumProperty<NetworkStatus> STATUS = EnumProperty.create("status", NetworkStatus.class);

	protected AENetworkBlock(@Nonnull BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this
			.defaultBlockState()
			.setValue(STATUS, NetworkStatus.OFF));
	}

	@Override
	protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATUS);
	}
}
