package com.revilodev.avarice.datagen;

import com.revilodev.avarice.datagen.client.AvariceLanguageProvider;
import com.revilodev.avarice.datagen.client.AvariceModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class AvariceDataGenerators {
    private AvariceDataGenerators() { }

    public static void gatherData(GatherDataEvent.Client event) {
        event.addProvider(new AvariceLanguageProvider(event.getGenerator().getPackOutput()));
        event.addProvider(new AvariceModelProvider(event.getGenerator().getPackOutput()));
    }
}
