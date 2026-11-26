package com.revilodev.avarice.world;

import com.mojang.brigadier.Command;
import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.dungeon.DungeonOperationResult;
import com.revilodev.avarice.dungeon.DungeonRunManager;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = Avarice.MODID)
public final class AvariceServerEvents {
    private AvariceServerEvents() { }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (!developmentEnabled()) return;
        event.getDispatcher().register(
                Commands.literal("avarice")
                        .then(Commands.literal("dev")
                                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                .then(Commands.literal("abyss").executes(context -> execute(
                                        context.getSource(),
                                        DungeonRunManager.get(context.getSource().getServer()).enterAbyss(context.getSource().getPlayerOrException())
                                )))
                                .then(Commands.literal("inventory").executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.openMenu(new SimpleMenuProvider((containerId, inventory, ignored) -> new AvariceInventoryMenu(containerId, inventory), Component.literal("Avarice Inventory")));
                                    return Command.SINGLE_SUCCESS;
                                }))
                                .then(Commands.literal("run")
                                        .then(Commands.literal("start").executes(context -> execute(
                                                context.getSource(),
                                                DungeonRunManager.get(context.getSource().getServer()).startRun(context.getSource().getPlayerOrException())
                                        )))
                                        .then(Commands.literal("next").executes(context -> execute(
                                                context.getSource(),
                                                DungeonRunManager.get(context.getSource().getServer()).advanceFloor(context.getSource().getPlayerOrException())
                                        )))
                                        .then(Commands.literal("return").executes(context -> execute(
                                                context.getSource(),
                                                DungeonRunManager.get(context.getSource().getServer()).abandonRun(context.getSource().getPlayerOrException())
                                        )))
                                        .then(Commands.literal("status").executes(context -> status(context.getSource()))))
                        )
        );
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) DungeonRunManager.get(player.level().getServer()).reconcile(player);
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        DungeonRunManager manager = DungeonRunManager.get(event.getServer());
        if (!manager.initializeAbyss()) {
            Avarice.LOGGER.error("The Abyss could not be initialized as the default spawn dimension");
            return;
        }
        event.getServer().setRespawnData(LevelData.RespawnData.of(AvariceDimensions.ABYSS, AvariceDimensions.ABYSS_SPAWN, 180.0F, 0.0F));
    }

    @SubscribeEvent
    public static void entityJoinedLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()
                && event.getEntity() instanceof Mob
                && event.getLevel().dimension().equals(AvariceDimensions.ABYSS)) {
            event.setCanceled(true);
        }
    }

    private static int execute(CommandSourceStack source, DungeonOperationResult result) {
        Component message = Component.translatable(result.messageKey(), result.run() == null ? 0 : result.run().currentFloor());
        if (result.success()) source.sendSuccess(() -> message, false);
        else source.sendFailure(message);
        return result.success() ? Command.SINGLE_SUCCESS : 0;
    }

    private static int status(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        DungeonRunManager.get(source.getServer()).diagnostics(player).forEach(source::sendSystemMessage);
        return Command.SINGLE_SUCCESS;
    }

    private static boolean developmentEnabled() {
        return Boolean.getBoolean("avarice.devtools");
    }
}
