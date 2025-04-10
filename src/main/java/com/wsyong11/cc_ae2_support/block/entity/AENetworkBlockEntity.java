package com.wsyong11.cc_ae2_support.block.entity;

import appeng.api.networking.*;
import appeng.api.util.AECableType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.wsyong11.cc_ae2_support.ae2.IAENetworkObject;
import com.wsyong11.cc_ae2_support.ae2.NetworkStatus;
import com.wsyong11.cc_ae2_support.block.AENetworkBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class AENetworkBlockEntity extends BaseBlockEntity implements IInWorldGridNodeHost, IAENetworkObject {
	private static final IGridNodeListener<AENetworkBlockEntity> GRID_NODE_LISTENER = new IGridNodeListener<>() {
		@Override
		public void onSaveChanges(@Nonnull AENetworkBlockEntity nodeOwner, @Nonnull IGridNode node) {
			nodeOwner.onNodeSaveChanges();
		}

		@Override
		public void onStateChanged(AENetworkBlockEntity nodeOwner, IGridNode node, State state) {
			nodeOwner.onNodeStateChanged(state);
		}
	};

	private final IManagedGridNode mainNode;

	public AENetworkBlockEntity(@Nonnull BlockEntityType<?> type, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		super(type, pos, state);
		this.mainNode = GridHelper.createManagedNode(this, GRID_NODE_LISTENER);
		this.setupMainNode(this.mainNode);
	}

	protected void setupMainNode(@Nonnull IManagedGridNode node) {
		node.setInWorldNode(true)
		    .setTagName("proxy")
		    .setFlags(GridFlags.REQUIRE_CHANNEL)
		    .setExposedOnSides(EnumSet.allOf(Direction.class))
		    .setIdlePowerUsage(4.0D);
	}

	// -------------------------------------------------------------------------------------------------------------- //

	protected void onNodeStateChanged(@Nonnull IGridNodeListener.State state) {
		if (state == IGridNodeListener.State.POWER || state == IGridNodeListener.State.CHANNEL) {
			Level level = this.getLevel();
			if (level == null) return;

			BlockState blockState = this.getBlockState();
			level.setBlockAndUpdate(this.getBlockPos(), blockState
				.setValue(AENetworkBlock.STATUS, NetworkStatus.withNode(this)));
		}
	}

	protected void onNodeSaveChanges() {
		this.setChanged();
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Nonnull
	public IManagedGridNode getMainNode() {
		return this.mainNode;
	}

	@Nullable
	@Override
	public IGridNode getGridNode() {
		return this.getMainNode().getNode();
	}

	@Nullable
	@Override
	public IGridNode getGridNode(@Nonnull Direction dir) {
		return this.getGridNode();
	}

	@Override
	public AECableType getCableConnectionType(@Nonnull Direction dir) {
		return AECableType.SMART;
	}

	@Override
	public boolean isPowered() {
		return this.getMainNode().isPowered();
	}

	@Override
	public boolean isMissingChannel() {
		IGridNode node = this.getGridNode();
		return node == null || !node.meetsChannelRequirements();
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	public void onPlaceByPlayer(@Nonnull Player player) {
		this.getMainNode().setOwningPlayer(player);
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	protected void saveAdditional(@Nonnull CompoundTag tag) {
		super.saveAdditional(tag);
		this.getMainNode().saveToNBT(tag);
	}

	@Override
	protected void loadAdditional(@Nonnull CompoundTag tag) {
		super.loadAdditional(tag);
		this.getMainNode().loadFromNBT(tag);
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	public void onChunkUnloaded() {
		super.onChunkUnloaded();
		this.getMainNode().destroy();
	}

	// -------------------------------------------------------------------------------------------------------------- //

	@Override
	protected void onReady() {
		super.onReady();
		this.getMainNode().create(this.getLevel(), this.getBlockPos());
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		this.getMainNode().destroy();
	}
}
