package com.wsyong11.cc_ae2_support.ae2.part.model;

import appeng.api.parts.IPartModel;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PartModel implements IPartModel {
	private final List<ResourceLocation> models;

	public PartModel(@Nonnull ResourceLocation... models) {
		this(List.of(models));
	}

	public PartModel(@Nonnull List<ResourceLocation> models) {
		Objects.requireNonNull(models, "models is null");
		this.models = models;
	}

	@Override
	public List<ResourceLocation> getModels() {
		return this.models;
	}
}
