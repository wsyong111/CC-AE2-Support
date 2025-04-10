package com.wsyong11.core_mod.register;

import javax.annotation.Nonnull;

public interface ILazyRegister<T extends IRegisterObject<?, ?>, THIS extends ILazyRegister<T, THIS, B>, B> {
	@Nonnull
	THIS builder(@Nonnull B objectBuilder);

	@Nonnull
	T build();
}
