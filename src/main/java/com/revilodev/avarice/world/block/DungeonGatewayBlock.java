package com.revilodev.avarice.world.block;

import com.revilodev.avarice.dungeon.DungeonOperationResult;
import com.revilodev.avarice.dungeon.DungeonRunManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class DungeonGatewayBlock extends Block {
    public DungeonGatewayBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            DungeonOperationResult result = DungeonRunManager.get(serverPlayer.level().getServer()).handleGateway(serverPlayer);
            serverPlayer.sendSystemMessage(Component.translatable(result.messageKey(), result.run() == null ? 0 : result.run().currentFloor()));
        }
        return InteractionResult.SUCCESS;
    }
}
