package elysium.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class ItemBlockElysium extends ItemBlock {
    public ItemBlockElysium(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int metadata) {
        return metadata;
    }

    public String getUnlocalizedName(ItemStack stack) {
        BlockElysium block = (BlockElysium) this.block;
        return block.hasProperties() ? super.getUnlocalizedName() + "." + block.getStateName(block.getStateFromMeta(stack.getMetadata()), false) : super.getUnlocalizedName();
    }
}
