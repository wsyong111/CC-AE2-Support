package com.wsyong11.cc_ae2_support.ae2.part.model;

import appeng.api.parts.PartModels;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wsyong11.cc_ae2_support.Constains.MOD_ID;

public final class PartModelRegistry {
	private static final List<ResourceLocation> partIds = new ArrayList<>();

	public static final ResourceLocation COMPUTER_INTERFACE_BASE = part("part/computer_interface_base");
	public static final ResourceLocation COMPUTER_INTERFACE_OFF = part("part/computer_interface_off");
	public static final ResourceLocation COMPUTER_INTERFACE_ON = part("part/computer_interface_on");
	public static final ResourceLocation COMPUTER_INTERFACE_HAS_CHANNEL = part("part/computer_interface_has_channel");

	@Nonnull
	private static ResourceLocation part(@Nonnull String location) {
		Objects.requireNonNull(location, "location is null");
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
		partIds.add(id);
		return id;
	}

	public static void init() {
		PartModels.registerModels(partIds);
	}

	private PartModelRegistry() { /* no-op */ }
}
