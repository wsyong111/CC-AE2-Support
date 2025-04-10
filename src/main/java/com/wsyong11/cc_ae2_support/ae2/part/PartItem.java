package com.wsyong11.cc_ae2_support.ae2.part;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class PartItem<T extends IPart> extends Item implements IPartItem<T> {
	private final Class<T> partClass;
	private final Function<IPartItem<T>, T> factory;

	public PartItem(@Nonnull Properties properties, @Nonnull Class<T> partClass, @Nonnull Function<IPartItem<T>, T> factory) {
		super(properties);

		this.partClass = partClass;
		this.factory = factory;
	}

	@Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        return PartHelper.usePartItem(context);
    }

    @Override
    public Class<T> getPartClass() {
        return this.partClass;
    }

    @Override
    public T createPart() {
        return this.factory.apply(this);
    }

}
