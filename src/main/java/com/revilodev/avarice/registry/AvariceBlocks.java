package com.revilodev.avarice.registry;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.world.block.DungeonGatewayBlock;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AvariceBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Avarice.MODID);
    public static final DeferredBlock<DungeonGatewayBlock> DUNGEON_GATEWAY = BLOCKS.registerBlock(
            "dungeon_gateway",
            DungeonGatewayBlock::new,
            properties -> properties.strength(-1.0F, 3_600_000.0F).lightLevel(state -> 12).sound(SoundType.AMETHYST).noLootTable()
    );

    private AvariceBlocks() { }
}
