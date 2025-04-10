package com.wsyong11.core_mod.register.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IRegisterObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class EntityRegisterObject<T extends Entity> implements IRegisterObject<EntityType<T>, EntityType<T>> {
	private final RegistryObject<EntityType<T>> registryObject;

	public EntityRegisterObject(@Nonnull RegistryObject<EntityType<T>> registryObject) {
		Objects.requireNonNull(registryObject, "registryObject is null");
		this.registryObject = registryObject;
	}

	@Nonnull
	@Override
	public EntityType<T> get() {
		return this.registryObject.get();
	}

	@Nullable
	@Override
	public ResourceKey<EntityType<T>> getKey() {
		return this.registryObject.getKey();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return this.registryObject.getId();
	}
}
