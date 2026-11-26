package com.revilodev.avarice.dungeon;

import com.revilodev.avarice.registry.AvariceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class PlaceholderFloorService {
    public static final int MIN_FLOOR = 1;
    public static final int MAX_FLOOR = 3;
    private static final int ROOM_RADIUS = 10;
    private static final int WALL_HEIGHT = 6;

    public boolean supports(int floor) {
        return floor >= MIN_FLOOR && floor <= MAX_FLOOR;
    }

    public void ensureFloor(ServerLevel level, DungeonArea area, int floor) {
        if (!supports(floor)) throw new IllegalArgumentException("unsupported placeholder floor " + floor);
        BlockPos origin = area.floorOrigin(floor);
        level.getChunkAt(origin.offset(-ROOM_RADIUS, 0, -ROOM_RADIUS));
        level.getChunkAt(origin.offset(ROOM_RADIUS, 0, ROOM_RADIUS));
        for (int x = -ROOM_RADIUS; x <= ROOM_RADIUS; x++) {
            for (int z = -ROOM_RADIUS; z <= ROOM_RADIUS; z++) {
                BlockPos floorPos = origin.offset(x, 0, z);
                level.setBlock(floorPos, Blocks.POLISHED_DEEPSLATE.defaultBlockState(), Block.UPDATE_ALL);
                for (int y = 1; y <= WALL_HEIGHT; y++) {
                    BlockPos roomPos = floorPos.above(y);
                    boolean wall = Math.abs(x) == ROOM_RADIUS || Math.abs(z) == ROOM_RADIUS;
                    level.setBlock(roomPos, (wall ? Blocks.DEEPSLATE_BRICKS : Blocks.AIR).defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
        for (BlockPos light : new BlockPos[]{origin.offset(-8, 0, -8), origin.offset(8, 0, -8), origin.offset(-8, 0, 8), origin.offset(8, 0, 8)}) {
            level.setBlock(light, Blocks.SEA_LANTERN.defaultBlockState(), Block.UPDATE_ALL);
        }
        level.setBlock(area.floorGateway(floor), AvariceBlocks.DUNGEON_GATEWAY.get().defaultBlockState(), Block.UPDATE_ALL);
        buildFloorNumber(level, origin, floor);
    }

    private void buildFloorNumber(ServerLevel level, BlockPos origin, int floor) {
        String[] pattern = switch (floor) {
            case 1 -> new String[]{"010", "110", "010", "010", "111"};
            case 2 -> new String[]{"111", "001", "111", "100", "111"};
            case 3 -> new String[]{"111", "001", "111", "001", "111"};
            default -> throw new IllegalArgumentException("unsupported placeholder floor " + floor);
        };
        for (int row = 0; row < pattern.length; row++) {
            for (int column = 0; column < pattern[row].length(); column++) {
                BlockPos pos = origin.offset(column - 1, 5 - row, -ROOM_RADIUS + 1);
                level.setBlock(pos, (pattern[row].charAt(column) == '1' ? Blocks.GLOWSTONE : Blocks.POLISHED_BLACKSTONE).defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
