package com.wsyong11.core_mod.register.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IPropertiesBuilder;
import com.wsyong11.core_mod.register.LazyRegister;
import com.wsyong11.core_mod.register.creativemodetab.CreativeModeTabRegisterObject;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class LazyItemRegister<T extends Item> extends LazyRegister<Item, ItemRegisterObject<T>, LazyItemRegister<T>, LazyItemRegister.ItemBuilderSupplier<T>> {
	private final ItemPropertiesBuilder propertiesBuilder;

	@Nullable
	private CreativeModeTabRegisterObject creativeModeTab;

	public LazyItemRegister(@Nonnull DeferredRegister<Item> deferredRegister, @Nonnull String id) {
		super(deferredRegister, id);

		this.propertiesBuilder = new ItemPropertiesBuilder();
		this.creativeModeTab = null;
	}

	@Nonnull
	public LazyItemRegister<T> tab(@Nullable CreativeModeTabRegisterObject tab) {
		this.creativeModeTab = tab;
		return this;
	}

	@Nonnull
	public ItemPropertiesBuilder properties() {
		return this.propertiesBuilder;
	}

	@Nonnull
	@Override
	public ItemRegisterObject<T> build() {
		Objects.requireNonNull(this.objectBuilder, "Requires specifying item builder");

		RegistryObject<Item> registryObject = this.deferredRegister.register(
			this.id,
			() -> this.objectBuilder.create(this.propertiesBuilder.build())
		);

		if (this.creativeModeTab != null)
			this.creativeModeTab.addItem(registryObject);

		return new ItemRegisterObject<>(registryObject);
	}

	/**
	 * 物品构建工厂函数
	 */
	@FunctionalInterface
	public interface ItemBuilderSupplier<C extends ItemLike> {
		@Nonnull
		C create(@Nonnull Item.Properties properties);
	}

	public class ItemPropertiesBuilder implements IPropertiesBuilder<Item.Properties, LazyItemRegister<T>> {
		private final Item.Properties properties;

		public ItemPropertiesBuilder() {
			this.properties = new Item.Properties();
		}

		@Nonnull
		public ItemPropertiesBuilder stackTo(@Nonnegative int count) {
			this.properties.stacksTo(count);
			return this;
		}

		@Nonnull
		@Override
		public Item.Properties build() {
			return this.properties;
		}

		@Nonnull
		@Override
		public LazyItemRegister<T> parent() {
			return LazyItemRegister.this;
		}
	}
}
