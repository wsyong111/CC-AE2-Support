package com.wsyong11.core_mod.register;

import javax.annotation.Nonnull;

public interface IPropertiesBuilder<T, P extends ILazyRegister<?, P, ?>> {
	@Nonnull
	T build();

	@Nonnull
	P parent();
}
