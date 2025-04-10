package com.wsyong11.cc_ae2_support.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import com.wsyong11.cc_ae2_support.block.entity.BaseBlockEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class AbstractEntityBlock<T extends BaseBlockEntity> extends BaseEntityBlock {
	private final Class<T> blockEntityClass;

	protected AbstractEntityBlock(@Nonnull Properties properties) {
		super(properties);
		this.blockEntityClass = this.getBlockEntityClass();
	}

	@Nullable
	@Override
	public abstract T newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state);

	@Nonnull
	protected abstract Class<T> getBlockEntityClass();

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	public T getBlockEntity(@Nonnull Level level, @Nonnull BlockPos pos) {
		Objects.requireNonNull(level, "level is null");
		Objects.requireNonNull(pos, "pos is null");

		BlockEntity blockEntity = level.getBlockEntity(pos);

		if (this.blockEntityClass.isInstance(blockEntity))
			return this.blockEntityClass.cast(blockEntity);

		return null;
	}

	@Override
	public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity entity, @Nonnull ItemStack item) {
		if (entity instanceof Player player) {
			T blockEntity = this.getBlockEntity(level, pos);
			if (blockEntity == null) return;

			blockEntity.onPlaceByPlayer(player);
		}
	}
}
