package com.hezhong.mccollisions.context;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

@FunctionalInterface
public interface WorldView {
    WrappedBlockState getBlock(int x, int y, int z);
}
