package com.wsyong11.core_mod.register.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IRegisterObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ItemRegisterObject<T extends Item> implements IRegisterObject<T, Item>, ItemLike {
	private final RegistryObject<Item> registryObject;

	public ItemRegisterObject(@Nonnull RegistryObject<Item> registryObject) {
		Objects.requireNonNull(registryObject, "registryObject is null");
		this.registryObject = registryObject;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public T get() {
		return (T) this.registryObject.get();
	}

	@Nullable
	@Override
	public ResourceKey<Item> getKey() {
		return this.registryObject.getKey();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return this.registryObject.getId();
	}

	@Nonnull
	@Override
	public Item asItem() {
		return this.get();
	}
}
