package elysium.common.config;

import elysium.common.Elysium;
import elysium.common.lib.world.biomes.BiomeHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftEnchantments;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class Config {
    public static Configuration config;
    public static final String CATEGORY_BIOMES = "Biomes";
    //    public static int overworldDim = 0;
    public static int biomePlainID = 200;
    public static int dimensionElysiumId;

    public Config() {
    }

    public static void initialize(File file) {
        config = new Configuration(file);
        config.addCustomCategoryComment("Graphics", "Graphics");
        config.addCustomCategoryComment("Enchantments", "Custom enchantments");
        config.addCustomCategoryComment("Monster_Spawning", "Will these mobs spawn");
        config.addCustomCategoryComment("Research", "Various research related things.");
        config.addCustomCategoryComment("World_Generation", "Settings to turn certain world-gen on or off.");
        config.addCustomCategoryComment("World_Regeneration", "If a chunk is encountered that skipped TC worldgen, then the game will attempt to regenerate certain world features if they are set to true. CAUTION: Best used for worlds created before you added this mod, and only if you know what you are doing. Backups are advised.");
        config.addCustomCategoryComment("Biomes", "Biomes and effects");
        config.addCustomCategoryComment("Runic_Shielding", "Runic Shielding");
        config.load();
        syncConfigurable();

//        Property mfcp = config.get("Biomes", "magical_forest_biome_weight", 3);
//        mfcp.comment = "higher values increases number of magical forest biomes. If you are using biome addon mods you probably want to increase this weight quite a bit";
//        biomeMagicalForestWeight = mfcp.getInt();
        Property biomePlainProp = config.get(CATEGORY_BIOMES, "biome_plain", biomePlainID);
        biomePlainProp.comment = "Elysian Plain biome id";
        biomePlainID = biomePlainProp.getInt();
        if (BiomeGenBase.getBiomeGenArray()[biomePlainID] != null) {
            biomePlainID = BiomeHandler.getFirstFreeBiomeSlot(biomePlainID);
            biomePlainProp.set(biomePlainID);
        }

//        try {
//            BiomeHandler.biomePlain = new BiomeGenPlain(biomePlainID);
//        } catch (Exception var14) {
//            Elysium.log.fatal("Could not register Elysian Plain Biome");
//        }


        Property dimElysium = config.get("Biomes", "elysium_dim", dimensionElysiumId);
        dimensionElysiumId = dimElysium.getInt();
//        Property mdim = config.get("Biomes", "main_dim", overworldDim);
//        mdim.comment = "The dimension considered to be your \'overworld\'. Certain TC structures will only spawn in this dim.";
//        overworldDim = mdim.getInt();

        config.save();
    }

    public static void save() {
        config.save();
    }

    public static void initPotions() {
//        PotionFluxTaint.instance = new PotionFluxTaint(true, 6697847);
//        PotionFluxTaint.init();
    }

    public static void syncConfigurable() {
    }

    public static void initLoot() {
//        ChestGenHooks.addItem("dungeonChest", new WeightedRandomChestContent(is, 1, 1, 1));
//        ChestGenHooks.addItem("pyramidJungleChest", new WeightedRandomChestContent(is, 1, 1, 1));
//        ChestGenHooks.addItem("pyramidDesertyChest", new WeightedRandomChestContent(is, 1, 1, 1));
//        ChestGenHooks.addItem("mineshaftCorridor", new WeightedRandomChestContent(is, 1, 1, 1));
//        ChestGenHooks.addItem("strongholdCorridor", new WeightedRandomChestContent(is, 1, 1, 1));
//        ChestGenHooks.addItem("strongholdCrossing", new WeightedRandomChestContent(is, 1, 1, 1));
//        ChestGenHooks.addItem("villageBlacksmith", new WeightedRandomChestContent(new ItemStack(ItemsTC.ingots), 1, 3, 10));
    }

    public static void initModCompatibility() {
        String[] ores = OreDictionary.getOreNames();
    }


    public static void initMisc() {
        //inside api reg

    }
}
