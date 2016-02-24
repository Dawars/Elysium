package elysium.common.items;

import elysium.api.items.ItemsElysium;
import elysium.common.Elysium;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by dawar on 2016. 02. 03..
 */
public class RegItems {

    public RegItems() {
    }

    public static void preInit() {
        initializeItems();
//        WAND_CAP_IRON = new WandCap("iron", 1.1F, 0, new ItemStack(ItemsTC.wandCaps, 1, 0), 1, new ResourceLocation("thaumcraft", "items/wand/cap_iron_mat"));

    }

    public static void init() {
//        ItemsTC.seals = registerItem(new ItemSealPlacer(), "seal", true);
    }

    public static void postInit() {
    }

    private static void initializeItems() {
        ItemsElysium.antler = registerItem(new Item(), "antler", true);
    }

    private static Item registerItem(Item item, String name, boolean registerInventory) {
        item.setUnlocalizedName(name);
        item.setCreativeTab(Elysium.tabElysium);
        GameRegistry.registerItem(item, name);
        if (registerInventory) {
            if (item instanceof IVariantItems) {
                for (int i = 0; i < ((IVariantItems) item).getVariantNames().length; ++i) {
                    int m = i;
                    if (((IVariantItems) item).getVariantMeta() != null) {
                        m = ((IVariantItems) item).getVariantMeta()[i];
                    }

                    String qq = name + "_" + ((IVariantItems) item).getVariantNames()[i];
                    if (((IVariantItems) item).getVariantNames()[i].equals("*")) {
                        qq = name;
                    }

                    Elysium.proxy.registerItemMesh(item, m, qq, true);
                }
            } else {
                Elysium.proxy.registerFromSubItems(item, name);
            }
        }

        return item;
    }
}
