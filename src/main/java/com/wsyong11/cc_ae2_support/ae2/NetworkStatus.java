package com.wsyong11.cc_ae2_support.ae2;

import appeng.api.networking.IGridNode;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Objects;

public enum NetworkStatus implements StringRepresentable {
	OFF("off"),
	ON("on"),
	HAS_CHANNEL("has_channel");

	private final String name;

	NetworkStatus(@Nonnull String name) {
		Objects.requireNonNull(name, "name is null");
		this.name = name;
	}

	@Nonnull
	@Override
	public String getSerializedName() {
		return this.name;
	}

	@Nonnull
	public static NetworkStatus withNode(@Nonnull IAENetworkObject networkObject) {
		Objects.requireNonNull(networkObject, "networkObject is null");

		if (networkObject.isActive()) return HAS_CHANNEL;
		else if (networkObject.isPowered()) return ON;
		else return OFF;
	}
}
