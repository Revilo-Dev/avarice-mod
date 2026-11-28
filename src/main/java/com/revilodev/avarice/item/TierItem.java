package com.revilodev.avarice.item;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public final class TierItem extends Item {
    private final List<String> stats;

    public TierItem(Properties properties, List<String> stats) {
        super(properties);
        this.stats = List.copyOf(stats);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        stats.forEach(stat -> lines.accept(Component.literal(stat)));
    }
}
