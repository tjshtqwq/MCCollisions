package com.hezhong.mccollisions.world;

import com.github.retrooper.packetevents.util.Vector3d;
import com.hezhong.mccollisions.datatypes.SimpleCollisionBox;
import com.hezhong.mccollisions.util.GrimMath;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class VectorUtils {

    @Contract("_, _ -> new")
    public static @NotNull Vector3d cutBoxToVector(@NotNull Vector3d vectorCutTo, @NotNull SimpleCollisionBox box) {
        return cutBoxToVector(vectorCutTo.getX(), vectorCutTo.getY(), vectorCutTo.getZ(), box);
    }

    public static @NotNull Vector3d cutBoxToVector(double x, double y, double z, @NotNull SimpleCollisionBox box) {
        return new Vector3d(GrimMath.clamp(x, box.minX, box.maxX),
                GrimMath.clamp(y, box.minY, box.maxY),
                GrimMath.clamp(z, box.minZ, box.maxZ));
    }

    // Clamping stops the player from causing an integer overflow and crashing the netty thread
    @Contract("_ -> new")
    public static @NotNull Vector3d clampVector(@NotNull Vector3d toClamp) {
        double x = GrimMath.clamp(toClamp.getX(), -3.0E7D, 3.0E7D);
        double y = GrimMath.clamp(toClamp.getY(), -2.0E7D, 2.0E7D);
        double z = GrimMath.clamp(toClamp.getZ(), -3.0E7D, 3.0E7D);

        return new Vector3d(x, y, z);
    }
}
