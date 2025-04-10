package com.wsyong11.core_mod.register.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import com.wsyong11.core_mod.register.IPropertiesBuilder;
import com.wsyong11.core_mod.register.LazyRegister;
import com.wsyong11.core_mod.register.creativemodetab.CreativeModeTabRegisterObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * 简单方块构造器
 */
public class LazyBlockRegister<T extends Block> extends LazyRegister<Block, BlockRegisterObject<T>, LazyBlockRegister<T>, LazyBlockRegister.BlockBuilderSupplier<T>> {
	private final BlockPropertiesBuilder propertiesBuilder;
	private final DeferredRegister<Item> itemDeferredRegister;

	private boolean noItem;

	@Nullable
	private CreativeModeTabRegisterObject creativeModeTab;

	public LazyBlockRegister(@Nonnull DeferredRegister<Block> deferredRegister, @Nonnull DeferredRegister<Item> itemDeferredRegister, @Nonnull String id) {
		super(deferredRegister, id);
		Objects.requireNonNull(itemDeferredRegister, "itemDeferredRegister is null");

		this.itemDeferredRegister = itemDeferredRegister;

		this.propertiesBuilder = new BlockPropertiesBuilder();
		this.noItem = false;
		this.creativeModeTab = null;
	}

	/**
	 * 获取方块属性构建器
	 *
	 * @return 方块属性构建器
	 */
	@Nonnull
	public BlockPropertiesBuilder properties() {
		return this.propertiesBuilder;
	}

	/**
	 * 方块将不会注册物品
	 */
	@Nonnull
	public LazyBlockRegister<T> noItem() {
		this.noItem = true;
		return this;
	}

	/**
	 * 设定物品所在的分类
	 *
	 * @param tab 分类
	 */
	@Nonnull
	public LazyBlockRegister<T> tab(@Nullable CreativeModeTabRegisterObject tab) {
		this.creativeModeTab = tab;
		return this;
	}

	/**
	 * 构建方块
	 *
	 * @return 方块注册对象
	 */
	@Nonnull
	@Override
	public BlockRegisterObject<T> build() {
		Objects.requireNonNull(this.objectBuilder, "Requires specifying block builder");

		RegistryObject<Block> blockRegistry = this.deferredRegister.register(this.id, () ->
			this.objectBuilder.create(this.propertiesBuilder.build())
		);

		RegistryObject<BlockItem> itemRegistry;
		if (!this.noItem) {
			Item.Properties itemProperties = new Item.Properties();

			itemRegistry = this.itemDeferredRegister.register(
				this.id,
				() -> new BlockItem(blockRegistry.get(), itemProperties)
			);

			if (this.creativeModeTab != null)
				this.creativeModeTab.addItem(itemRegistry::get);
		} else {
			itemRegistry = null;
		}

		return new BlockRegisterObject<>(blockRegistry, itemRegistry);
	}

	/**
	 * 方块构建工厂函数
	 */
	@FunctionalInterface
	public interface BlockBuilderSupplier<C extends Block> {
		C create(@Nonnull BlockBehaviour.Properties properties);
	}

	/**
	 * 方块属性构建器
	 */
	public class BlockPropertiesBuilder implements IPropertiesBuilder<BlockBehaviour.Properties, LazyBlockRegister<T>> {
		private final BlockBehaviour.Properties properties;

		BlockPropertiesBuilder() {
			this.properties = BlockBehaviour.Properties.of();
		}

		/**
		 * 设定方块的硬度和爆炸抗性
		 *
		 * @param hardness   方块硬度
		 * @param resistance 方块爆炸抗性
		 */
		@Nonnull
		public BlockPropertiesBuilder strength(float hardness, float resistance) {
			this.properties.strength(hardness, resistance);
			return this;
		}

		/**
		 * 设定方块的硬度和爆炸抗性
		 *
		 * @param strength 方块硬度和爆炸抗性
		 */
		@Nonnull
		public BlockPropertiesBuilder strength(float strength) {
			return this.strength(strength, strength);
		}

		/**
		 * 设定方块的亮度
		 *
		 * @param lightLevel 方块亮度等级
		 */
		@Nonnull
		public BlockPropertiesBuilder lightLevel(int lightLevel) {
			this.properties.lightLevel((blockState) -> lightLevel);
			return this;
		}

		/**
		 * 设定方块不会遮挡
		 */
		@Nonnull
		public BlockPropertiesBuilder noOcclusion() {
			this.properties.noOcclusion();
			return this;
		}

		@Nonnull
		public BlockPropertiesBuilder noSuffocating() {
			this.properties.isSuffocating((a, b, c) -> false);
			return this;
		}

		@Nonnull
		public BlockPropertiesBuilder noViewBlocking() {
			this.properties.isViewBlocking((a, b, c) -> false);
			return this;
		}

		/**
		 * 设定方块的亮度
		 *
		 * @param lightLevelSuppler 方块亮度提供器
		 */
		@Nonnull
		public BlockPropertiesBuilder lightLevel(@Nonnull ToIntFunction<BlockState> lightLevelSuppler) {
			Objects.requireNonNull(lightLevelSuppler, "lightLevelSuppler is null");
			this.properties.lightLevel(lightLevelSuppler);
			return this;
		}

		@Nonnull
		public BlockPropertiesBuilder requiresCorrectToolForDrops() {
			this.properties.requiresCorrectToolForDrops();
			return this;
		}

		@Nonnull
		public BlockPropertiesBuilder notRedstoneConductor() {
			this.properties.isRedstoneConductor((b, l, p) -> false);
			return this;
		}

		@Nonnull
		public BlockPropertiesBuilder noCollission() {
			this.properties.noCollission();
			return this;
		}

		/**
		 * 方块音效类型
		 */
		@Nonnull
		public BlockPropertiesBuilder sound(@Nonnull SoundType soundType) {
			Objects.requireNonNull(soundType, "soundType is null");
			this.properties.sound(soundType);
			return this;
		}

		@Nonnull
		@Override
		public BlockBehaviour.Properties build() {
			return this.properties;
		}

		@Nonnull
		@Override
		public LazyBlockRegister<T> parent() {
			return LazyBlockRegister.this;
		}
	}
}
