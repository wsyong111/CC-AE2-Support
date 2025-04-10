package com.wsyong11.core_mod.register;

import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class LazyRegister<D, T extends IRegisterObject<?, ?>, THIS extends ILazyRegister<T, THIS, B>, B> implements ILazyRegister<T, THIS, B> {
	protected final DeferredRegister<D> deferredRegister;
	protected final String id;

	@Nullable
	protected B objectBuilder;

	public LazyRegister(@Nonnull DeferredRegister<D> deferredRegister, @Nonnull String id) {
		Objects.requireNonNull(deferredRegister, "deferredRegister is null");
		Objects.requireNonNull(id, "id is null");

		this.deferredRegister = deferredRegister;
		this.id = id;
		this.objectBuilder = null;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public THIS builder(@Nonnull B builder) {
		Objects.requireNonNull(builder, "builder is null");
		this.objectBuilder = builder;
		return (THIS) this;
	}
}
