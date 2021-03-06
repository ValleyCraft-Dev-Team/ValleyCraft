package io.github.linkle.valleycraft.blocks.decorations;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class IceBoxBarrelBlock extends BarrelBlock {

    protected static final VoxelShape BARREL_SHAPE;

    public IceBoxBarrelBlock() {
        super(FabricBlockSettings.of(Material.WOOD)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1f, 2.5f));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BARREL_SHAPE;
    }


    static {
        BARREL_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    }
}

