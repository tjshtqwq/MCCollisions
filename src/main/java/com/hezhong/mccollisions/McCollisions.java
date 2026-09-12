package com.hezhong.mccollisions;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.hezhong.mccollisions.context.CollisionContext;
import com.hezhong.mccollisions.datatypes.CollisionBox;
import com.hezhong.mccollisions.datatypes.SimpleCollisionBox;
import com.hezhong.mccollisions.entity.EntityData;
import com.hezhong.mccollisions.entity.EntityState;
import com.hezhong.mccollisions.world.Axis;
import com.hezhong.mccollisions.world.BlockProperties;
import com.hezhong.mccollisions.world.Collisions;
import com.hezhong.mccollisions.world.MainSupportingBlockData;
import com.hezhong.mccollisions.world.SupportingBlockFinder;

import java.util.List;

/**
 * Unified entry point: pass a block + block state (or entity + entity state) and get its collision box.
 */
public final class McCollisions {

    private McCollisions() {
    }

    /**
     * Block movement collision box (from {@link CollisionData}). Includes complex boxes.
     */
    public static CollisionBox getBlockCollisionBox(CollisionContext context, WrappedBlockState state, int x, int y, int z) {
        return CollisionData.getData(state.getType()).getMovementCollisionBox(context, context.clientVersion(), state, x, y, z);
    }

    public static CollisionBox getBlockCollisionBox(CollisionContext context, WrappedBlockState state) {
        return getBlockCollisionBox(context, state, 0, 0, 0);
    }

    /**
     * Block interaction / ray-trace hitbox (from {@link HitboxData}). Includes complex boxes.
     *
     * @param heldItem     the block type held by the player, used by a few shapes (may be null)
     * @param isTargetBlock whether the block is the current target of the player
     */
    public static CollisionBox getBlockHitBox(CollisionContext context, WrappedBlockState state, StateType heldItem,
                                              boolean isTargetBlock, int x, int y, int z) {
        return HitboxData.getBlockHitbox(context, heldItem, context.clientVersion(), state, isTargetBlock, x, y, z);
    }

    public static CollisionBox getBlockHitBox(CollisionContext context, WrappedBlockState state) {
        return getBlockHitBox(context, state, null, false, 0, 0, 0);
    }

    /**
     * Entity hitbox, with the entity's feet at {@code minY} and centered on {@code centerX}/{@code centerZ}.
     */
    public static SimpleCollisionBox getEntityBox(EntityState entity, double centerX, double minY, double centerZ) {
        double[] dimensions = EntityData.getEntityDimensions(entity);
        double halfWidth = dimensions[0] / 2.0;
        double height = dimensions[1];
        double halfDepth = dimensions[2] / 2.0;

        return new SimpleCollisionBox(
                centerX - halfWidth,
                minY,
                centerZ - halfDepth,
                centerX + halfWidth,
                minY + height,
                centerZ + halfDepth,
                false
        );
    }

    /**
     * Entity hitbox with its feet at the origin (0,0,0).
     */
    public static SimpleCollisionBox getEntityBox(EntityState entity) {
        return getEntityBox(entity, 0, 0, 0);
    }

    /**
     * @return array of {width, height, depth} for the given entity, scale included.
     */
    public static double[] getEntityDimensions(EntityState entity) {
        return EntityData.getEntityDimensions(entity);
    }

    // ---------------------------------------------------------------------
    // World collision (NMS getCubes / MoveEntity)
    // ---------------------------------------------------------------------

    /**
     * NMS {@code getCubes} / {@code getBlockCollisions}: every block collision box intersecting the
     * given box. Complex boxes are split into simple boxes.
     */
    public static List<SimpleCollisionBox> getCollisionBoxes(CollisionContext context, SimpleCollisionBox wantedBB) {
        return Collisions.getCollisionBoxes(context, wantedBB);
    }

    public static boolean isEmpty(CollisionContext context, SimpleCollisionBox box) {
        return Collisions.isEmpty(context, box);
    }

    /**
     * NMS {@code Entity.collide} / MoveEntity: resolve {@code movement} against the surrounding world,
     * including stepping up blocks.
     *
     * @param entityBox current bounding box of the entity
     * @param movement  desired movement (velocity for this tick)
     * @param onGround  whether the entity is on the ground
     * @return actual movement after collisions
     */
    public static Vector3d collide(CollisionContext context, SimpleCollisionBox entityBox, Vector3d movement, boolean onGround) {
        return Collisions.collide(context, entityBox, movement, onGround);
    }

    /**
     * Alias for {@link #collide(CollisionContext, SimpleCollisionBox, Vector3d, boolean)}.
     */
    public static Vector3d move(CollisionContext context, SimpleCollisionBox entityBox, Vector3d movement, boolean onGround) {
        return Collisions.move(context, entityBox, movement, onGround);
    }

    /**
     * NMS {@code Entity.collideBoundingBox}: resolve movement axis by axis against existing boxes.
     */
    public static Vector3d collideBoundingBox(CollisionContext context, Vector3d movement, SimpleCollisionBox box, List<Axis> order) {
        return Collisions.collideBoundingBox(context, movement, box, order);
    }

    /**
     * Finds the "main supporting block" the entity is standing on (1.19.4+ vanilla logic).
     */
    public static MainSupportingBlockData getSupportingBlock(CollisionContext context, Vector3d entityPos,
                                                             MainSupportingBlockData lastSupportingBlock,
                                                             Vector3d lastMovement, SimpleCollisionBox maxPose,
                                                             boolean onGround) {
        return SupportingBlockFinder.findMainSupportingBlockPos(context, entityPos, lastSupportingBlock, lastMovement, maxPose, onGround);
    }

    /**
     * The block the entity is standing on, taking the main supporting block into account.
     */
    public static StateType getOnPos(CollisionContext context, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        return BlockProperties.getOnPos(context, mainSupportingBlockData, playerPos);
    }
}
