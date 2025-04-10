package com.wsyong11.core_mod.register.creativemodetab;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IRegisterObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class CreativeModeTabRegisterObject implements IRegisterObject<CreativeModeTab, CreativeModeTab> {
	private final RegistryObject<CreativeModeTab> registry;
	private final List<Supplier<Item>> itemSupplierList;

	public CreativeModeTabRegisterObject(@Nonnull RegistryObject<CreativeModeTab> registry, @Nonnull List<Supplier<Item>> itemSupplierList) {
		Objects.requireNonNull(registry, "registry is null");
		Objects.requireNonNull(itemSupplierList, "itemSupplierList is null");
		this.registry = registry;
		this.itemSupplierList = itemSupplierList;
	}

	@Nonnull
	@Override
	public CreativeModeTab get() {
		return this.registry.get();
	}

	@Nullable
	@Override
	public ResourceKey<CreativeModeTab> getKey() {
		return this.registry.getKey();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return this.registry.getId();
	}

	public void addItem(@Nonnull Supplier<Item> itemSupplier) {
		Objects.requireNonNull(itemSupplier, "itemSupplier is null");
		this.itemSupplierList.add(itemSupplier);
	}
}
