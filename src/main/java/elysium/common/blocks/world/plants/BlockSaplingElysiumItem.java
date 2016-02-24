package elysium.common.blocks.world.plants;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockSaplingElysiumItem extends ItemBlock {
    public BlockSaplingElysiumItem(Block par1) {
        super(par1);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int metadata) {
        return metadata;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + ((BlockSaplingElysium) this.block).getStateName(this.block.getStateFromMeta(stack.getMetadata()));
    }
}
