package elysium.common.blocks.world.plants;

import elysium.common.blocks.BlockElysium;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockLogsElysium extends BlockElysium {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockLogsElysium.LogType.class);
    public static final PropertyEnum AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockLogsElysium() {
        super(Material.wood);
        this.setHarvestLevel("axe", 0);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundTypeWood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, LogType.FOSTIMBER).withProperty(AXIS, EnumFacing.Axis.Y));
    }

    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int metadata, EntityLivingBase entity) {
        return super.onBlockPlaced(world, pos, side, hitX, hitY, hitZ, metadata, entity).withProperty(AXIS, side.getAxis());
    }

    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return this.damageDropped(world.getBlockState(pos)) == 1 ? 6 : super.getLightValue(world, pos);
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(state));
    }

    public int damageDropped(IBlockState state) {
        int baseMeta = ((BlockLogsElysium.LogType) state.getValue(VARIANT)).ordinal();
        return baseMeta * 3;
    }

    public IBlockState getStateFromMeta(int meta) {
        int axis = meta % 3;
        int type = (meta - axis) / 3;
        return this.getDefaultState().withProperty(VARIANT, BlockLogsElysium.LogType.values()[type]).withProperty(AXIS, EnumFacing.Axis.values()[axis]);
    }

    public int getMetaFromState(IBlockState state) {
        int baseMeta = ((BlockLogsElysium.LogType) state.getValue(VARIANT)).ordinal();
        return baseMeta * 3 + ((EnumFacing.Axis) state.getValue(AXIS)).ordinal();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{AXIS, VARIANT});
    }

    public IProperty[] getProperties() {
        return new IProperty[]{VARIANT};
    }

    public String getStateName(IBlockState state, boolean fullName) {
        return ((BlockLogsElysium.LogType) state.getValue(VARIANT)).getName() + (fullName ? "_log" : "");
    }

    public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
        return true;
    }

    public boolean isWood(IBlockAccess world, BlockPos pos) {
        return true;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        byte b0 = 4;
        int i = b0 + 1;
        if (worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
            Iterator iterator = BlockPos.getAllInBox(pos.add(-b0, -b0, -b0), pos.add(b0, b0, b0)).iterator();

            while (iterator.hasNext()) {
                BlockPos blockpos1 = (BlockPos) iterator.next();
                IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
                if (iblockstate1.getBlock().isLeaves(worldIn, blockpos1)) {
                    iblockstate1.getBlock().beginLeavesDecay(worldIn, blockpos1);
                }
            }
        }

    }

    public static enum LogType implements IStringSerializable {
        FOSTIMBER(0),
        GILDENSILV(1);

        private static final BlockLogsElysium.LogType[] META_LOOKUP;
        private final int meta;

        public String getName() {
            return this.name().toLowerCase();
        }

        public String toString() {
            return this.getName();
        }

        private LogType(int meta) {
            this.meta = meta;
        }

        public int getMetadata() {
            return this.meta;
        }

        public static BlockLogsElysium.LogType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        static {
            META_LOOKUP = new BlockLogsElysium.LogType[values().length];
            BlockLogsElysium.LogType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockLogsElysium.LogType var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }

        }
    }
}
