package com.wsyong11.cc_ae2_support.ae2;

import appeng.api.networking.IGridNode;
import appeng.api.networking.IManagedGridNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAENetworkObject {
	@Nonnull
	IManagedGridNode getMainNode();

	@Nullable
	IGridNode getGridNode();

    boolean isPowered();

    boolean isMissingChannel();

    default boolean isActive(){
	    return this.isPowered() && !this.isMissingChannel();
    }
}
