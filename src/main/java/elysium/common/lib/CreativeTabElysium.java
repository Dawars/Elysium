package elysium.common.lib;

import elysium.api.blocks.BlocksElysium;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by dawar on 2016. 02. 01..
 */
public class CreativeTabElysium extends CreativeTabs {
    public CreativeTabElysium(int index, String label) {
        super(index, label);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(BlocksElysium.palestone);
    }
}
