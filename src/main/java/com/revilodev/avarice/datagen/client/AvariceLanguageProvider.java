package com.revilodev.avarice.datagen.client;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.registry.AvariceItems;
import com.revilodev.avarice.registry.AvariceBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class AvariceLanguageProvider extends LanguageProvider {
    public AvariceLanguageProvider(PackOutput output) { super(output, Avarice.MODID, "en_us"); }

    @Override protected void addTranslations() {
        add(AvariceItems.DEV_RELIC.get(), "Developer Relic");
        add(AvariceBlocks.DUNGEON_GATEWAY.get(), "Dungeon Gateway");
        add(AvariceBlocks.COIN_PILE.get(), "Coin Pile");
        AvariceItems.ARMOR.forEach(item -> add(item.get(), title(item.getId().getPath())));
        add(AvariceItems.MANA_STEEL_SWORD.get(), "Mana Steel Sword");
        add(AvariceItems.ELIXRITE_SWORD.get(), "Elixrite Sword");
        add(AvariceItems.ASTRITE_SWORD.get(), "Astrite Sword");
        add(AvariceItems.LUNARIUM_SWORD.get(), "Lunarium Sword");
        add(AvariceItems.IGNITE_SWORD.get(), "Ignite Sword");
        add(AvariceItems.IRIDIUM_SWORD.get(), "Iridium Sword");
        add(AvariceItems.MYTHRIL_SWORD.get(), "Mythril Sword");
        add(AvariceItems.ARCANIUM_SWORD.get(), "Arcanium Sword");
        add(AvariceItems.PRISMATIC_STEEL_SWORD.get(), "Prismatic Steel Sword");
        add(AvariceItems.MANA_STEEL_PAXEL.get(), "Mana Steel Paxel");
        add(AvariceItems.ELIXRITE_PAXEL.get(), "Elixrite Paxel");
        add(AvariceItems.ASTRITE_PAXEL.get(), "Astrite Paxel");
        add(AvariceItems.LUNARIUM_PAXEL.get(), "Lunarium Paxel");
        add(AvariceItems.IGNITE_PAXEL.get(), "Ignite Paxel");
        add(AvariceItems.IRIDIUM_PAXEL.get(), "Iridium Paxel");
        add(AvariceItems.MYTHRIL_PAXEL.get(), "Mythril Paxel");
        add(AvariceItems.ARCANIUM_PAXEL.get(), "Arcanium Paxel");
        add(AvariceItems.PRISMATIC_STEEL_PAXEL.get(), "Prismatic Steel Paxel");
        add(AvariceItems.MANA_STEEL_MAGNET.get(), "Mana Steel Magnet");
        add(AvariceItems.DUNGEON_MAGNET.get(), "Dungeon Magnet");
        add(AvariceItems.ELIXRITE_MAGNET.get(), "Elixrite Magnet");
        add(AvariceItems.ASTRITE_MAGNET.get(), "Astrite Magnet");
        add(AvariceItems.LUNARIUM_MAGNET.get(), "Lunarium Magnet");
        add(AvariceItems.IGNITE_MAGNET.get(), "Ignite Magnet");
        add(AvariceItems.IRIDIUM_MAGNET.get(), "Iridium Magnet");
        add(AvariceItems.MYTHRIL_MAGNET.get(), "Mythril Magnet");
        add(AvariceItems.ARCANIUM_MAGNET.get(), "Arcanium Magnet");
        add(AvariceItems.PRISMATIC_STEEL_MAGNET.get(), "Prismatic Steel Magnet");
        add(AvariceItems.MANA_STEEL_INGOT.get(), "Mana Steel Ingot");
        add(AvariceItems.MANA_STEEL_SCRAP.get(), "Mana Steel Scrap");
        add(AvariceItems.ELIXRITE_INGOT.get(), "Elixrite Ingot");
        add(AvariceItems.ELIXRITE_SCRAP.get(), "Elixrite Scrap");
        add(AvariceItems.ASTRITE_INGOT.get(), "Astrite Ingot");
        add(AvariceItems.ASTRITE_SCRAP.get(), "Astrite Scrap");
        add(AvariceItems.LUNARIUM_INGOT.get(), "Lunarium Ingot");
        add(AvariceItems.LUNARIUM_SCRAP.get(), "Lunarium Scrap");
        add(AvariceItems.IGNIUM_INGOT.get(), "Ignium Ingot");
        add(AvariceItems.IGNIUM_SCRAP.get(), "Ignium Scrap");
        add(AvariceItems.IRIDIUM_INGOT.get(), "Iridium Ingot");
        add(AvariceItems.IRIDIUM_SCRAP.get(), "Iridium Scrap");
        add(AvariceItems.MYTHRIL_INGOT.get(), "Mythril Ingot");
        add(AvariceItems.MYTHRIL_SCRAP.get(), "Mythril Scrap");
        add(AvariceItems.ARCANIUM_INGOT.get(), "Arcanium Ingot");
        add(AvariceItems.ARCANIUM_SCRAP.get(), "Arcanium Scrap");
        add(AvariceItems.PRISMATIC_STEEL_INGOT.get(), "Prismatic Steel Ingot");
        add(AvariceItems.PRISMATIC_STEEL_SCRAP.get(), "Prismatic Steel Scrap");
        add(AvariceItems.MAGNETITE_INGOT.get(), "Magnetite Ingot");
        add(AvariceItems.MAGNETITE_SCRAP.get(), "Magnetite Scrap");
        add("itemGroup.avarice", "Avarice");
        add("itemGroup.avarice.armor_and_gear", "Armor and Gear");
        add("item.avarice.dev_relic.description", "A reloadable development item");
        add("key.category.avarice.development", "Avarice Development");
        add("message.avarice.abyss.entered", "Entered The Abyss. The illuminated gateway near the centre starts a dungeon run.");
        add("message.avarice.dimension.missing", "The required Avarice dimension is unavailable.");
        add("message.avarice.gateway.wrong_dimension", "This gateway is not connected from here.");
        add("message.avarice.run.already_active", "You already have an active dungeon run.");
        add("message.avarice.run.capacity_reached", "All temporary dungeon areas are currently allocated.");
        add("message.avarice.run.completed", "Dungeon run completed. Returned safely to The Abyss.");
        add("message.avarice.run.failed", "Dungeon run failed. Returned safely to The Abyss.");
        add("message.avarice.run.final_floor", "The final placeholder floor must be completed through its gateway.");
        add("message.avarice.run.floor_entered", "Entered placeholder floor %s.");
        add("message.avarice.run.invalid_floor", "That floor transition is not valid.");
        add("message.avarice.run.invalid_transition", "The dungeon run cannot perform that transition.");
        add("message.avarice.run.missing", "The dungeon run no longer exists.");
        add("message.avarice.run.no_online_participants", "No run participants are online for the transition.");
        add("message.avarice.run.none", "You do not have an active dungeon run.");
        add("message.avarice.run.not_complete", "This run has not reached the final placeholder floor.");
        add("message.avarice.run.recovered", "Recovered dungeon run on floor %s.");
        add("message.avarice.run.recovery_failed", "The saved dungeon run could not be recovered.");
        add("message.avarice.run.recovery_fallback", "Run recovery failed. You were returned safely to The Abyss.");
        add("message.avarice.run.requires_abyss", "Start dungeon runs from The Abyss gateway.");
        add("message.avarice.run.returned", "Dungeon run abandoned. Returned safely to The Abyss.");
        add("message.avarice.run.started", "Dungeon run started on placeholder floor %s.");
        add("message.avarice.run.teleport_failed", "The destination was not ready or safe. The transition was cancelled.");
        add("message.avarice.run.transitioning", "A dungeon transition is already in progress.");
        add("message.avarice.status.area", "Allocation slot: %s, floor origin: %s, participants: %s");
        add("message.avarice.status.location", "Dimension: %s, position: %s %s %s");
        add("message.avarice.status.no_run", "Active run: none");
        add("message.avarice.status.persistence", "Persistence dirty: %s, retained runs: %s");
        add("message.avarice.status.run", "Run: %s, status: %s, floor: %s");
        add("message.avarice.status.transition", "Transitioning: %s, target floor: %s");
    }

    private static String title(String id) {
        StringBuilder title = new StringBuilder();
        for (String word : id.split("_")) {
            if (!title.isEmpty()) title.append(' ');
            title.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return title.toString();
    }
}
