package com.wsyong11.cc_ae2_support.cc.peripheral.manager;

import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class LuaManagerInterface<T> {
	private final IManagedGridNode node;

	@Nullable
	private volatile T manager;
	private volatile boolean isReset;

	public LuaManagerInterface(@Nonnull IManagedGridNode node) {
		Objects.requireNonNull(node, "node is null");
		this.node = node;
		this.isReset = true;
	}

	@Nonnull
	public IManagedGridNode getNode() {
		return this.node;
	}

	@Nullable
	protected synchronized T getManager() {
		if (this.isReset) {
			this.isReset = false;

			IGrid grid = this.node.getGrid();
			this.manager = grid == null || !this.node.isOnline() ? null : this.getManagerInstance(grid);
		}
		return this.manager;
	}

	@Nullable
	protected abstract T getManagerInstance(@Nonnull IGrid grid);

	public synchronized void reset() {
		this.isReset = true;
		this.manager = null;
	}
}
