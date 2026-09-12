package com.hezhong.mccollisions.world;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.hezhong.mccollisions.context.CollisionContext;
import com.hezhong.mccollisions.datatypes.SimpleCollisionBox;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class SupportingBlockFinder {

    public static @NotNull MainSupportingBlockData findMainSupportingBlockPos(CollisionContext context, Vector3d entityPos,
                                                                             MainSupportingBlockData lastSupportingBlock,
                                                                             Vector3d lastMovement, SimpleCollisionBox maxPose,
                                                                             boolean isOnGround) {
        if (!isOnGround) {
            return MainSupportingBlockData.AIR_OFF_GROUND;
        }

        SimpleCollisionBox slightlyBelowPlayer = new SimpleCollisionBox(maxPose.minX, maxPose.minY - 1.0E-6D, maxPose.minZ, maxPose.maxX, maxPose.minY, maxPose.maxZ);

        Vector3i supportingBlock = findSupportingBlock(context, entityPos, slightlyBelowPlayer);
        if (supportingBlock == null && !lastSupportingBlock.lastOnGroundAndNoBlock()) {
            if (lastMovement != null) {
                SimpleCollisionBox aabb2 = slightlyBelowPlayer.offset(-lastMovement.getX(), 0.0D, -lastMovement.getZ());
                return new MainSupportingBlockData(findSupportingBlock(context, entityPos, aabb2), true);
            }
        } else {
            return new MainSupportingBlockData(supportingBlock, true);
        }

        return MainSupportingBlockData.AIR_ON_GROUND;
    }

    private static @Nullable Vector3i findSupportingBlock(CollisionContext context, Vector3d playerPos, SimpleCollisionBox searchBox) {
        Vector3i[] bestBlockPos = new Vector3i[1];
        double[] blockPosDistance = {Double.MAX_VALUE};

        Collisions.forEachCollisionBox(context, searchBox, (block, x, y, z) -> {
            Vector3d center = new Vector3d(x + 0.5, y + 0.5, z + 0.5);
            double distance = playerPos.distanceSquared(center);

            if (distance < blockPosDistance[0] || distance == blockPosDistance[0] && (bestBlockPos[0] == null || firstHasPriorityOverSecond(x, y, z, bestBlockPos[0]))) {
                bestBlockPos[0] = new Vector3i(x, y, z);
                blockPosDistance[0] = distance;
            }
        });

        return bestBlockPos[0];
    }

    private static boolean firstHasPriorityOverSecond(int firstX, int firstY, int firstZ, @NotNull Vector3i second) {
        // Order of loop is X, Y, and Z
        // We prioritize lowest Y axis, then lowest X axis, then lowest Z axis
        if (firstY < second.getY()) return true;

        double sumX = second.getX() - firstX;
        double sumY = second.getZ() - firstZ;

        double horizontalSumTotal = sumX + sumY;
        if (horizontalSumTotal == 0) {
            // If X is farther in the X direction, then it was found later and therefore won't override
            return sumX < 0;
        }

        // Otherwise, lower X and lower Z have priority
        return horizontalSumTotal < 0;
    }
}
