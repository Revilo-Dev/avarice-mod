package com.revilodev.avarice.devtools.items;

import com.revilodev.avarice.registry.AvariceDataComponents;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

/** One non-gameplay test item whose tooltip is resolved every time from the active definition snapshot. */
public final class ReloadableDefinitionItem extends Item {
    public ReloadableDefinitionItem(Properties properties) { super(properties); }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        String id = stack.get(AvariceDataComponents.DEFINITION_ID.get());
        ItemDefinition definition = ItemDefs.get(id == null ? "dev_relic" : id);
        if (definition == null) { lines.accept(Component.literal("Missing definition: " + id)); return; }
        lines.accept(Component.literal(definition.description()).setStyle(Style.EMPTY.withColor(parseColor(definition.color()))));
        definition.tooltip().forEach(line -> lines.accept(Component.literal(line)));
        definition.stats().forEach((key, value) -> lines.accept(Component.literal(key + ": " + value)));
        definition.percentages().forEach((key, value) -> lines.accept(Component.literal(key + ": " + value + "%")));
        definition.multipliers().forEach((key, value) -> lines.accept(Component.literal(key + ": " + value + "x")));
    }
    private static int parseColor(String value) { try { return Integer.parseInt(value.startsWith("#") ? value.substring(1) : value, 16); } catch (RuntimeException exception) { return 0xFFFFFF; } }
}
