package com.revilodev.avarice.registry;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.devtools.items.ReloadableDefinitionItem;
import com.revilodev.avarice.item.TierItem;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AvariceItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Avarice.MODID);
    public static final DeferredHolder<Item, ReloadableDefinitionItem> DEV_RELIC = ITEMS.register("dev_relic", id -> new ReloadableDefinitionItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).stacksTo(99)));
    public static final DeferredItem<BlockItem> DUNGEON_GATEWAY = ITEMS.register("dungeon_gateway", id -> new BlockItem(AvariceBlocks.DUNGEON_GATEWAY.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).stacksTo(99)));
    public static final DeferredItem<BlockItem> COIN_PILE = ITEMS.register("coin_pile", id -> new BlockItem(AvariceBlocks.COIN_PILE.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).stacksTo(99)));

    public static final DeferredItem<TierItem> MANA_STEEL_SWORD = sword("mana_steel_sword", 8.0, -2.4, 2, "0 s", "0 s", "0 s", "+0.00");
    public static final DeferredItem<TierItem> ELIXRITE_SWORD = sword("elixrite_sword", 9.0, -2.4, 2, "0 s", "0 s", "0 s", "+0.00");
    public static final DeferredItem<TierItem> ASTRITE_SWORD = sword("astrite_sword", 9.0, -2.3, 3, "0 s", "0 s", "0 s", "+0.00");
    public static final DeferredItem<TierItem> LUNARIUM_SWORD = sword("lunarium_sword", 10.0, -2.3, 3, "60 ticks", "0 s", "0 s", "+0.00");
    public static final DeferredItem<TierItem> IGNITE_SWORD = sword("ignite_sword", 10.0, -2.3, 4, "60 ticks", "0 s", "4 s", "+0.25");
    public static final DeferredItem<TierItem> IRIDIUM_SWORD = sword("iridium_sword", 11.0, -2.3, 4, "60 ticks", "0 s", "4 s", "+0.25");
    public static final DeferredItem<TierItem> MYTHRIL_SWORD = sword("mythril_sword", 11.0, -2.2, 5, "60 ticks", "0 s", "4 s", "+0.25");
    public static final DeferredItem<TierItem> ARCANIUM_SWORD = sword("arcanium_sword", 11.0, -2.2, 5, "60 ticks", "80 ticks", "4 s", "+0.25");
    public static final DeferredItem<TierItem> PRISMATIC_STEEL_SWORD = sword("prismatic_steel_sword", 14.0, -2.2, 6, "60 ticks", "80 ticks", "4 s", "+0.25");

    public static final DeferredItem<TierItem> MANA_STEEL_PAXEL = paxel("mana_steel_paxel", 6.0, -2.9, 3);
    public static final DeferredItem<TierItem> ELIXRITE_PAXEL = paxel("elixrite_paxel", 8.0, -2.8, 4);
    public static final DeferredItem<TierItem> ASTRITE_PAXEL = paxel("astrite_paxel", 8.0, -2.8, 5);
    public static final DeferredItem<TierItem> LUNARIUM_PAXEL = paxel("lunarium_paxel", 10.0, -2.8, 6);
    public static final DeferredItem<TierItem> IGNITE_PAXEL = paxel("ignite_paxel", 10.5, -2.75, 7);
    public static final DeferredItem<TierItem> IRIDIUM_PAXEL = paxel("iridium_paxel", 11.0, -2.7, 8);
    public static final DeferredItem<TierItem> MYTHRIL_PAXEL = paxel("mythril_paxel", 11.5, -2.7, 9);
    public static final DeferredItem<TierItem> ARCANIUM_PAXEL = paxel("arcanium_paxel", 12.0, -2.65, 10);
    public static final DeferredItem<TierItem> PRISMATIC_STEEL_PAXEL = paxel("prismatic_steel_paxel", 13.0, -2.6, 11);

    public static final DeferredItem<TierItem> MANA_STEEL_MAGNET = magnet("mana_steel_magnet", 1, 5, 2);
    public static final DeferredItem<TierItem> DUNGEON_MAGNET = magnet("dungeon_magnet", 1, 5, 2);
    public static final DeferredItem<TierItem> ELIXRITE_MAGNET = magnet("elixrite_magnet", 2, 7, 2);
    public static final DeferredItem<TierItem> ASTRITE_MAGNET = magnet("astrite_magnet", 3, 9, 2);
    public static final DeferredItem<TierItem> LUNARIUM_MAGNET = magnet("lunarium_magnet", 3, 11, 2);
    public static final DeferredItem<TierItem> IGNITE_MAGNET = magnet("ignite_magnet", 4, 13, 2);
    public static final DeferredItem<TierItem> IRIDIUM_MAGNET = magnet("iridium_magnet", 4, 15, 2);
    public static final DeferredItem<TierItem> MYTHRIL_MAGNET = magnet("mythril_magnet", 5, 17, 2);
    public static final DeferredItem<TierItem> ARCANIUM_MAGNET = magnet("arcanium_magnet", 6, 18, 2);
    public static final DeferredItem<TierItem> PRISMATIC_STEEL_MAGNET = magnet("prismatic_steel_magnet", 8, 20, 3);

    public static final DeferredItem<TierItem> MANA_STEEL_INGOT = metal("mana_steel_ingot");
    public static final DeferredItem<TierItem> MANA_STEEL_SCRAP = metal("mana_steel_scrap");
    public static final DeferredItem<TierItem> ELIXRITE_INGOT = metal("elixrite_ingot");
    public static final DeferredItem<TierItem> ELIXRITE_SCRAP = metal("elixrite_scrap");
    public static final DeferredItem<TierItem> ASTRITE_INGOT = metal("astrite_ingot");
    public static final DeferredItem<TierItem> ASTRITE_SCRAP = metal("astrite_scrap");
    public static final DeferredItem<TierItem> LUNARIUM_INGOT = metal("lunarium_ingot");
    public static final DeferredItem<TierItem> LUNARIUM_SCRAP = metal("lunarium_scrap");
    public static final DeferredItem<TierItem> IGNIUM_INGOT = metal("ignium_ingot");
    public static final DeferredItem<TierItem> IGNIUM_SCRAP = metal("ignium_scrap");
    public static final DeferredItem<TierItem> IRIDIUM_INGOT = metal("iridium_ingot");
    public static final DeferredItem<TierItem> IRIDIUM_SCRAP = metal("iridium_scrap");
    public static final DeferredItem<TierItem> MYTHRIL_INGOT = metal("mythril_ingot");
    public static final DeferredItem<TierItem> MYTHRIL_SCRAP = metal("mythril_scrap");
    public static final DeferredItem<TierItem> ARCANIUM_INGOT = metal("arcanium_ingot");
    public static final DeferredItem<TierItem> ARCANIUM_SCRAP = metal("arcanium_scrap");
    public static final DeferredItem<TierItem> PRISMATIC_STEEL_INGOT = metal("prismatic_steel_ingot");
    public static final DeferredItem<TierItem> PRISMATIC_STEEL_SCRAP = metal("prismatic_steel_scrap");
    public static final DeferredItem<TierItem> MAGNETITE_INGOT = metal("magnetite_ingot");
    public static final DeferredItem<TierItem> MAGNETITE_SCRAP = metal("magnetite_scrap");

    public static final DeferredItem<Item> ASSASSIN_HELMET = armor("assassin_helmet");
    public static final DeferredItem<Item> ASSASSIN_CHESTPLATE = armor("assassin_chestplate");
    public static final DeferredItem<Item> ASSASSIN_LEGGINGS = armor("assassin_leggings");
    public static final DeferredItem<Item> ASSASSIN_BOOTS = armor("assassin_boots");
    public static final DeferredItem<Item> BESERKER_HELMET = armor("beserker_helmet");
    public static final DeferredItem<Item> BESERKER_CHESTPLATE = armor("beserker_chestplate");
    public static final DeferredItem<Item> BESERKER_LEGGINGS = armor("beserker_leggings");
    public static final DeferredItem<Item> BESERKER_BOOTS = armor("beserker_boots");
    public static final DeferredItem<Item> GLADIATOR_HELMET = armor("gladiator_helmet");
    public static final DeferredItem<Item> GLADIATOR_CHESTPLATE = armor("gladiator_chestplate");
    public static final DeferredItem<Item> GLADIATOR_LEGGINGS = armor("gladiator_leggings");
    public static final DeferredItem<Item> GLADIATOR_BOOTS = armor("gladiator_boots");
    public static final DeferredItem<Item> KNIGHT_HELMET = armor("knight_helmet");
    public static final DeferredItem<Item> KNIGHT_CHESTPLATE = armor("knight_chestplate");
    public static final DeferredItem<Item> KNIGHT_LEGGINGS = armor("knight_leggings");
    public static final DeferredItem<Item> KNIGHT_BOOTS = armor("knight_boots");
    public static final DeferredItem<Item> MARKSMAN_HELMET = armor("marksman_helmet");
    public static final DeferredItem<Item> MARKSMAN_CHESTPLATE = armor("marksman_chestplate");
    public static final DeferredItem<Item> MARKSMAN_LEGGINGS = armor("marksman_leggings");
    public static final DeferredItem<Item> MARKSMAN_BOOTS = armor("marksman_boots");
    public static final DeferredItem<Item> NOMAD_HELMET = armor("nomad_helmet");
    public static final DeferredItem<Item> NOMAD_CHESTPLATE = armor("nomad_chestplate");
    public static final DeferredItem<Item> NOMAD_LEGGINGS = armor("nomad_leggings");
    public static final DeferredItem<Item> NOMAD_BOOTS = armor("nomad_boots");
    public static final DeferredItem<Item> RANGER_HELMET = armor("ranger_helmet");
    public static final DeferredItem<Item> RANGER_CHESTPLATE = armor("ranger_chestplate");
    public static final DeferredItem<Item> RANGER_LEGGINGS = armor("ranger_leggings");
    public static final DeferredItem<Item> RANGER_BOOTS = armor("ranger_boots");
    public static final DeferredItem<Item> REAPER_HELMET = armor("reaper_helmet");
    public static final DeferredItem<Item> REAPER_CHESTPLATE = armor("reaper_chestplate");
    public static final DeferredItem<Item> REAPER_LEGGINGS = armor("reaper_leggings");
    public static final DeferredItem<Item> REAPER_BOOTS = armor("reaper_boots");
    public static final DeferredItem<Item> SAMURAI_HELMET = armor("samurai_helmet");
    public static final DeferredItem<Item> SAMURAI_CHESTPLATE = armor("samurai_chestplate");
    public static final DeferredItem<Item> SAMURAI_LEGGINGS = armor("samurai_leggings");
    public static final DeferredItem<Item> SAMURAI_BOOTS = armor("samurai_boots");
    public static final DeferredItem<Item> SPELLBLADE_HELMET = armor("spellblade_helmet");
    public static final DeferredItem<Item> SPELLBLADE_CHESTPLATE = armor("spellblade_chestplate");
    public static final DeferredItem<Item> SPELLBLADE_LEGGINGS = armor("spellblade_leggings");
    public static final DeferredItem<Item> SPELLBLADE_BOOTS = armor("spellblade_boots");
    public static final DeferredItem<Item> VANGUARD_HELMET = armor("vanguard_helmet");
    public static final DeferredItem<Item> VANGUARD_CHESTPLATE = armor("vanguard_chestplate");
    public static final DeferredItem<Item> VANGUARD_LEGGINGS = armor("vanguard_leggings");
    public static final DeferredItem<Item> VANGUARD_BOOTS = armor("vanguard_boots");
    public static final DeferredItem<Item> WARLOCK_HELMET = armor("warlock_helmet");
    public static final DeferredItem<Item> WARLOCK_CHESTPLATE = armor("warlock_chestplate");
    public static final DeferredItem<Item> WARLOCK_LEGGINGS = armor("warlock_leggings");
    public static final DeferredItem<Item> WARLOCK_BOOTS = armor("warlock_boots");

    public static final List<DeferredItem<Item>> ARMOR = List.of(
            ASSASSIN_HELMET, ASSASSIN_CHESTPLATE, ASSASSIN_LEGGINGS, ASSASSIN_BOOTS, BESERKER_HELMET, BESERKER_CHESTPLATE, BESERKER_LEGGINGS, BESERKER_BOOTS,
            GLADIATOR_HELMET, GLADIATOR_CHESTPLATE, GLADIATOR_LEGGINGS, GLADIATOR_BOOTS, KNIGHT_HELMET, KNIGHT_CHESTPLATE, KNIGHT_LEGGINGS, KNIGHT_BOOTS,
            MARKSMAN_HELMET, MARKSMAN_CHESTPLATE, MARKSMAN_LEGGINGS, MARKSMAN_BOOTS, NOMAD_HELMET, NOMAD_CHESTPLATE, NOMAD_LEGGINGS, NOMAD_BOOTS,
            RANGER_HELMET, RANGER_CHESTPLATE, RANGER_LEGGINGS, RANGER_BOOTS, REAPER_HELMET, REAPER_CHESTPLATE, REAPER_LEGGINGS, REAPER_BOOTS,
            SAMURAI_HELMET, SAMURAI_CHESTPLATE, SAMURAI_LEGGINGS, SAMURAI_BOOTS, SPELLBLADE_HELMET, SPELLBLADE_CHESTPLATE, SPELLBLADE_LEGGINGS, SPELLBLADE_BOOTS,
            VANGUARD_HELMET, VANGUARD_CHESTPLATE, VANGUARD_LEGGINGS, VANGUARD_BOOTS, WARLOCK_HELMET, WARLOCK_CHESTPLATE, WARLOCK_LEGGINGS, WARLOCK_BOOTS
    );

    public static final List<DeferredItem<? extends Item>> CONTENT = List.of(
            MANA_STEEL_SWORD, ELIXRITE_SWORD, ASTRITE_SWORD, LUNARIUM_SWORD, IGNITE_SWORD, IRIDIUM_SWORD, MYTHRIL_SWORD, ARCANIUM_SWORD, PRISMATIC_STEEL_SWORD,
            MANA_STEEL_PAXEL, ELIXRITE_PAXEL, ASTRITE_PAXEL, LUNARIUM_PAXEL, IGNITE_PAXEL, IRIDIUM_PAXEL, MYTHRIL_PAXEL, ARCANIUM_PAXEL, PRISMATIC_STEEL_PAXEL,
            MANA_STEEL_MAGNET, DUNGEON_MAGNET, ELIXRITE_MAGNET, ASTRITE_MAGNET, LUNARIUM_MAGNET, IGNITE_MAGNET, IRIDIUM_MAGNET, MYTHRIL_MAGNET, ARCANIUM_MAGNET, PRISMATIC_STEEL_MAGNET,
            MANA_STEEL_INGOT, MANA_STEEL_SCRAP, ELIXRITE_INGOT, ELIXRITE_SCRAP, ASTRITE_INGOT, ASTRITE_SCRAP, LUNARIUM_INGOT, LUNARIUM_SCRAP, IGNIUM_INGOT, IGNIUM_SCRAP, IRIDIUM_INGOT, IRIDIUM_SCRAP, MYTHRIL_INGOT, MYTHRIL_SCRAP, ARCANIUM_INGOT, ARCANIUM_SCRAP, PRISMATIC_STEEL_INGOT, PRISMATIC_STEEL_SCRAP, MAGNETITE_INGOT, MAGNETITE_SCRAP
    );

    private AvariceItems() { }

    private static DeferredItem<TierItem> sword(String id, double damage, double speed, int slots, String slowness, String poison, String fire, String critDamage) {
        return combat(id, damage, speed, List.of("Rune slots: " + slots, "Slowness: " + slowness, "Poison: " + poison, "Fire: " + fire, "Crit damage: " + critDamage));
    }

    private static DeferredItem<TierItem> paxel(String id, double damage, double speed, int slots) {
        return combat(id, damage, speed, List.of("Rune slots: " + slots));
    }

    private static DeferredItem<TierItem> combat(String id, double damage, double speed, List<String> stats) {
        return ITEMS.register(id, key -> new TierItem(combatProperties(key, id, damage, speed), stats));
    }

    private static DeferredItem<TierItem> magnet(String id, int bonusRange, int pullSpeed, int slots) {
        return ITEMS.register(id, key -> new TierItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key)).stacksTo(1), List.of(
                "Bonus range: +" + bonusRange,
                "Pull speed: " + pullSpeed,
                "Attraction range: " + (3 + bonusRange) + " blocks",
                "Attraction force: " + String.format(java.util.Locale.ROOT, "%.3f", 0.025 + pullSpeed * 0.03),
                "Rune slots: " + slots
        )));
    }

    private static DeferredItem<TierItem> metal(String id) {
        return ITEMS.register(id, key -> new TierItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key)).stacksTo(99), List.of()));
    }

    private static DeferredItem<Item> armor(String id) {
        return ITEMS.register(id, key -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key)).stacksTo(1)));
    }

    private static Item.Properties combatProperties(Identifier id, String name, double damage, double speed) {
        ItemAttributeModifiers attributes = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(Avarice.MODID, name + "_damage"), damage - 1.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(Avarice.MODID, name + "_speed"), speed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).stacksTo(1).attributes(attributes);
    }
}
