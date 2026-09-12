package com.hezhong.mccollisions.world;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.hezhong.mccollisions.context.CollisionContext;
import com.hezhong.mccollisions.util.GrimMath;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BlockProperties {

    /**
     * The block the entity is standing on, taking the main supporting block into account on 1.19.4+.
     */
    public static StateType getOnPos(CollisionContext context, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        if (context.clientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
            return getOnBlock(context, playerPos.getX(), playerPos.getY(), playerPos.getZ());
        }

        Vector3i pos = getOnPos(context, playerPos, mainSupportingBlockData, 0.2F);
        return context.getBlockType(pos.getX(), pos.getY(), pos.getZ());
    }

    public static StateType getBlockPosBelowThatAffectsMyMovement(CollisionContext context, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        Vector3i pos = context.clientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)
                ? new Vector3i(GrimMath.floor(playerPos.getX()), GrimMath.floor(playerPos.getY() - 0.5000001), GrimMath.floor(playerPos.getZ()))
                : getOnPos(context, playerPos, mainSupportingBlockData, 0.500001F);
        return context.getBlockType(pos.getX(), pos.getY(), pos.getZ());
    }

    private static Vector3i getOnPos(CollisionContext context, Vector3d playerPos, MainSupportingBlockData mainSupportingBlockData, float searchBelowPlayer) {
        Vector3i mainBlockPos = mainSupportingBlockData.blockPos();
        if (mainBlockPos != null) {
            StateType blockstate = context.getBlockType(mainBlockPos.getX(), mainBlockPos.getY(), mainBlockPos.getZ());

            // I genuinely don't understand this code, or why fences are special
            boolean shouldReturn = (!((double) searchBelowPlayer <= 0.5D) || !BlockTags.FENCES.contains(blockstate)) &&
                    !BlockTags.WALLS.contains(blockstate) &&
                    !BlockTags.FENCE_GATES.contains(blockstate);

            return shouldReturn
                    ? new Vector3i(mainBlockPos.getX(), GrimMath.floor(playerPos.getY() - (double) searchBelowPlayer), mainBlockPos.getZ())
                    : mainBlockPos;
        } else {
            return new Vector3i(GrimMath.floor(playerPos.getX()), GrimMath.floor(playerPos.getY() - searchBelowPlayer), GrimMath.floor(playerPos.getZ()));
        }
    }

    private static StateType getOnBlock(CollisionContext context, double x, double y, double z) {
        StateType block1 = context.getBlockType(GrimMath.floor(x), GrimMath.floor(y - 0.2F), GrimMath.floor(z));

        if (block1.isAir()) {
            StateType block2 = context.getBlockType(GrimMath.floor(x), GrimMath.floor(y - 1.2F), GrimMath.floor(z));

            if (BlockTags.FENCES.contains(block2) || BlockTags.WALLS.contains(block2) || BlockTags.FENCE_GATES.contains(block2)) {
                return block2;
            }
        }

        return block1;
    }
}
