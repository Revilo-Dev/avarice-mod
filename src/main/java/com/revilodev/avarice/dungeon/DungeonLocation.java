package com.revilodev.avarice.dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public record DungeonLocation(ResourceKey<Level> dimension, double x, double y, double z, float yaw, float pitch) {
    public static final Codec<DungeonLocation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(DungeonLocation::dimension),
            Codec.DOUBLE.fieldOf("x").forGetter(DungeonLocation::x),
            Codec.DOUBLE.fieldOf("y").forGetter(DungeonLocation::y),
            Codec.DOUBLE.fieldOf("z").forGetter(DungeonLocation::z),
            Codec.FLOAT.fieldOf("yaw").forGetter(DungeonLocation::yaw),
            Codec.FLOAT.fieldOf("pitch").forGetter(DungeonLocation::pitch)
    ).apply(instance, DungeonLocation::new));

    public static DungeonLocation fromPlayer(ServerPlayer player) {
        return new DungeonLocation(player.level().dimension(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
    }

    public static DungeonLocation at(ResourceKey<Level> dimension, BlockPos feet, float yaw) {
        return new DungeonLocation(dimension, feet.getX() + 0.5, feet.getY(), feet.getZ() + 0.5, yaw, 0.0F);
    }

    public BlockPos blockPosition() {
        return BlockPos.containing(x, y, z);
    }
}
