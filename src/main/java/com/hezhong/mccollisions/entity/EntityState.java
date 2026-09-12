package com.hezhong.mccollisions.entity;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

/**
 * Lightweight, standalone replacement for Grim's PacketEntity when looking up entity hitbox sizes.
 */
public final class EntityState {

    private final ClientVersion clientVersion;
    private final EntityType type;
    private final boolean baby;
    private final int size;
    private final boolean elderGuardian;
    private final Pose pose;
    private final Pose transitionalPose;
    private final double scale;

    public EntityState(ClientVersion clientVersion, EntityType type) {
        this(clientVersion, type, false, 1, false, Pose.STANDING, null, 1.0);
    }

    public EntityState(ClientVersion clientVersion, EntityType type, boolean baby, int size,
                       boolean elderGuardian, Pose pose, Pose transitionalPose, double scale) {
        this.clientVersion = clientVersion;
        this.type = type;
        this.baby = baby;
        this.size = size;
        this.elderGuardian = elderGuardian;
        this.pose = pose == null ? Pose.STANDING : pose;
        this.transitionalPose = transitionalPose;
        this.scale = scale;
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public EntityType getType() {
        return type;
    }

    public boolean isBaby() {
        return baby;
    }

    public int getSize() {
        return size;
    }

    public boolean isElderGuardian() {
        return elderGuardian;
    }

    public Pose getPose() {
        return pose;
    }

    public Pose getTransitionalPose() {
        return transitionalPose;
    }

    public double getScale() {
        return scale;
    }

    public static Builder builder(ClientVersion clientVersion, EntityType type) {
        return new Builder(clientVersion, type);
    }

    public static final class Builder {
        private final ClientVersion clientVersion;
        private final EntityType type;
        private boolean baby;
        private int size = 1;
        private boolean elderGuardian;
        private Pose pose = Pose.STANDING;
        private Pose transitionalPose;
        private double scale = 1.0;

        private Builder(ClientVersion clientVersion, EntityType type) {
            this.clientVersion = clientVersion;
            this.type = type;
        }

        public Builder baby(boolean baby) {
            this.baby = baby;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder elderGuardian(boolean elderGuardian) {
            this.elderGuardian = elderGuardian;
            return this;
        }

        public Builder pose(Pose pose) {
            this.pose = pose;
            return this;
        }

        public Builder transitionalPose(Pose transitionalPose) {
            this.transitionalPose = transitionalPose;
            return this;
        }

        public Builder scale(double scale) {
            this.scale = scale;
            return this;
        }

        public EntityState build() {
            return new EntityState(clientVersion, type, baby, size, elderGuardian, pose, transitionalPose, scale);
        }
    }
}
