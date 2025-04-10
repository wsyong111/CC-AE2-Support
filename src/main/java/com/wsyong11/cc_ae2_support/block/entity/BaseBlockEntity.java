package com.wsyong11.cc_ae2_support.block.entity;

import appeng.api.networking.GridHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public abstract class BaseBlockEntity extends BlockEntity {
	public BaseBlockEntity(@Nonnull BlockEntityType<?> type, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		super(type, pos, state);
	}

	public void onPlaceByPlayer(@Nonnull Player player) {
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	protected void saveAdditional(@Nonnull CompoundTag tag) {
		super.saveAdditional(tag);
	}

	protected void loadAdditional(@Nonnull CompoundTag tag) {
		super.load(tag);
	}

	@Override
	public final void load(@Nonnull CompoundTag tag) {
		this.loadAdditional(tag);
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	public void clearRemoved() {
		super.clearRemoved();
		this.scheduleInit();
	}

	private void scheduleInit() {
		GridHelper.onFirstTick(this, BaseBlockEntity::onReady);
	}

	protected void onReady() {
	}
}
