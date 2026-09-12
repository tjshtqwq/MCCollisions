package com.hezhong.mccollisions.world;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3d;
import com.hezhong.mccollisions.CollisionData;
import com.hezhong.mccollisions.context.CollisionContext;
import com.hezhong.mccollisions.datatypes.CollisionBox;
import com.hezhong.mccollisions.datatypes.SimpleCollisionBox;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Standalone port of the vanilla Minecraft collision logic:
 * {@code World.getCollidingBoundingBoxes} (getCubes) and {@code Entity.moveEntity} (MoveEntity).
 * <p>
 * This is plain vanilla behaviour only. No optimizations or fixes from any anticheat are applied.
 */
@UtilityClass
public final class Collisions {
    public static final double COLLISION_EPSILON = SimpleCollisionBox.COLLISION_EPSILON;

    /**
     * Vanilla resolves movement on the Y axis first, then X, then Z (see {@code Entity.moveEntity}).
     */
    public static final List<Axis> VANILLA_AXIS_ORDER = List.of(Axis.Y, Axis.X, Axis.Z);

    /**
     * Vanilla {@code getCollidingBoundingBoxes} / {@code getCubes}: collect every block collision box
     * that intersects {@code wantedBB}.
     */
    public static List<SimpleCollisionBox> getCollisionBoxes(CollisionContext context, SimpleCollisionBox wantedBB) {
        List<SimpleCollisionBox> boxes = new ArrayList<>();
        getCollisionBoxes(context, wantedBB, boxes, false);
        return boxes;
    }

    public static boolean isEmpty(CollisionContext context, SimpleCollisionBox box) {
        return !getCollisionBoxes(context, box, null, true);
    }

