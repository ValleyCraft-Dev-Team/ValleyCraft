package io.github.linkle.valleycraft.init;

import static io.github.linkle.valleycraft.ValleyMain.MOD_ID;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class VBlockTags {

    //Warning!
    //This class makes tags in the Valleycraft namespace instead of the c namespace, unlike VItemTags.
    //This is intentional, because no other mods will have sickles that work the same way as Valleycraft's.

    public static final TagKey<Block> SICKLE_HARVESTABLES = TagKey.of(Registry.BLOCK_KEY, newId("sickle_harvestables"));

    // private constructor to avoid instantiation
    private VBlockTags() {
        assert false; // should never be called
    }

    private static Identifier newId(String id) {
        return new Identifier(MOD_ID, id);
    }

}
