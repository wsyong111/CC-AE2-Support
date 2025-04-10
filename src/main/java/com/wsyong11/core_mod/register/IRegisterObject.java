package com.wsyong11.core_mod.register;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRegisterObject<T, RK> {
	@Nonnull
	T get();

	@Nullable
	ResourceKey<RK> getKey();

	@Nonnull
	ResourceLocation getId();
}
