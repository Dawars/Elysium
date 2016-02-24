package elysium.common.blocks;

import com.google.common.collect.ImmutableSet;
import elysium.common.Elysium;
import elysium.common.lib.utils.BlockStateUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.List;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockElysium extends Block {
    public ImmutableSet<IBlockState> states = BlockStateUtils.getValidStatesForProperties(this.getDefaultState(), this.getProperties());

    public BlockElysium(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    public BlockElysium(Material materialIn) {
        this(materialIn, materialIn.getMaterialMapColor());
        this.setCreativeTab(Elysium.tabElysium);
        this.setResistance(2.0F);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this.hasProperties()) {

            for(IBlockState state : states) {
                list.add(new ItemStack(item, 1, this.getMetaFromState(state)));
            }
        } else {
            list.add(new ItemStack(item, 1, 0));
        }
    }

    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    public IProperty[] getProperties() {
        return null;
    }

    public boolean hasProperties() {
        return this.getProperties() != null;
    }

    public String getStateName(IBlockState state, boolean fullName) {
        String unlocalizedName = state.getBlock().getUnlocalizedName();
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    public boolean defineVariantsForItemBlock() {
        return false;
    }
}
