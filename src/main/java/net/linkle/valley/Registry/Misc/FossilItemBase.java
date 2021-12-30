package net.linkle.valley.Registry.Misc;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class FossilItemBase extends Item {
    public FossilItemBase(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add( new TranslatableText("item.valley.fossilized_starfish.tooltip") );
        tooltip.add( new TranslatableText("item.valley.fossilized_starfish.tooltip_2") );
    }
}
