package com.wsyong11.core_mod.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.wsyong11.core_mod.register.block.LazyBlockRegister;
import com.wsyong11.core_mod.register.blockentity.LazyBlockEntityRegister;
import com.wsyong11.core_mod.register.creativemodetab.LazyCreativeModeTabRegister;
import com.wsyong11.core_mod.register.entity.LazyEntityRegister;
import com.wsyong11.core_mod.register.item.LazyItemRegister;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@SuppressWarnings("unused")
public class Registers {
	private final DeferredRegister<Block> block;
	private final DeferredRegister<Item> item;
	private final DeferredRegister<BlockEntityType<?>> blockEntity;
	private final DeferredRegister<EntityType<?>> entity;
	private final DeferredRegister<CreativeModeTab> tab;

	private Registers(@Nonnull String modId, @Nonnull IEventBus eventBus) {
		Objects.requireNonNull(modId, "modId is null");
		Objects.requireNonNull(eventBus, "eventBus is null");

		this.block = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
		this.item = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
		this.blockEntity = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, modId);
		this.entity = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, modId);
		this.tab = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);

		this.block.register(eventBus);
		this.item.register(eventBus);
		this.blockEntity.register(eventBus);
		this.entity.register(eventBus);
		this.tab.register(eventBus);
	}

	@Nonnull
	public static Registers create(@Nonnull String modId,@Nonnull IEventBus eventBus) {
		Objects.requireNonNull(modId, "modId is null");
		Objects.requireNonNull(eventBus, "eventBus is null");
		return new Registers(modId, eventBus);
	}

	@Nonnull
	public <V extends Block> LazyBlockRegister<V> block(@Nonnull String id, @Nullable Class<V> classType) {
		Objects.requireNonNull(id, "id is null");
		return new LazyBlockRegister<>(this.block, this.item, id);
	}

	@Nonnull
	public <V extends Block> LazyBlockRegister<V> block(@Nonnull String id, @Nullable Type<V> type) {
		Objects.requireNonNull(id, "id is null");
		return new LazyBlockRegister<>(this.block, this.item, id);
	}

	@Nonnull
	public <V extends Item> LazyItemRegister<V> item(@Nonnull String id, @Nullable Class<V> classType) {
		Objects.requireNonNull(id, "id is null");
		return new LazyItemRegister<>(this.item, id);
	}

	@Nonnull
	public <V extends Item> LazyItemRegister<V> item(@Nonnull String id, @Nullable Type<V> type) {
		Objects.requireNonNull(id, "id is null");
		return new LazyItemRegister<>(this.item, id);
	}

	@Nonnull
	public <V extends BlockEntity> LazyBlockEntityRegister<V> blockEntity(@Nonnull String id, @Nullable Class<V> classType) {
		Objects.requireNonNull(id, "id is null");
		return new LazyBlockEntityRegister<>(this.blockEntity, id);
	}

	@Nonnull
	public <V extends BlockEntity> LazyBlockEntityRegister<V> blockEntity(@Nonnull String id, @Nullable Type<V> type) {
		Objects.requireNonNull(id, "id is null");
		return new LazyBlockEntityRegister<>(this.blockEntity, id);
	}

	@Nonnull
	public <V extends Entity> LazyEntityRegister<V> entity(@Nonnull String id, @Nullable Class<V> classType) {
		Objects.requireNonNull(id, "id is null");
		return new LazyEntityRegister<>(this.entity, id);
	}

	@Nonnull
	public <V extends Entity> LazyEntityRegister<V> entity(@Nonnull String id, @Nullable Type<V> type) {
		Objects.requireNonNull(id, "id is null");
		return new LazyEntityRegister<>(this.entity, id);
	}

	@Nonnull
	public LazyCreativeModeTabRegister tab(@Nonnull String id) {
		Objects.requireNonNull(id, "id is null");
		return new LazyCreativeModeTabRegister(this.tab, id);
	}

	@SuppressWarnings("unused")
	public static class Type<V> {
		public Type() { /* no-op */ }
	}
}
