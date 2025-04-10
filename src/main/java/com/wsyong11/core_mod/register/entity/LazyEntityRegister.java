package com.wsyong11.core_mod.register.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.LazyRegister;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LazyEntityRegister<T extends Entity> extends LazyRegister<EntityType<?>, EntityRegisterObject<T>, LazyEntityRegister<T>, LazyEntityRegister.EntityBuilderSupplier<T>> {
	private MobCategory category;

	public LazyEntityRegister(@Nonnull DeferredRegister<EntityType<?>> deferredRegister, @Nonnull String id) {
		super(deferredRegister,id);
		this.category = MobCategory.MISC;
	}

	@Nonnull
	public LazyEntityRegister<T> category(@Nonnull MobCategory category) {
		Objects.requireNonNull(category, "category is null");
		this.category = category;
		return this;
	}

	@Nonnull
	@Override
	public EntityRegisterObject<T> build() {
		Objects.requireNonNull(this.objectBuilder, "Requires specifying entity builder");

		RegistryObject<EntityType<T>> registryObject = this.deferredRegister.register(
			this.id,
			() -> EntityType.Builder.of(this.objectBuilder, this.category)
			                        .build(this.id)
		);
		return new EntityRegisterObject<>(registryObject);
	}

	/**
	 * 方块实体构建工厂函数
	 */
	@FunctionalInterface
	public interface EntityBuilderSupplier<C extends Entity> extends EntityType.EntityFactory<C> {
		@Nonnull
		@Override
		C create(@Nonnull EntityType<C> type, @Nonnull Level level);
	}
}
