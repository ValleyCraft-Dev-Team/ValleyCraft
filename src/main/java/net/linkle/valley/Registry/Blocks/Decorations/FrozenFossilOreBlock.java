package net.linkle.valley.Registry.Blocks.Decorations;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.sound.BlockSoundGroup;

public class FrozenFossilOreBlock extends OreBlock {

    public FrozenFossilOreBlock() {
        super(FabricBlockSettings.of(Material.DENSE_ICE)
                .requiresTool()
                //.breakByTool(FabricToolTags.PICKAXES)
                .sounds(BlockSoundGroup.GLASS).nonOpaque()
                .strength(1, 1f));
    }
}
