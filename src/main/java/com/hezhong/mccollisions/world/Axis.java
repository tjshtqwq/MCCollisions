package com.hezhong.mccollisions.world;

import com.github.retrooper.packetevents.protocol.world.Direction;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;

public enum Axis {
    X {
        @Override
        public double get(Vector3d vector) {
            return vector.getX();
        }

        @Override
        public int get(Vector3i vector) {
            return vector.getX();
        }

        @Override
        public double choose(double x, double y, double z) {
            return x;
        }

        @Override
        public int choose(int x, int y, int z) {
            return x;
        }

        @Override
        public Direction getPositive() {
            return Direction.EAST;
        }

        @Override
        public Direction getNegative() {
            return Direction.WEST;
        }
    },
    Y {
        @Override
        public double get(Vector3d vector) {
            return vector.getY();
        }

        @Override
        public int get(Vector3i vector) {
            return vector.getY();
        }

        @Override
        public double choose(double x, double y, double z) {
            return y;
        }

        @Override
        public int choose(int x, int y, int z) {
            return y;
        }

        @Override
        public Direction getPositive() {
            return Direction.UP;
        }

        @Override
        public Direction getNegative() {
            return Direction.DOWN;
        }
    },
    Z {
        @Override
        public double get(Vector3d vector) {
            return vector.getZ();
        }

        @Override
        public int get(Vector3i vector) {
            return vector.getZ();
        }

        @Override
        public double choose(double x, double y, double z) {
            return z;
        }

        @Override
        public int choose(int x, int y, int z) {
            return z;
        }

        @Override
        public Direction getPositive() {
            return Direction.SOUTH;
        }

        @Override
        public Direction getNegative() {
            return Direction.NORTH;
        }
    };

    public abstract double get(Vector3d vector);

    public abstract int get(Vector3i vector);

    public abstract double choose(double x, double y, double z);

    public abstract int choose(int x, int y, int z);

    public abstract Direction getPositive();

    public abstract Direction getNegative();
}
