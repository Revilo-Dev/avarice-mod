package com.revilodev.avarice.dungeon;

import com.revilodev.avarice.world.AvariceDimensions;
import net.minecraft.core.BlockPos;

public record DungeonArea(int slot) {
    public BlockPos floorOrigin(int floor) {
        if (floor < 1) throw new IllegalArgumentException("floor must be positive");
        int x = slot % DungeonAreaAllocator.GRID_WIDTH * DungeonAreaAllocator.RUN_SPACING;
        int z = slot / DungeonAreaAllocator.GRID_WIDTH * DungeonAreaAllocator.RUN_SPACING
                + (floor - 1) * DungeonAreaAllocator.FLOOR_SPACING;
        return new BlockPos(x, AvariceDimensions.PLATFORM_Y, z);
    }

    public BlockPos floorSpawn(int floor) {
        return floorOrigin(floor).offset(0, 1, 6);
    }

    public BlockPos floorGateway(int floor) {
        return floorOrigin(floor).offset(7, 1, 0);
    }
}
