package com.hezhong.mccollisions.world;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

@FunctionalInterface
public interface BlockAndPositionConsumer {
    void accept(WrappedBlockState block, int x, int y, int z);
}