    public static boolean getCollisionBoxes(CollisionContext context, SimpleCollisionBox wantedBB, List<SimpleCollisionBox> listOfBlocks, boolean onlyCheckCollide) {
        SimpleCollisionBox expandedBB = wantedBB.copy();

        boolean collided = addWorldBorder(context, wantedBB, listOfBlocks, onlyCheckCollide);
        if (onlyCheckCollide && collided) return true;

        int minBlockX = (int) Math.floor(expandedBB.minX - COLLISION_EPSILON) - 1;
        int maxBlockX = (int) Math.floor(expandedBB.maxX + COLLISION_EPSILON) + 1;
        int minBlockY = (int) Math.floor(expandedBB.minY - COLLISION_EPSILON) - 1;
        int maxBlockY = (int) Math.floor(expandedBB.maxY + COLLISION_EPSILON) + 1;
        int minBlockZ = (int) Math.floor(expandedBB.minZ - COLLISION_EPSILON) - 1;
        int maxBlockZ = (int) Math.floor(expandedBB.maxZ + COLLISION_EPSILON) + 1;

        final int minBlock = context.minHeight();
        final int maxBlock = context.maxHeight() - 1;

        int minYIterate = Math.max(minBlock, minBlockY);
        int maxYIterate = Math.min(maxBlock, maxBlockY);

        for (int y = minYIterate; y <= maxYIterate; ++y) {
            for (int z = minBlockZ; z <= maxBlockZ; ++z) {
                for (int x = minBlockX; x <= maxBlockX; ++x) {
                    WrappedBlockState data = context.getBlock(x, y, z);

                    // Air has global id 0, which is the common case
                    if (data == null || data.getGlobalId() == 0) continue;

                    final CollisionBox collisionBox = CollisionData.getData(data.getType()).getMovementCollisionBox(context, context.clientVersion(), data, x, y, z);
                    // Don't add to a list if we only care if the entity intersects with the block
                    if (!onlyCheckCollide) {
                        collisionBox.downCast(listOfBlocks);
                    } else if (collisionBox.isCollided(wantedBB)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Iterate every collision box intersecting {@code checkBox} and feed it to {@code searchingFor}.
     */
    public static void forEachCollisionBox(CollisionContext context, SimpleCollisionBox checkBox, BlockAndPositionConsumer searchingFor) {
        SimpleCollisionBox expandedBB = checkBox.copy();

        int minBlockX = (int) Math.floor(expandedBB.minX - COLLISION_EPSILON) - 1;
        int maxBlockX = (int) Math.floor(expandedBB.maxX + COLLISION_EPSILON) + 1;
        int minBlockY = (int) Math.floor(expandedBB.minY - COLLISION_EPSILON) - 1;
        int maxBlockY = (int) Math.floor(expandedBB.maxY + COLLISION_EPSILON) + 1;
        int minBlockZ = (int) Math.floor(expandedBB.minZ - COLLISION_EPSILON) - 1;
        int maxBlockZ = (int) Math.floor(expandedBB.maxZ + COLLISION_EPSILON) + 1;

        final int minBlock = context.minHeight();
        final int maxBlock = context.maxHeight() - 1;

        int minYIterate = Math.max(minBlock, minBlockY);
        int maxYIterate = Math.min(maxBlock, maxBlockY);

        for (int y = minYIterate; y <= maxYIterate; ++y) {
            for (int z = minBlockZ; z <= maxBlockZ; ++z) {
                for (int x = minBlockX; x <= maxBlockX; ++x) {
                    WrappedBlockState data = context.getBlock(x, y, z);

                    if (data == null || data.getGlobalId() == 0) continue;

                    final CollisionBox collisionBox = CollisionData.getData(data.getType()).getMovementCollisionBox(context, context.clientVersion(), data, x, y, z);

                    if (collisionBox.isIntersected(checkBox)) {
                        searchingFor.accept(data, x, y, z);
                    }
                }
            }
        }
    }

    /**
     * Adds the optional world border collision boxes (four thin boxes) when the searched box is near it.
     */
    public static boolean addWorldBorder(CollisionContext context, SimpleCollisionBox wantedBB, List<SimpleCollisionBox> listOfBlocks, boolean onlyCheckCollide) {
        WorldBorder border = context.worldBorder();
        if (border == null) return false;

        double minX = Math.floor(border.getMinX());
        double minZ = Math.floor(border.getMinZ());
        double maxX = Math.ceil(border.getMaxX());
        double maxZ = Math.ceil(border.getMaxZ());

        double centerX = (wantedBB.minX + wantedBB.maxX) / 2.0;
        double centerZ = (wantedBB.minZ + wantedBB.maxZ) / 2.0;

        // If the box is fully within the worldborder
        double toMinX = centerX - minX;
        double toMaxX = maxX - centerX;
        double minimumInXDirection = Math.min(toMinX, toMaxX);

        double toMinZ = centerZ - minZ;
        double toMaxZ = maxZ - centerZ;
        double minimumInZDirection = Math.min(toMinZ, toMaxZ);

        double distanceToBorder = Math.min(minimumInXDirection, minimumInZDirection);

        // If the box is within 16 blocks of the worldborder, add the worldborder to the collisions (optimization)
        if (distanceToBorder < 16 && centerX > minX && centerX < maxX && centerZ > minZ && centerZ < maxZ) {
            if (listOfBlocks == null) listOfBlocks = new ArrayList<>();

            // South border
            listOfBlocks.add(new SimpleCollisionBox(minX - 10, Double.NEGATIVE_INFINITY, maxZ, maxX + 10, Double.POSITIVE_INFINITY, maxZ, false));
            // North border
            listOfBlocks.add(new SimpleCollisionBox(minX - 10, Double.NEGATIVE_INFINITY, minZ, maxX + 10, Double.POSITIVE_INFINITY, minZ, false));
            // East border
            listOfBlocks.add(new SimpleCollisionBox(maxX, Double.NEGATIVE_INFINITY, minZ - 10, maxX, Double.POSITIVE_INFINITY, maxZ + 10, false));
            // West border
            listOfBlocks.add(new SimpleCollisionBox(minX, Double.NEGATIVE_INFINITY, minZ - 10, minX, Double.POSITIVE_INFINITY, maxZ + 10, false));

            if (onlyCheckCollide) {
                for (SimpleCollisionBox box : listOfBlocks) {
                    if (box.isIntersected(wantedBB)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Vanilla {@code Entity.collideBoundingBox}: resolves the movement axis by axis against the boxes.
     */
    public static Vector3d collideBoundingBox(Vector3d toCollide, SimpleCollisionBox box, List<SimpleCollisionBox> desiredMovementCollisionBoxes, List<Axis> order) {
        double x = toCollide.getX();
        double y = toCollide.getY();
        double z = toCollide.getZ();

        SimpleCollisionBox setBB = box.copy();

        for (Axis axis : order) {
            if (axis == Axis.X) {
                for (SimpleCollisionBox bb : desiredMovementCollisionBoxes) {
                    x = bb.collideX(setBB, x);
                }
                setBB = setBB.offset(x, 0.0D, 0.0D).copy();
            } else if (axis == Axis.Y) {
                for (SimpleCollisionBox bb : desiredMovementCollisionBoxes) {
                    y = bb.collideY(setBB, y);
                }
                setBB = setBB.offset(0.0D, y, 0.0D).copy();
            } else if (axis == Axis.Z) {
                for (SimpleCollisionBox bb : desiredMovementCollisionBoxes) {
                    z = bb.collideZ(setBB, z);
                }
                setBB = setBB.offset(0.0D, 0.0D, z).copy();
            }
        }

        return new Vector3d(x, y, z);
    }

    public static Vector3d collideBoundingBox(CollisionContext context, Vector3d movement, SimpleCollisionBox box) {
        return collideBoundingBox(context, movement, box, VANILLA_AXIS_ORDER);
    }

    public static Vector3d collideBoundingBox(CollisionContext context, Vector3d movement, SimpleCollisionBox box, List<Axis> order) {
        SimpleCollisionBox grabBoxesBB = box.copy();
        grabBoxesBB.expandToCoordinate(movement.getX(), movement.getY(), movement.getZ());

        List<SimpleCollisionBox> boxes = new ArrayList<>();
        getCollisionBoxes(context, grabBoxesBB, boxes, false);

        return collideBoundingBox(movement, box, boxes, order);
    }

    /**
     * Vanilla {@code Entity.moveEntity} (the "MoveEntity" computation): resolve the movement against
     * the surrounding collision boxes, including vanilla step-up.
     *
     * @param entityBox the current bounding box of the entity
     * @param movement  the desired movement (velocity for this tick)
     * @param onGround  whether the entity is on the ground
     * @return the actual movement after collisions
     */
    public static Vector3d collide(CollisionContext context, SimpleCollisionBox entityBox, Vector3d movement, boolean onGround) {
        final double desiredX = movement.getX();
        final double desiredY = movement.getY();
        final double desiredZ = movement.getZ();

        if (desiredX == 0 && desiredY == 0 && desiredZ == 0) return Vector3d.zero();

        final double stepHeight = context.maxUpStep();

        final SimpleCollisionBox bb = entityBox.copy();

        final List<SimpleCollisionBox> collidingBoxes = new ArrayList<>();
        getCollisionBoxes(context, bb.copy().expandToCoordinate(desiredX, desiredY, desiredZ), collidingBoxes, false);

        double x = desiredX;
        double y = desiredY;
        double z = desiredZ;

        SimpleCollisionBox entityBB = bb.copy();

        for (SimpleCollisionBox box : collidingBoxes) {
            y = box.collideY(entityBB, y);
        }
        entityBB = entityBB.copy().offset(0.0D, y, 0.0D);

        for (SimpleCollisionBox box : collidingBoxes) {
            x = box.collideX(entityBB, x);
        }
        entityBB = entityBB.copy().offset(x, 0.0D, 0.0D);

        for (SimpleCollisionBox box : collidingBoxes) {
            z = box.collideZ(entityBB, z);
        }
        entityBB = entityBB.copy().offset(0.0D, 0.0D, z);

        final boolean flag1 = onGround || (desiredY != y && desiredY < 0.0D);

        if (stepHeight > 0.0F && flag1 && (desiredX != x || desiredZ != z)) {
            final double d11 = x;
            final double d7 = y;
            final double d8 = z;

            final SimpleCollisionBox axisalignedbb3 = entityBB;
            entityBB = bb.copy();

            final double stepY = stepHeight;

            final List<SimpleCollisionBox> list = new ArrayList<>();
            getCollisionBoxes(context, bb.copy().expandToCoordinate(desiredX, stepY, desiredZ), list, false);

            SimpleCollisionBox aabb4 = entityBB.copy();
            final SimpleCollisionBox aabb5 = aabb4.copy().expandToCoordinate(desiredX, 0.0D, desiredZ);

            double d9 = stepY;
            for (SimpleCollisionBox box : list) {
                d9 = box.collideY(aabb5, d9);
            }
            aabb4 = aabb4.copy().offset(0.0D, d9, 0.0D);

            double d15 = desiredX;
            for (SimpleCollisionBox box : list) {
                d15 = box.collideX(aabb4, d15);
            }
            aabb4 = aabb4.copy().offset(d15, 0.0D, 0.0D);

            double d16 = desiredZ;
            for (SimpleCollisionBox box : list) {
                d16 = box.collideZ(aabb4, d16);
            }
            aabb4 = aabb4.copy().offset(0.0D, 0.0D, d16);

            SimpleCollisionBox aabb14 = entityBB.copy();

            double d17 = stepY;
            for (SimpleCollisionBox box : list) {
                d17 = box.collideY(aabb14, d17);
            }
            aabb14 = aabb14.copy().offset(0.0D, d17, 0.0D);

            double d18 = desiredX;
            for (SimpleCollisionBox box : list) {
                d18 = box.collideX(aabb14, d18);
            }
            aabb14 = aabb14.copy().offset(d18, 0.0D, 0.0D);

            double d19 = desiredZ;
            for (SimpleCollisionBox box : list) {
                d19 = box.collideZ(aabb14, d19);
            }
            aabb14 = aabb14.copy().offset(0.0D, 0.0D, d19);

            final double d20 = d15 * d15 + d16 * d16;
            final double d10 = d18 * d18 + d19 * d19;

            if (d20 > d10) {
                x = d15;
                z = d16;
                y = -d9;
                entityBB = aabb4;
            } else {
                x = d18;
                z = d19;
                y = -d17;
                entityBB = aabb14;
            }

            for (SimpleCollisionBox box : list) {
                y = box.collideY(entityBB, y);
            }
            entityBB = entityBB.copy().offset(0.0D, y, 0.0D);

            if (d11 * d11 + d8 * d8 >= x * x + z * z) {
                x = d11;
                y = d7;
                z = d8;
                entityBB = axisalignedbb3;
            }
        }

        return new Vector3d(x, y, z);
    }

    /**
     * Alias for {@link #collide(CollisionContext, SimpleCollisionBox, Vector3d, boolean)}.
     */
    public static Vector3d move(CollisionContext context, SimpleCollisionBox entityBox, Vector3d movement, boolean onGround) {
        return collide(context, entityBox, movement, onGround);
    }
}
