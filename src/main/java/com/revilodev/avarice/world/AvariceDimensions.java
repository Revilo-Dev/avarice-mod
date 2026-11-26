package com.revilodev.avarice.world;

import com.revilodev.avarice.Avarice;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class AvariceDimensions {
    public static final ResourceKey<Level> ABYSS = key("abyss");
    public static final ResourceKey<Level> DUNGEON = key("dungeon");
    public static final BlockPos ABYSS_SPAWN = new BlockPos(0, 65, 6);
    public static final BlockPos ABYSS_GATEWAY = new BlockPos(0, 65, -4);
    public static final int PLATFORM_Y = 64;

    private AvariceDimensions() { }

    private static ResourceKey<Level> key(String path) {
        return ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(Avarice.MODID, path));
    }
}
