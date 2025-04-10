package com.wsyong11.cc_ae2_support.ae2.part;

import appeng.api.networking.*;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.util.AECableType;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import com.wsyong11.cc_ae2_support.ae2.IAENetworkObject;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Objects;

public abstract class BasePart implements IPart, IAENetworkObject {
	private static final IGridNodeListener<BasePart> GRID_NODE_LISTENER = (nodeOwner, node) -> {
	};

	private static final byte FLAG_MISSING_CHANNEL = 1;
	private static final byte FLAG_POWERED = 2;

	private final IPartItem<?> partItem;
	private final IManagedGridNode mainNode;

	private BlockEntity blockEntity;

	// Client
	private boolean clientSideMissingChannel;
	private boolean clientSidePowered;

	public BasePart(@Nonnull IPartItem<?> partItem) {
		Objects.requireNonNull(partItem, "partItem is null");

		this.partItem = partItem;

		this.mainNode = GridHelper.createManagedNode(this, GRID_NODE_LISTENER);
		this.setupMainNode(this.mainNode);

		this.blockEntity = null;

		this.clientSideMissingChannel = false;
		this.clientSidePowered = false;
	}

	protected void setupMainNode(@Nonnull IManagedGridNode node) {
		node.setFlags(GridFlags.REQUIRE_CHANNEL)
		    .setExposedOnSides(EnumSet.noneOf(Direction.class))
		    .setIdlePowerUsage(4.0D);
	}

	@Override
	public abstract IPartModel getStaticModels();

	// -------------------------------------------------------------------------------------------------------------- //

	protected final boolean isClientSide() {
		return this.blockEntity == null
		       || this.blockEntity.getLevel() == null
		       || this.blockEntity.getLevel().isClientSide();
	}

	@Override
	public boolean readFromStream(@Nonnull FriendlyByteBuf data) {
		byte statusFlag = data.readByte();

		boolean lastMissingChannelValue = this.clientSideMissingChannel;
		boolean lastPoweredValue = this.clientSidePowered;

		this.clientSideMissingChannel = (statusFlag & FLAG_MISSING_CHANNEL) == FLAG_MISSING_CHANNEL;
		this.clientSidePowered = (statusFlag & FLAG_POWERED) == FLAG_POWERED;

		return this.clientSideMissingChannel != lastMissingChannelValue
		       || this.clientSidePowered != lastPoweredValue;
	}

	@Override
	public void writeToStream(@Nonnull FriendlyByteBuf data) {
		byte statusFlag = 0;
		if (this.isMissingChannel()) statusFlag |= FLAG_MISSING_CHANNEL;
		if (this.isPowered()) statusFlag |= FLAG_POWERED;

		data.writeByte(statusFlag);
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	public IPartItem<?> getPartItem() {
		return this.partItem;
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Nonnull
	public IManagedGridNode getMainNode() {
		return this.mainNode;
	}

	@Nullable
	@Override
	public IGridNode getGridNode() {
		return this.mainNode.getNode();
	}

	@Override
	public boolean isPowered() {
		if (this.isClientSide()) return this.clientSidePowered;
		return this.getMainNode().isPowered();
	}

	@Override
	public boolean isMissingChannel() {
		if (this.isClientSide()) return this.clientSideMissingChannel;

		IGridNode node = this.getMainNode().getNode();
		return node == null || !node.meetsChannelRequirements();
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	public void setPartHostInfo(@Nullable Direction side, @Nonnull IPartHost host, @Nonnull BlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	@Override
	public float getCableConnectionLength(@Nonnull AECableType cable) {
		return 3;
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	public void removeFromWorld() {
		this.mainNode.destroy();
	}

	@Override
	public void addToWorld() {
		this.mainNode.create(this.blockEntity.getLevel(), this.blockEntity.getBlockPos());
	}
}
