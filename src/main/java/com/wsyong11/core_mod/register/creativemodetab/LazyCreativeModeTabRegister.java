package com.wsyong11.core_mod.register.creativemodetab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import com.wsyong11.core_mod.register.ILazyRegister;
import com.wsyong11.core_mod.register.block.BlockRegisterObject;
import com.wsyong11.core_mod.register.item.ItemRegisterObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class LazyCreativeModeTabRegister implements ILazyRegister<CreativeModeTabRegisterObject, LazyCreativeModeTabRegister, Void> {
	private final DeferredRegister<CreativeModeTab> deferredRegister;
	private final String id;

	private final CreativeModeTab.Builder builder;

	public LazyCreativeModeTabRegister(@Nonnull DeferredRegister<CreativeModeTab> deferredRegister, @Nonnull String id) {
		Objects.requireNonNull(deferredRegister, "deferredRegister is null");
		this.deferredRegister = deferredRegister;
		this.id = id;
		this.builder = CreativeModeTab.builder();
	}

	@Deprecated
	@NotNull
	@Override
	public LazyCreativeModeTabRegister builder(@NotNull Void objectBuilder) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public CreativeModeTabRegisterObject build() {
		List<Supplier<Item>> displayItemList = new ArrayList<>();

		this.builder.displayItems((parameters, output) ->
			displayItemList.stream()
			               .map(Supplier::get)
			               .forEachOrdered(output::accept));

		RegistryObject<CreativeModeTab> registryObject = this.deferredRegister.register(this.id, this.builder::build);
		return new CreativeModeTabRegisterObject(registryObject, displayItemList);
	}

	@Nonnull
	public LazyCreativeModeTabRegister title(@Nonnull Component title) {
		Objects.requireNonNull(title, "title is null");
		this.builder.title(title);
		return this;
	}

	@Nonnull
	public LazyCreativeModeTabRegister icon(@Nonnull Supplier<ItemLike> icon) {
		Objects.requireNonNull(icon, "icon is null");
		this.builder.icon(Lazy.of(() -> icon.get().asItem().getDefaultInstance()));
		return this;
	}
}
