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
        add("itemGroup.avarice", "Avarice");
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
}
