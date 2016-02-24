package elysium.common.lib.world.biomes;

import elysium.common.Elysium;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BiomeHandler {

    //    public static BiomeGenPlain biomePlain;
    public static void registerBiomes() {
//        BiomeDictionary.registerBiomeType(biomePlain, new BiomeDictionary.Type[]{BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.PLAINS});
    }

    public static int getFirstFreeBiomeSlot(int old) {
        for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; ++i) {
            if (BiomeGenBase.getBiomeGenArray()[i] == null) {
                Elysium.log.warn("Biome slot " + old + " already occupied. Using first free biome slot at " + i);
                return i;
            }
        }

        return -1;
    }
}
