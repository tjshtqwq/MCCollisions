package com.hezhong.mccollisions.datatypes;

import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.hezhong.mccollisions.util.GrimMath;

import java.util.HashSet;

public class OffsetCollisionBox extends SimpleCollisionBox {

    private static final HashSet<StateType> XZ_OFFSET_BLOCKSTATES = new HashSet<>();
    private static final HashSet<StateType> XYZ_OFFSET_BLOCKSTATES = new HashSet<>();

    static {
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.MANGROVE_PROPAGULE);

        XZ_OFFSET_BLOCKSTATES.addAll(BlockTags.SMALL_FLOWERS.getStates());
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.BAMBOO_SAPLING);
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.BAMBOO);
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.POINTED_DRIPSTONE);
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.SULFUR_SPIKE);
    }

    private float maxHorizontalModelOffset = 0.25F;
    private float maxVerticalModelOffset = 0.2F;
    private double offsetX = 0;
    private double offsetY = 0;
    private double offsetZ = 0;
    private final OffsetType offsetType;

    public OffsetCollisionBox(StateType block, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        if (block.equals(StateTypes.POINTED_DRIPSTONE) || block.equals(StateTypes.SULFUR_SPIKE)) {
            maxHorizontalModelOffset = 0.125F;
        }

        if (XZ_OFFSET_BLOCKSTATES.contains(block)) {
            offsetType = OffsetType.XZ;
            return;
        } else if (XYZ_OFFSET_BLOCKSTATES.contains(block)) {
            offsetType = OffsetType.XYZ;
            return;
        }
        throw new RuntimeException("Invalid State Type for OffSetCollisionBox: " + block);
    }

    @Override
    public SimpleCollisionBox offset(double x, double y, double z) {
        // In case you want to call .offset() again or get the box values without offset.
        resetBlockStateOffSet();
        return switch (offsetType) {
            case NONE -> super.offset(x, y, z);
            case XZ -> {
                long l = GrimMath.hashCode(x, 0, z);
                offsetX = GrimMath.clamp(((double) ((float) (l & 15L) / 15.0F) - 0.5) * 0.5, -maxHorizontalModelOffset, maxHorizontalModelOffset);
                offsetZ = GrimMath.clamp(((double) ((float) (l >> 8 & 15L) / 15.0F) - 0.5) * 0.5, -maxHorizontalModelOffset, maxHorizontalModelOffset);
                yield super.offset(x + offsetX, y, z + offsetZ);
            }
            case XYZ -> {
                long l = GrimMath.hashCode(x, 0, z);
                offsetY = ((double) ((float) (l >> 4 & 15L) / 15.0F) - 1.0) * (double) maxVerticalModelOffset;
                offsetX = GrimMath.clamp(((double) ((float) (l & 15L) / 15.0F) - 0.5) * 0.5, -maxHorizontalModelOffset, maxHorizontalModelOffset);
                offsetZ = GrimMath.clamp(((double) ((float) (l >> 8 & 15L) / 15.0F) - 0.5) * 0.5, -maxHorizontalModelOffset, maxHorizontalModelOffset);
                yield super.offset(x + offsetX, offsetY, z + offsetZ);
            }
        };
    }

    public void resetBlockStateOffSet() {
        this.minX += offsetX;
        this.minY += offsetY;
        this.minZ += offsetZ;
        this.maxX += offsetX;
        this.maxY += offsetY;
        this.maxZ += offsetZ;
    }

    public enum OffsetType {
        NONE,
        XZ,
        XYZ,
    }
}
