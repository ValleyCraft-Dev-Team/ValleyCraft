package io.github.linkle.valleycraft.world.gen.features;

import java.util.ArrayList;

import io.github.linkle.valleycraft.world.placer.AirBlockPlacer;
import io.github.linkle.valleycraft.world.placer.BlockPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SimplePatchFeature extends Feature<SimplePatchConfig> {

    /** An array list of block placers. */
    private final ArrayList<BlockPlacer> list = new ArrayList<>();

    public SimplePatchFeature() {
        super(SimplePatchConfig.CODEC);
        list.add(new AirBlockPlacer());
    }

    /** Add block placer and return an id */
    public int create(BlockPlacer placer) {
        int id = list.indexOf(placer);
        if (id == -1) {
            list.add(placer);
            return list.size()-1;
        }
        return id;
    }

    @Override
    public boolean generate(FeatureContext<SimplePatchConfig> context) {
        var config = context.getConfig();
        var random = context.getRandom();
        var origin = context.getOrigin();
        var world = context.getWorld();
        var mutable = new BlockPos.Mutable();

        int height = config.ySpread();
        int size = config.xzSpread();
        int tries = config.tries();
        int spawned = 0;

        for (int i = 0; i < tries; ++i) {
            int xOffset = random.nextInt(size*2) - size;
            int yOffset = random.nextInt(height*2) - height;
            int zOffset = random.nextInt(size*2) - size;
            mutable.set(origin, xOffset, yOffset, zOffset);
            var state = config.state().getBlockState(random, mutable);

            if (state.canPlaceAt(world, mutable) && list.get(config.placerId()).place(world, mutable, state)) {
                spawned++;
            }
        }

        return spawned > 0;
    }
}