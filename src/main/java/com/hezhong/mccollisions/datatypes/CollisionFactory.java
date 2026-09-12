package com.hezhong.mccollisions.datatypes;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.hezhong.mccollisions.context.CollisionContext;

public interface CollisionFactory {
    CollisionBox fetch(CollisionContext context, ClientVersion version, WrappedBlockState block, int x, int y, int z);
}
