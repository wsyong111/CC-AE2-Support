package com.wsyong11.core_mod.register.blockentity;

import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.LazyRegister;
import com.wsyong11.core_mod.register.block.BlockRegisterObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class LazyBlockEntityRegister<T extends BlockEntity> extends LazyRegister<BlockEntityType<?>, BlockEntityRegisterObject<T>, LazyBlockEntityRegister<T>, LazyBlockEntityRegister.BlockEntityBuilderSupplier<T>> {
	private final List<BlockRegisterObject<? extends Block>> validBlocks;

	public LazyBlockEntityRegister(@Nonnull DeferredRegister<BlockEntityType<?>> deferredRegister, @Nonnull String id) {
		super(deferredRegister, id);
		this.validBlocks = new ArrayList<>();
	}

	@Nonnull
	public LazyBlockEntityRegister<T> validBlock(@Nonnull BlockRegisterObject<? extends Block> blockRegistryObject) {
		Objects.requireNonNull(blockRegistryObject, "blockRegistryObject is null");
		this.validBlocks.add(blockRegistryObject);
		return this;
	}

	@SuppressWarnings("ConstantConditions")
	@Nonnull
	@Override
	public BlockEntityRegisterObject<T> build() {
		Objects.requireNonNull(this.objectBuilder, "Requires specifying block entity builder");

		BlockEntityProvider<T> provider = new BlockEntityProvider<>(this.objectBuilder);

		RegistryObject<BlockEntityType<T>> registryObject = this.deferredRegister.register(
			this.id,
			() -> {
				Block[] validBlocks = this.validBlocks.stream()
				                                      .map(BlockRegisterObject::get)
				                                      .toArray(Block[]::new);

				Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, this.id);

				return BlockEntityType.Builder.of(
					provider,
					validBlocks
				).build(type);
			}
		);

		provider.setType(registryObject);

		return new BlockEntityRegisterObject<>(registryObject);
	}

	private static class BlockEntityProvider<C extends BlockEntity> implements BlockEntityType.BlockEntitySupplier<C> {
		private final BlockEntityBuilderSupplier<C> builder;
		private Supplier<BlockEntityType<C>> type;

		public BlockEntityProvider(@Nonnull BlockEntityBuilderSupplier<C> builder) {
			Objects.requireNonNull(builder, "builder is null");
			this.builder = builder;
			this.type = null;
		}

		public void setType(@Nonnull Supplier<BlockEntityType<C>> type) {
			Objects.requireNonNull(type, "type is null");
			this.type = type;
		}

		@Nonnull
		@Override
		public C create(@Nonnull BlockPos pos, @Nonnull BlockState state) {
			if (this.type == null)
				throw new IllegalStateException("Cannot get entity type");
			return builder.create(this.type.get(), pos, state);
		}
	}

	/**
	 * 方块实体构建工厂函数
	 */
	@FunctionalInterface
	public interface BlockEntityBuilderSupplier<C extends BlockEntity> {
		@Nonnull
		C create(@Nonnull BlockEntityType<C> type, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState);
	}
}
