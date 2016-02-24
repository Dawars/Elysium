package elysium.common.blocks.world.plants;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockLeavesElysiumItem extends ItemBlock {
    private final BlockLeavesElysium leaves;

    public BlockLeavesElysiumItem(Block block) {
        super(block);
        this.leaves = (BlockLeavesElysium)block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int damage) {
        return damage | 4;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + this.leaves.getWoodElysiumType(stack.getMetadata()).getName();
    }
}
