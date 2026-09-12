package com.hezhong.mccollisions.entity;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import lombok.experimental.UtilityClass;

/**
 * Standalone port of Grim's BoundingBoxSize. Computes entity width/height per entity type,
 * baby state, size (slime/magma/phantom), elder guardian, pose and client version.
 */
@UtilityClass
public final class EntityData {

    public static float getWidth(EntityState entity) {
        float width = getWidthMinusBaby(entity);
        return width * (entity.isBaby() ? getBabyScaleFactor(entity) : 1f);
    }

    private static float getWidthMinusBaby(EntityState entity) {
        final ClientVersion version = entity.getClientVersion();
        final EntityType type = entity.getType();
        if (type == EntityTypes.AXOLOTL) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.375f;
            return version.isNewerThanOrEquals(ClientVersion.V_26_1) && entity.isBaby() ? 0.5f : 0.75f;
        } else if (type == EntityTypes.PANDA) {
            return 1.3f;
        } else if (type == EntityTypes.BAT || type == EntityTypes.PARROT || type == EntityTypes.COD || type == EntityTypes.EVOKER_FANGS || type == EntityTypes.TROPICAL_FISH || type == EntityTypes.FROG || type == EntityTypes.COPPER_GOLEM) {
            return 0.5f;
        } else if (type == EntityTypes.ARMADILLO || type == EntityTypes.BEE || type == EntityTypes.PUFFERFISH || type == EntityTypes.SALMON || type == EntityTypes.SNOW_GOLEM || type == EntityTypes.CAVE_SPIDER) {
            return 0.7f;
        } else if (type == EntityTypes.WITHER_SKELETON) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.7f : 0.72f;
        } else if (type == EntityTypes.WITHER_SKULL || type == EntityTypes.SHULKER_BULLET) {
            return 0.3125f;
        } else if (type == EntityTypes.HOGLIN || type == EntityTypes.ZOGLIN) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.75f;
            return 1.3964844f;
        } else if (type == EntityTypes.SKELETON_HORSE || type == EntityTypes.ZOMBIE_HORSE || type == EntityTypes.HORSE || type == EntityTypes.DONKEY || type == EntityTypes.MULE) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.3964844f : 1.4f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT)) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.375f : 1.5f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_NAUTILUS)) {
            return 0.875f;
        } else if (type == EntityTypes.HAPPY_GHAST) {
            return 4.0f;
        } else if (type == EntityTypes.CHICKEN || type == EntityTypes.ENDERMITE || type == EntityTypes.SILVERFISH || type == EntityTypes.VEX || type == EntityTypes.TADPOLE) {
            return type == EntityTypes.CHICKEN && entity.isBaby() && version.isNewerThanOrEquals(ClientVersion.V_26_1) ? 0.3f : 0.4f;
        } else if (type == EntityTypes.RABBIT) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_1)) return entity.isBaby() ? 0.24f : 0.49F;
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.4f : 0.6f;
        } else if (type == EntityTypes.FOX && version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) {
            return 0.36f;
        } else if (type == EntityTypes.GOAT && version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) {
            return 0.45f;
        } else if (type == EntityTypes.CREAKING || type == EntityTypes.STRIDER || type == EntityTypes.COW || type == EntityTypes.SHEEP || type == EntityTypes.MOOSHROOM || type == EntityTypes.PIG || type == EntityTypes.LLAMA || type == EntityTypes.DOLPHIN || type == EntityTypes.WITHER || type == EntityTypes.TRADER_LLAMA || type == EntityTypes.WARDEN || type == EntityTypes.GOAT) {
            return 0.9f;
        } else if (type == EntityTypes.PHANTOM) {
            return 0.9f + entity.getSize() * 0.2f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.GUARDIAN)) {
            return entity.isElderGuardian() ? 1.9975f : 0.85f;
        } else if (type == EntityTypes.END_CRYSTAL) {
            return 2f;
        } else if (type == EntityTypes.ENDER_DRAGON) {
            return 16f;
        } else if (type == EntityTypes.FIREBALL) {
            return 1f;
        } else if (type == EntityTypes.GHAST) {
            return 4f;
        } else if (type == EntityTypes.GIANT) {
            return 3.6f;
        } else if (type == EntityTypes.IRON_GOLEM) {
            return 1.4f;
        } else if (type == EntityTypes.SULFUR_CUBE && version.isNewerThanOrEquals(ClientVersion.V_26_2)) {
            return 0.49f * entity.getSize();
        } else if (type == EntityTypes.MAGMA_CUBE) {
            float size = entity.getSize();
            return version.isNewerThanOrEquals(ClientVersion.V_1_20_5)
                    ? 0.52f * size : version.isNewerThanOrEquals(ClientVersion.V_1_9)
                    ? 2.04f * (0.255f * size) : version.isNewerThanOrEquals(ClientVersion.V_1_8)
                    ? 0.51000005f * size : 0.6f * size;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT)) {
            return 0.98f;
        } else if (type == EntityTypes.PLAYER || type == EntityTypes.MANNEQUIN) {
            float width = entity.getPose().width;
            if (entity.getTransitionalPose() != null) {
                width = Math.max(width, entity.getTransitionalPose().width);
            }

            return width;
        } else if (type == EntityTypes.POLAR_BEAR) {
            return 1.4f;
        } else if (type == EntityTypes.RAVAGER) {
            return 1.95f;
        } else if (type == EntityTypes.SHULKER) {
            return 1f;
        } else if (type == EntityTypes.SLIME || (version.isOlderThan(ClientVersion.V_26_2) && type == EntityTypes.SULFUR_CUBE)) {
            float size = entity.getSize();
            return version.isNewerThanOrEquals(ClientVersion.V_1_20_5)
                    ? 0.52f * size : version.isNewerThanOrEquals(ClientVersion.V_1_9)
                    ? 2.04f * (0.255f * size) : version.isNewerThanOrEquals(ClientVersion.V_1_8)
                    ? 0.51000005f * size : 0.6f * size;
        } else if (type == EntityTypes.SMALL_FIREBALL) {
            return 0.3125f;
        } else if (type == EntityTypes.SPIDER) {
            return 1.4f;
        } else if (type == EntityTypes.SQUID || type == EntityTypes.GLOW_SQUID) {
            return version.isNewerThanOrEquals(ClientVersion.V_26_1) && entity.isBaby() ? 0.5f : version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.8f : 0.95f;
        } else if (type == EntityTypes.TURTLE) {
            return 1.2f;
        } else if (type == EntityTypes.ALLAY) {
            return 0.35f;
        } else if (type == EntityTypes.SNIFFER) {
            return 1.9f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL)) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.95f;
            return 1.7f;
        } else if (type == EntityTypes.WIND_CHARGE) {
            return 0.3125f;
        } else if (type == EntityTypes.ARMOR_STAND) {
            return 0.5F;
        } else if (type == EntityTypes.FALLING_BLOCK) {
            return 0.98F;
        } else if (type == EntityTypes.FIREWORK_ROCKET) {
            return 0.25F;
        } else if ((EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_PIGLIN) || type == EntityTypes.DROWNED || type == EntityTypes.HUSK || type == EntityTypes.ZOMBIE || type == EntityTypes.VILLAGER || type == EntityTypes.ZOMBIE_VILLAGER || type == EntityTypes.ZOMBIFIED_PIGLIN) &&
                version.isNewerThanOrEquals(ClientVersion.V_26_1) && entity.isBaby()) {
            return 0.49f;
        }
        return 0.6f;
    }

    public static double[] getEntityDimensions(EntityState entity) {
        final float scale = (float) entity.getScale();
        final float width = getWidth(entity) * scale;
        final float height = getHeight(entity) * scale;
        return new double[]{width, height, width};
    }

    public static float getHeight(EntityState entity) {
        float height = getHeightMinusBaby(entity);
        return height * (entity.isBaby() ? getBabyScaleFactor(entity) : 1f);
    }

    public static double getMyRidingOffset(EntityState entity) {
        final EntityType type = entity.getType();
        if (type == EntityTypes.PIGLIN || type == EntityTypes.ZOMBIFIED_PIGLIN || type == EntityTypes.ZOMBIE) {
            return entity.isBaby() ? -0.05 : -0.45;
        } else if (type == EntityTypes.SKELETON) {
            return -0.6;
        } else if (type == EntityTypes.ENDERMITE || type == EntityTypes.SILVERFISH) {
            return 0.1;
        } else if (type == EntityTypes.EVOKER || type == EntityTypes.ILLUSIONER || type == EntityTypes.PILLAGER || type == EntityTypes.RAVAGER || type == EntityTypes.VINDICATOR || type == EntityTypes.WITCH) {
            return -0.45;
        } else if (type == EntityTypes.PLAYER || type == EntityTypes.MANNEQUIN) {
            return -0.35;
        }

        if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_ANIMAL)) {
            return 0.14;
        }

        return 0;
    }

    public static double getPassengerRidingOffset(EntityState entity) {
        if (isHorse(entity.getType()))
            return (getHeight(entity) * 0.75) - 0.25;

        final EntityType type = entity.getType();
        if (EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT)) {
            return 0;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT)) {
            return -0.1;
        } else if (type == EntityTypes.HAPPY_GHAST) {
            return 0.5;
        } else if (type == EntityTypes.HOGLIN || type == EntityTypes.ZOGLIN) {
            if (entity.isBaby() && entity.getClientVersion().isNewerThanOrEquals(ClientVersion.V_26_2)) return 0.875;
            return getHeight(entity) - (entity.isBaby() ? 0.2 : 0.15);
        } else if (type == EntityTypes.LLAMA) {
            return getHeight(entity) * 0.67;
        } else if (type == EntityTypes.PIGLIN) {
            return getHeight(entity) * 0.92;
        } else if (type == EntityTypes.RAVAGER) {
            return 2.1;
        } else if (type == EntityTypes.SKELETON) {
            return (getHeight(entity) * 0.75) - 0.1875;
        } else if (type == EntityTypes.SPIDER) {
            return getHeight(entity) * 0.5;
        } else if (type == EntityTypes.STRIDER) {
            if (entity.isBaby() && entity.getClientVersion().isNewerThanOrEquals(ClientVersion.V_26_2)) return 0.65625;
            return getHeight(entity) - 0.19;
        }

        return getHeight(entity) * 0.75;
    }

    private static boolean isHorse(EntityType type) {
        return EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_HORSE)
                || type == EntityTypes.HORSE || type == EntityTypes.DONKEY || type == EntityTypes.MULE
                || type == EntityTypes.SKELETON_HORSE || type == EntityTypes.ZOMBIE_HORSE;
    }

    private static float getHeightMinusBaby(EntityState entity) {
        final ClientVersion version = entity.getClientVersion();
        final EntityType type = entity.getType();
        if (type == EntityTypes.ARMADILLO) {
            return 0.65f;
        } else if (type == EntityTypes.AXOLOTL) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.21f;
            return version.isNewerThanOrEquals(ClientVersion.V_26_1) && entity.isBaby() ? 0.25f : 0.42f;
        } else if (type == EntityTypes.BEE || type == EntityTypes.DOLPHIN || type == EntityTypes.ALLAY) {
            return 0.6f;
        } else if (type == EntityTypes.EVOKER_FANGS || type == EntityTypes.VEX) {
            return 0.8f;
        } else if (type == EntityTypes.SQUID || type == EntityTypes.GLOW_SQUID) {
            return version.isNewerThanOrEquals(ClientVersion.V_26_1) && entity.isBaby() ? 0.63f : version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.8f : 0.95f;
        } else if (type == EntityTypes.PARROT || type == EntityTypes.BAT || type == EntityTypes.PIG || type == EntityTypes.SPIDER) {
            return 0.9f;
        } else if (type == EntityTypes.WITHER_SKULL || type == EntityTypes.SHULKER_BULLET) {
            return 0.3125f;
        } else if (type == EntityTypes.BLAZE) {
            return 1.8f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT)) {
            return 0.5625f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_NAUTILUS)) {
            return 0.95f;
        } else if (type == EntityTypes.HAPPY_GHAST) {
            return 4.0f;
        } else if (type == EntityTypes.CAT) {
            return 0.7f;
        } else if (type == EntityTypes.CAVE_SPIDER) {
            return 0.5f;
        } else if (type == EntityTypes.FROG) {
            return 0.55f;
        } else if (type == EntityTypes.CHICKEN) {
            return entity.isBaby() && version.isNewerThanOrEquals(ClientVersion.V_26_1) ? 0.4f : 0.7f;
        } else if (type == EntityTypes.HOGLIN || type == EntityTypes.ZOGLIN) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.85f;
            return 1.4f;
        } else if (type == EntityTypes.COW) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.4f : 1.3f;
        } else if (type == EntityTypes.STRIDER) {
            return 1.7f;
        } else if (type == EntityTypes.CREEPER) {
            return 1.7f;
        } else if (type == EntityTypes.DONKEY) {
            return 1.5f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.GUARDIAN)) {
            return entity.isElderGuardian() ? 1.9975f : 0.85f;
        } else if (type == EntityTypes.ENDERMAN || type == EntityTypes.WARDEN) {
            return 2.9f;
        } else if (type == EntityTypes.ENDERMITE || type == EntityTypes.COD) {
            return 0.3f;
        } else if (type == EntityTypes.END_CRYSTAL) {
            return 2f;
        } else if (type == EntityTypes.ENDER_DRAGON) {
            return 8f;
        } else if (type == EntityTypes.FIREBALL) {
            return 1f;
        } else if (type == EntityTypes.FOX) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.42f;
            return 0.7f;
        } else if (type == EntityTypes.GHAST) {
            return 4f;
        } else if (type == EntityTypes.GIANT) {
            return 12f;
        } else if (type == EntityTypes.HORSE) {
            return 1.6f;
        } else if (type == EntityTypes.IRON_GOLEM) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.7f : 2.9f;
        } else if (type == EntityTypes.CREAKING) {
            return 2.7f;
        } else if (type == EntityTypes.LLAMA || type == EntityTypes.TRADER_LLAMA) {
            return 1.87f;
        } else if (type == EntityTypes.TROPICAL_FISH) {
            return 0.4f;
        } else if (type == EntityTypes.SULFUR_CUBE && version.isNewerThanOrEquals(ClientVersion.V_26_2)) {
            return 0.49f * entity.getSize();
        } else if (type == EntityTypes.MAGMA_CUBE) {
            float size = entity.getSize();
            return version.isNewerThanOrEquals(ClientVersion.V_1_20_5)
                    ? 0.52f * size : version.isNewerThanOrEquals(ClientVersion.V_1_9)
                    ? 2.04f * (0.255f * size) : version.isNewerThanOrEquals(ClientVersion.V_1_8)
                    ? 0.51000005f * size : 0.6f * size;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT)) {
            return 0.7f;
        } else if (type == EntityTypes.MULE) {
            return 1.6f;
        } else if (type == EntityTypes.MOOSHROOM) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.4f : 1.3f;
        } else if (type == EntityTypes.OCELOT) {
            return 0.7f;
        } else if (type == EntityTypes.PANDA) {
            return 1.25f;
        } else if (type == EntityTypes.PHANTOM) {
            return 0.5f + entity.getSize() * 0.1f;
        } else if (type == EntityTypes.PLAYER || type == EntityTypes.MANNEQUIN) {
            float height = entity.getPose().height;
            if (entity.getTransitionalPose() != null) {
                height = Math.max(height, entity.getTransitionalPose().height);
            }

            return height;
        } else if (type == EntityTypes.POLAR_BEAR) {
            return 1.4f;
        } else if (type == EntityTypes.PUFFERFISH) {
            return 0.7f;
        } else if (type == EntityTypes.RABBIT) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_1)) return entity.isBaby() ? 0.4f : 0.6F;
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.5f : 0.7f;
        } else if (type == EntityTypes.RAVAGER) {
            return 2.2f;
        } else if (type == EntityTypes.SALMON) {
            return 0.4f;
        } else if (type == EntityTypes.SHEEP || type == EntityTypes.GOAT) {
            if (type == EntityTypes.GOAT && version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 0.65f;
            return 1.3f;
        } else if (type == EntityTypes.SHULKER) {
            return 2f;
        } else if (type == EntityTypes.SILVERFISH) {
            return 0.3f;
        } else if (type == EntityTypes.SKELETON) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.99f : 1.95f;
        } else if (type == EntityTypes.SKELETON_HORSE) {
            return 1.6f;
        } else if (type == EntityTypes.SLIME || (version.isOlderThan(ClientVersion.V_26_2) && type == EntityTypes.SULFUR_CUBE)) {
            float size = entity.getSize();
            return version.isNewerThanOrEquals(ClientVersion.V_1_20_5)
                    ? 0.52f * size : version.isNewerThanOrEquals(ClientVersion.V_1_9)
                    ? 2.04f * (0.255f * size) : version.isNewerThanOrEquals(ClientVersion.V_1_8)
                    ? 0.51000005f * size : 0.6f * size;
        } else if (type == EntityTypes.SMALL_FIREBALL) {
            return 0.3125f;
        } else if (type == EntityTypes.SNOW_GOLEM) {
            return 1.9f;
        } else if (type == EntityTypes.STRAY) {
            return 1.99f;
        } else if (type == EntityTypes.TURTLE) {
            return 0.4f;
        } else if (type == EntityTypes.WITHER) {
            return 3.5f;
        } else if (type == EntityTypes.WITHER_SKELETON) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.4f : 2.535f;
        } else if (type == EntityTypes.WOLF) {
            return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.85f : 0.8f;
        } else if (type == EntityTypes.ZOMBIE_HORSE) {
            return 1.6f;
        } else if (type == EntityTypes.TADPOLE) {
            return 0.3f;
        } else if (type == EntityTypes.SNIFFER) {
            return 1.75f;
        } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL)) {
            if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && entity.isBaby()) return 1.4f;
            return 2.375f;
        } else if (type == EntityTypes.BREEZE) {
            return 1.77f;
        } else if (type == EntityTypes.BOGGED) {
            return 1.99f;
        } else if (type == EntityTypes.PARCHED) {
            return 1.99f;
        } else if (type == EntityTypes.WIND_CHARGE) {
            return 0.3125f;
        } else if (type == EntityTypes.ARMOR_STAND) {
            return 1.975F;
        } else if (type == EntityTypes.FALLING_BLOCK) {
            return 0.98F;
        } else if (type == EntityTypes.VILLAGER && version.isOlderThan(ClientVersion.V_1_9)) {
            return 1.8F;
        } else if (type == EntityTypes.FIREWORK_ROCKET) {
            return 0.25F;
        } else if (type == EntityTypes.COPPER_GOLEM) {
            return 1.0F;
        } else if ((EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_PIGLIN) || type == EntityTypes.DROWNED || type == EntityTypes.HUSK || type == EntityTypes.ZOMBIE || type == EntityTypes.ZOMBIE_VILLAGER || type == EntityTypes.VILLAGER || type == EntityTypes.ZOMBIFIED_PIGLIN) && version.isNewerThanOrEquals(ClientVersion.V_26_1) && entity.isBaby()) {
            return version.isNewerThanOrEquals(ClientVersion.V_26_2) ? 0.98f : 0.99f;
        }
        return 1.95f;
    }

    private static float getBabyScaleFactor(EntityState entity) {
        final ClientVersion version = entity.getClientVersion();
        final EntityType type = entity.getType();
        if (type == EntityTypes.TURTLE) return 0.3f;
        else if (type == EntityTypes.HAPPY_GHAST) return 0.2375f;
        else if (type == EntityTypes.DOLPHIN) return 0.65f;
        else if (type == EntityTypes.ARMADILLO) return 0.6f;
        else if (version.isNewerThanOrEquals(ClientVersion.V_26_2) && (type == EntityTypes.HOGLIN || type == EntityTypes.ZOGLIN || type == EntityTypes.FOX || type == EntityTypes.GOAT || EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL))) return 1f;
        else if (type == EntityTypes.GOAT && version.isNewerThanOrEquals(ClientVersion.V_26_1)) return 0.55f;
        else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL)) return version.isNewerThanOrEquals(ClientVersion.V_26_1) ? 0.6f : 0.45f;
        else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_HORSE) && version.isNewerThanOrEquals(ClientVersion.V_26_1)) return 0.7f;

        // in 26.1 mojang refactored baby variants, so they have their own independent size, so we return 1f to make this method do nothing
        else if (version.isNewerThanOrEquals(ClientVersion.V_26_1) && (
                type == EntityTypes.CHICKEN ||
                        type == EntityTypes.SQUID ||
                        type == EntityTypes.GLOW_SQUID ||
                        type == EntityTypes.AXOLOTL ||
                        type == EntityTypes.RABBIT ||
                        type == EntityTypes.ZOMBIE ||
                        type == EntityTypes.DROWNED ||
                        type == EntityTypes.HUSK ||
                        type == EntityTypes.VILLAGER ||
                        type == EntityTypes.ZOMBIE_VILLAGER ||
                        type == EntityTypes.ZOMBIFIED_PIGLIN ||
                        EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_PIGLIN)
        )) return 1f;

        return 0.5f;
    }
}
