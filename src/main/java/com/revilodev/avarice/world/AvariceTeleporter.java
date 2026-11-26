package com.revilodev.avarice.world;

import com.revilodev.avarice.dungeon.DungeonLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public final class AvariceTeleporter {
    private AvariceTeleporter() { }

    public static boolean teleport(ServerPlayer player, ServerLevel destination, DungeonLocation location) {
        BlockPos feet = location.blockPosition();
        destination.getChunkAt(feet);
        if (!isSafe(destination, feet)) return false;
        player.resetFallDistance();
        TeleportTransition transition = new TeleportTransition(
                destination,
                new Vec3(location.x(), location.y(), location.z()),
                Vec3.ZERO,
                location.yaw(),
                location.pitch(),
                TeleportTransition.DO_NOTHING
        );
        return player.teleport(transition) != null;
    }

    public static boolean isSafe(ServerLevel level, BlockPos feet) {
        return !level.getBlockState(feet.below()).isAir()
                && level.getBlockState(feet).isAir()
                && level.getBlockState(feet.above()).isAir();
    }
}
