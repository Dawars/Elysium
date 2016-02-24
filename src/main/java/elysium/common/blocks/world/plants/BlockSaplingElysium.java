package elysium.common.blocks.world.plants;

import elysium.common.Elysium;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockSaplingElysium extends BlockBush implements IGrowable {
    public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockLogsElysium.LogType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    public BlockSaplingElysium() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockLogsElysium.LogType.FOSTIMBER).withProperty(STAGE, 0));
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.setCreativeTab(Elysium.tabElysium);
        this.setStepSound(soundTypeGrass);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, pos, state, rand);
            }
        }

    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        } else {
            this.generateTree(worldIn, pos, state, rand);
        }

    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
            Object object = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
            byte i = 0;
            byte j = 0;
            switch (BlockSaplingElysium.SwitchEnumType.WOOD_TYPE_LOOKUP[((BlockLogsElysium.LogType) state.getValue(TYPE)).getMetadata()]) {
                case 1:
//                    object = new WorldGenGreatwoodTrees(true, false); TODO: Tree gen
                    break;
                case 2:
//                    object = new WorldGenSilverwoodTrees(true, 7, 4);
            }

            IBlockState iblockstate1 = Blocks.air.getDefaultState();
            worldIn.setBlockState(pos, iblockstate1, 4);
            if (!((WorldGenerator) object).generate(worldIn, rand, pos.add(i, 0, j))) {
                worldIn.setBlockState(pos, state, 4);
            }

        }
    }

    public int damageDropped(IBlockState state) {
        return ((BlockLogsElysium.LogType) state.getValue(TYPE)).getMetadata();
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockLogsElysium.LogType[] aenumtype = BlockLogsElysium.LogType.values();
        int i = aenumtype.length;

        for (int j = 0; j < i; ++j) {
            BlockLogsElysium.LogType enumtype = aenumtype[j];
            list.add(new ItemStack(itemIn, 1, enumtype.getMetadata()));
        }

    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return (double) worldIn.rand.nextFloat() < 0.25D;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.grow(worldIn, pos, state, rand);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, BlockLogsElysium.LogType.byMetadata(meta & 7)).withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
    }

    public int getMetaFromState(IBlockState state) {
        byte b0 = 0;
        int i = b0 | ((BlockLogsElysium.LogType) state.getValue(TYPE)).getMetadata();
        i |= state.getValue(STAGE) << 3;
        return i;
    }

    public String getStateName(IBlockState state) {
        return ((BlockLogsElysium.LogType) state.getValue(TYPE)).getName();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, TYPE, STAGE);
    }

    static final class SwitchEnumType {
        static final int[] WOOD_TYPE_LOOKUP = new int[BlockLogsElysium.LogType.values().length];

        SwitchEnumType() {
        }

        static {
            try {
                WOOD_TYPE_LOOKUP[BlockLogsElysium.LogType.FOSTIMBER.getMetadata()] = 1;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                WOOD_TYPE_LOOKUP[BlockLogsElysium.LogType.GILDENSILV.getMetadata()] = 2;
            } catch (NoSuchFieldError var1) {
                ;
            }

        }
    }
}
