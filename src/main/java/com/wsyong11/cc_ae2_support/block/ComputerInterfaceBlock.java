package com.wsyong11.cc_ae2_support.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import com.wsyong11.cc_ae2_support.block.entity.BlocksEntity;
import com.wsyong11.cc_ae2_support.block.entity.ComputerInterfaceBlockEntity;

import javax.annotation.Nonnull;

public class ComputerInterfaceBlock extends AENetworkBlock<ComputerInterfaceBlockEntity> {
	public ComputerInterfaceBlock(@Nonnull Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public ComputerInterfaceBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState blockState) {
		return BlocksEntity.COMPUTER_INTERFACE.create(pos, blockState);
	}

	@Nonnull
	@Override
	protected Class<ComputerInterfaceBlockEntity> getBlockEntityClass() {
		return ComputerInterfaceBlockEntity.class;
	}
}
