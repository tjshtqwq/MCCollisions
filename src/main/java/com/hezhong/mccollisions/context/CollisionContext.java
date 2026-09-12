package com.hezhong.mccollisions.context;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.hezhong.mccollisions.world.WorldBorder;

/**
 * Provides everything the collision tables need, without depending on any anticheat/plugin class.
 * Only {@link #clientVersion()} and {@link #world()} are required; all player flags are optional
 * and default to a neutral state.
 */
public interface CollisionContext {

    ClientVersion clientVersion();

    WorldView world();

    /**
     * Defaults to the running PacketEvents server version. Override if PacketEvents is not initialized yet.
     */
    default ServerVersion serverVersion() {
        return PacketEvents.getAPI().getServerManager().getVersion();
    }

    default WrappedBlockState getBlock(int x, int y, int z) {
        WrappedBlockState state = world().getBlock(x, y, z);
        return state == null ? StateTypes.AIR.createBlockState() : state;
    }

    default StateType getBlockType(int x, int y, int z) {
        return getBlock(x, y, z).getType();
    }

    default double lastY() {
        return 0;
    }

    default double y() {
        return 0;
    }

    default boolean isSneaking() {
        return false;
    }

    default double fallDistance() {
        return 0;
    }

    default boolean hasLeatherBoots() {
        return false;
    }

    default boolean inVehicle() {
        return false;
    }

    default boolean isRidingBoat() {
        return false;
    }

    default boolean isRidingStrider() {
        return false;
    }

    default boolean isAboveLava() {
        return false;
    }

    default boolean snowCollisionFix() {
        return false;
    }

    /**
     * Maximum height the entity can step up. Vanilla player default is 0.6.
     */
    default double maxUpStep() {
        return 0.6;
    }

    /**
     * Lowest block Y that can be iterated when gathering collisions.
     */
    default int minHeight() {
        return 0;
    }

    /**
     * Highest block Y (exclusive) that can be iterated when gathering collisions.
     */
    default int maxHeight() {
        return 256;
    }

    /**
     * Optional world border, or null if there is none.
     */
    default WorldBorder worldBorder() {
        return null;
    }
}
