package com.wsyong11.core_mod.register.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IRegisterObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BlockRegisterObject<T extends Block> implements IRegisterObject<T, Block>, ItemLike {
	private final RegistryObject<Block> blockRegistry;

	@Nullable
	private final RegistryObject<BlockItem> itemRegistry;

	public BlockRegisterObject(@Nonnull RegistryObject<Block> blockRegistry, @Nullable RegistryObject<BlockItem> itemRegistry) {
		Objects.requireNonNull(blockRegistry, "blockRegistry is null");

		this.blockRegistry = blockRegistry;
		this.itemRegistry = itemRegistry;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public T get() {
		return (T) this.blockRegistry.get();
	}

	@Nonnull
	public BlockItem getItem() {
		if (this.itemRegistry == null) {
			throw new NullPointerException("Block doesn't have a block item");
		}

		return this.itemRegistry.get();
	}

	@Nullable
	@Override
	public ResourceKey<Block> getKey() {
		return this.blockRegistry.getKey();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return this.blockRegistry.getId();
	}

	@Nonnull
	@Override
	public Item asItem() {
		return this.getItem();
	}
}
