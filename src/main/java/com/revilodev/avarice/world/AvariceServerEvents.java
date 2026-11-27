package com.revilodev.avarice.world;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.currency.Coins;
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
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import java.util.UUID;

@EventBusSubscriber(modid = Avarice.MODID)
public final class AvariceServerEvents {
    private AvariceServerEvents() { }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("transfer")
                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .then(Commands.argument("id", StringArgumentType.word()).executes(context -> {
                            ServerPlayer sender = context.getSource().getPlayerOrException();
                            ServerPlayer target;
                            try { target = context.getSource().getServer().getPlayerList().getPlayer(UUID.fromString(StringArgumentType.getString(context, "id"))); }
                            catch (IllegalArgumentException exception) { context.getSource().sendFailure(Component.literal("Invalid player id")); return 0; }
                            if (target == null || !Coins.transfer(sender, target, IntegerArgumentType.getInteger(context, "amount"))) { context.getSource().sendFailure(Component.literal("Coin transfer failed")); return 0; }
                            return Command.SINGLE_SUCCESS;
                        }))));
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
                                .then(Commands.literal("debug").then(Commands.literal("coins")
                                        .then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(context -> coins(context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "amount"), true))))
                                        .then(Commands.literal("add").then(Commands.argument("amount", IntegerArgumentType.integer(1)).executes(context -> coins(context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "amount"), false))))
                                        .then(Commands.literal("reset").executes(context -> coins(context.getSource().getPlayerOrException(), 0, true)))))
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

    @SubscribeEvent
    public static void removeHunger(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        event.getEntity().getFoodData().setFoodLevel(20);
        event.getEntity().getFoodData().setSaturation(20.0F);
    }

    @SubscribeEvent
    public static void collectCoinPile(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide() || !event.getLevel().getBlockState(event.getPos()).is(com.revilodev.avarice.registry.AvariceBlocks.COIN_PILE.get())) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Coins.add(player, 20 + player.getRandom().nextInt(31));
        event.getLevel().removeBlock(event.getPos(), false);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void awardDungeonMobCoins(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide() || !event.getEntity().level().dimension().equals(AvariceDimensions.DUNGEON) || !(event.getEntity() instanceof Mob)) return;
        if (event.getSource().getEntity() instanceof ServerPlayer player) Coins.add(player, 1 + player.getRandom().nextInt(5));
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

    private static int coins(ServerPlayer player, int amount, boolean set) {
        if (set) Coins.set(player, amount); else Coins.add(player, amount);
        player.sendSystemMessage(Component.literal("Coins: " + Coins.balance(player)));
        return Command.SINGLE_SUCCESS;
    }
}
