package com.hezhong.mccollisions.util;

import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

@UtilityClass
public class Materials {
    // Includes iron panes in addition to glass panes
    private static final Set<StateType> PANES = Collections.newSetFromMap(new IdentityHashMap<>());

    public static final Set<StateType> CHESTS = Collections.newSetFromMap(new IdentityHashMap<>());
    public static final Set<StateType> RODS = Collections.newSetFromMap(new IdentityHashMap<>());
    public static final Set<StateType> CHAINS = Collections.newSetFromMap(new IdentityHashMap<>());

    static {
        PANES.addAll(BlockTags.GLASS_PANES.getStates());
        PANES.addAll(BlockTags.BARS.getStates());
        PANES.add(StateTypes.IRON_BARS);

        CHESTS.addAll(BlockTags.COPPER_CHESTS.getStates());
        CHESTS.add(StateTypes.TRAPPED_CHEST);
        CHESTS.add(StateTypes.CHEST);

        RODS.addAll(BlockTags.LIGHTNING_RODS.getStates());
        RODS.add(StateTypes.END_ROD);
        RODS.add(StateTypes.LIGHTNING_ROD);

        CHAINS.addAll(BlockTags.CHAINS.getStates());
        CHAINS.add(StateTypes.CHAIN);
    }

    public static boolean isStairs(StateType type) {
        return BlockTags.STAIRS.contains(type);
    }

    public static boolean isWall(StateType type) {
        return BlockTags.WALLS.contains(type);
    }

    public static Set<StateType> getPanes() {
        return new HashSet<>(PANES);
    }

    public static Set<StateType> getChests() {
        return new HashSet<>(CHESTS);
    }

    public static Set<StateType> getRods() {
        return new HashSet<>(RODS);
    }

    public static Set<StateType> getChains() {
        return new HashSet<>(CHAINS);
    }

    public static boolean isGlassPane(StateType type) {
        return PANES.contains(type);
    }
}
