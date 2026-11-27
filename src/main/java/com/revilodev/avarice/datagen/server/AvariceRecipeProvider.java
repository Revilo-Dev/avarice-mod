package com.revilodev.avarice.datagen.server;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;

public final class AvariceRecipeProvider extends RecipeProvider {
    public AvariceRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements);
    }

    @Override
    protected void buildRecipes() { }
}
