package com.revilodev.avarice.world;

import com.revilodev.avarice.dungeon.DungeonLocation;
import com.revilodev.avarice.dungeon.DungeonRunSavedData;
import com.revilodev.avarice.registry.AvariceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class AbyssService {
    private static final int PLATFORM_RADIUS = 12;
    private static final int END_FEATURE_CLEANUP_RADIUS = 16;

    private final MinecraftServer server;
    private final DungeonRunSavedData data;

    public AbyssService(MinecraftServer server, DungeonRunSavedData data) {
        this.server = server;
        this.data = data;
    }

    public ServerLevel level() {
        return server.getLevel(AvariceDimensions.ABYSS);
    }

    public boolean ensureInitialized() {
        ServerLevel level = level();
        if (level == null) return false;
        level.getChunkAt(AvariceDimensions.ABYSS_SPAWN);
        if (!data.abyssInitialized()) {
            buildPlatform(level);
            data.markAbyssInitialized();
        }
        if (!data.abyssEnvironmentSanitized()) {
            removeLegacyEndFeatures(level);
            data.markAbyssEnvironmentSanitized();
        }
        repairArrival(level);
        level.setBlock(AvariceDimensions.ABYSS_GATEWAY, AvariceBlocks.DUNGEON_GATEWAY.get().defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    public boolean teleport(ServerPlayer player) {
        if (!ensureInitialized()) return false;
        return AvariceTeleporter.teleport(player, level(), returnLocation());
    }

    public DungeonLocation returnLocation() {
        return DungeonLocation.at(AvariceDimensions.ABYSS, AvariceDimensions.ABYSS_SPAWN, 180.0F);
    }

    private void buildPlatform(ServerLevel level) {
        for (int x = -PLATFORM_RADIUS; x <= PLATFORM_RADIUS; x++) {
            for (int z = -PLATFORM_RADIUS; z <= PLATFORM_RADIUS; z++) {
                BlockPos pos = new BlockPos(x, AvariceDimensions.PLATFORM_Y, z);
                boolean border = Math.abs(x) == PLATFORM_RADIUS || Math.abs(z) == PLATFORM_RADIUS;
                level.setBlock(pos, (border ? Blocks.POLISHED_BLACKSTONE_BRICKS : Blocks.DEEPSLATE_TILES).defaultBlockState(), Block.UPDATE_ALL);
            }
        }
        for (BlockPos light : new BlockPos[]{new BlockPos(-8, 64, -8), new BlockPos(8, 64, -8), new BlockPos(-8, 64, 8), new BlockPos(8, 64, 8)}) {
            level.setBlock(light, Blocks.SEA_LANTERN.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    private void repairArrival(ServerLevel level) {
        BlockPos spawn = AvariceDimensions.ABYSS_SPAWN;
        if (!level.getBlockState(spawn.below()).isAir() && level.getBlockState(spawn).isAir() && level.getBlockState(spawn.above()).isAir()) return;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                level.setBlock(spawn.below().offset(x, 0, z), Blocks.DEEPSLATE_TILES.defaultBlockState(), Block.UPDATE_ALL);
                level.setBlock(spawn.offset(x, 0, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                level.setBlock(spawn.above().offset(x, 0, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    private void removeLegacyEndFeatures(ServerLevel level) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = -END_FEATURE_CLEANUP_RADIUS; x <= END_FEATURE_CLEANUP_RADIUS; x++) {
            for (int z = -END_FEATURE_CLEANUP_RADIUS; z <= END_FEATURE_CLEANUP_RADIUS; z++) {
                for (int y = level.getMinY(); y < level.getMaxY(); y++) {
                    cursor.set(x, y, z);
                    if (level.getBlockState(cursor).is(Blocks.BEDROCK)
                            || level.getBlockState(cursor).is(Blocks.END_PORTAL)
                            || level.getBlockState(cursor).is(Blocks.END_GATEWAY)) {
                        level.setBlock(cursor, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }
}
