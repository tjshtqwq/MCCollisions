package com.hezhong.mccollisions.datatypes;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.hezhong.mccollisions.context.CollisionContext;

public interface HitBoxFactory {
    CollisionBox fetch(CollisionContext context, StateType heldItem, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z);
}
