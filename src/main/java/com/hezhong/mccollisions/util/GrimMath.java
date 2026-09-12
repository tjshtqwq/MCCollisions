package com.hezhong.mccollisions.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;

@UtilityClass
public class GrimMath {

    @Contract(pure = true)
    public static int floor(double d) {
        return (int) Math.floor(d);
    }

    @Contract(pure = true)
    public static int ceil(double d) {
        return (int) Math.ceil(d);
    }

    @Contract(pure = true)
    public static double clamp(double num, double min, double max) {
        if (num < min) {
            return min;
        }
        return Math.min(num, max);
    }

    @Contract(pure = true)
    public static float clamp(float num, float min, float max) {
        if (num < min) {
            return min;
        }
        return Math.min(num, max);
    }

    @Contract(pure = true)
    public static double lerp(double lerpAmount, double start, double end) {
        return start + lerpAmount * (end - start);
    }

    @Contract(pure = true)
    public static long hashCode(double x, int y, double z) {
        long l = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
        l = l * l * 42317861L + l * 11L;
        return l >> 16;
    }

    private static final float DEGREES_TO_RADIANS = (float) Math.PI / 180f;

    @Contract(pure = true)
    public static float radians(float degrees) {
        return degrees * DEGREES_TO_RADIANS;
    }
}
