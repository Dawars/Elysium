package elysium.common.blocks.world.plants;

import com.google.common.base.Predicate;
import elysium.api.blocks.BlocksElysium;
import elysium.common.Elysium;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockLeavesElysium extends BlockLeaves {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockLogsElysium.LogType.class, new Predicate() {
        public boolean apply(BlockLogsElysium.LogType type) {
            return type.getMetadata() < 4;
        }

        public boolean apply(Object p_apply_1_) {
            return this.apply((BlockLogsElysium.LogType) p_apply_1_);
        }
    });

    public BlockLeavesElysium() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockLogsElysium.LogType.FOSTIMBER).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
        this.setCreativeTab(Elysium.tabElysium);
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return Blocks.leaves.getBlockLayer();
    }

    public boolean isOpaqueCube() {
        return Blocks.leaves.isOpaqueCube();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return this.isOpaqueCube() && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state) {
        return this.damageDropped(state) == 1 ? 16777215 : ColorizerFoliage.getFoliageColorBasic();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return this.damageDropped(worldIn.getBlockState(pos)) == 1 ? 16777215 : BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
    }

    public int damageDropped(IBlockState state) {
        return ((BlockLogsElysium.LogType) state.getValue(VARIANT)).getMetadata();
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate) & 3;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockLogsElysium.LogType) state.getValue(VARIANT)).getMetadata());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, this.getWoodElysiumType(meta)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    public int getMetaFromState(IBlockState state) {
        byte b0 = 0;
        int i = b0 | ((BlockLogsElysium.LogType) state.getValue(VARIANT)).getMetadata();
        if (!((Boolean) state.getValue(DECAYABLE)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) state.getValue(CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    protected int getSaplingDropChance(IBlockState state) {
        return ((BlockLogsElysium.LogType) state.getValue(VARIANT)).getMetadata() == 0 ? 44 : 200;
    }

//    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
//        if(state.getValue(VARIANT) == BlockLogsElysium.LogType.SILVERWOOD && worldIn.rand.nextInt((int)((double)chance * 1.5D)) == 0) {
//            spawnAsEntity(worldIn, pos, new ItemStack(ItemsTC.quicksilver, 1, 0));
//        }
//
//    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlocksElysium.sapling);
    }

    public BlockLogsElysium.LogType getWoodElysiumType(int meta) {
        return BlockLogsElysium.LogType.byMetadata(meta & 3);
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT, CHECK_DECAY, DECAYABLE});
    }

    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        IBlockState state = world.getBlockState(pos);
        return new ArrayList(Arrays.asList(new ItemStack[]{new ItemStack(this, 1, ((BlockLogsElysium.LogType) state.getValue(VARIANT)).getMetadata())}));
    }

    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }
}
