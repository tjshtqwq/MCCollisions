package com.hezhong.mccollisions.world;

/**
 * Optional world border supplied by {@link com.hezhong.mccollisions.context.CollisionContext}.
 */
public interface WorldBorder {
    double getMinX();

    double getMinZ();

    double getMaxX();

    double getMaxZ();
}
