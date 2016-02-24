package elysium.common.blocks;

import elysium.api.blocks.BlocksElysium;
import elysium.common.Elysium;
import elysium.common.blocks.machines.BlockCraftingPillar;
import elysium.common.blocks.machines.BlockEssentiaMixer;
import elysium.common.blocks.world.plants.BlockLeavesElysium;
import elysium.common.blocks.world.plants.BlockLeavesElysiumItem;
import elysium.common.blocks.world.plants.BlockLogsElysium;
import elysium.common.tiles.TileEssentiaMixer;
import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class RegBlocks {


    public static void init() {
        initializeBlocks();
        registerTileEntities();
        BlocksElysium.palestone.setHarvestLevel("pickaxe", 1);
    }

    private static void initializeBlocks() {
        BlocksElysium.palestone = registerBlock(new BlockElysium(Material.rock), "palestone").setHardness(1.25F);
        BlocksElysium.energyCrystal = registerBlock(new BlockCrystal(), "crystal");
        BlocksElysium.plank = registerBlock(new BlockPlanksElysium(), "plank");
        BlocksElysium.log = registerBlock(new BlockLogsElysium(), "log");
        BlocksElysium.leaf = registerBlockBase(new BlockLeavesElysium(), "leaf", BlockLeavesElysiumItem.class);
//        BlocksElysium.sapling = (new BlockSaplingElysium()).setUnlocalizedName("sapling");
//        GameRegistry.registerBlock(BlocksElysium.sapling, BlockSaplingElysiumItem.class, "sapling");
        BlocksElysium.craftingPillar = registerBlock(new BlockCraftingPillar(), "crafting_pillar");

        BlocksElysium.essentiaMixer = registerBlock(new BlockEssentiaMixer(), "essentia_mixer");


    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileCraftingPillar.class, "TileCraftingPillar");
        GameRegistry.registerTileEntity(TileEssentiaMixer.class, "TileEssentiaMixer");

    }

    private static Block registerBlock(BlockElysium block, String name) {
        return registerBlock(block, name, (Class) null);
    }

    private static Block registerBlock(BlockElysium block, String name, Class ib) {
        block.setUnlocalizedName(name);
        if (ib != null && !block.defineVariantsForItemBlock()) {
            GameRegistry.registerBlock(block, ib, name);
            Elysium.proxy.registerVariantName(Item.getItemFromBlock(block), name);
        } else if (block.hasProperties()) {
            Class clazz = ItemBlockElysium.class;
            if (ib != null) {
                clazz = ib;
            }

            GameRegistry.registerBlock(block, clazz, name);

            for (IBlockState state : block.states) {
                String stateName = block.getStateName(state, true);
                Elysium.proxy.registerVariantName(Item.getItemFromBlock(block), stateName);
                Elysium.proxy.registerBlockMesh(block, block.getMetaFromState(state), stateName);
            }
        } else {
            GameRegistry.registerBlock(block, name);
            Elysium.proxy.registerVariantName(Item.getItemFromBlock(block), name);
            Elysium.proxy.registerBlockMesh(block, 0, name);
        }

        return block;
    }

    private static Block registerBlockBase(Block block, String name) {
        return registerBlockBase(block, name, (Class) null);
    }

    private static Block registerBlockBase(Block block, String name, Class ib) {
        block.setUnlocalizedName(name);
        if (ib == null) {
            GameRegistry.registerBlock(block, name);
        } else {
            GameRegistry.registerBlock(block, ib, name);
        }

        Elysium.proxy.registerVariantName(Item.getItemFromBlock(block), name);
        Elysium.proxy.registerBlockMesh(block, 0, name);
        return block;
    }

}
