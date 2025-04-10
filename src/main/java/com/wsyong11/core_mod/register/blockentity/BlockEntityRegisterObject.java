package com.wsyong11.core_mod.register.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IRegisterObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BlockEntityRegisterObject<T extends BlockEntity> implements IRegisterObject<BlockEntityType<T>, BlockEntityType<T>> {
	private final RegistryObject<BlockEntityType<T>> registryObject;

	public BlockEntityRegisterObject(@Nonnull RegistryObject<BlockEntityType<T>> registryObject) {
		Objects.requireNonNull(registryObject, "registryObject is null");
		this.registryObject = registryObject;
	}

	@Nonnull
	@Override
	public BlockEntityType<T> get() {
		return this.registryObject.get();
	}

	@Nullable
	public T create(@Nonnull BlockPos pos, @Nonnull BlockState blockState) {
		Objects.requireNonNull(pos, "pos is null");
		Objects.requireNonNull(blockState, "blockState is null");
		return this.get().create(pos, blockState);
	}

	@Nullable
	@Override
	public ResourceKey<BlockEntityType<T>> getKey() {
		return this.registryObject.getKey();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return this.registryObject.getId();
	}
}
