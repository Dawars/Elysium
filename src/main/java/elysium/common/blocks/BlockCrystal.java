package elysium.common.blocks;

import elysium.api.blocks.BlocksElysium;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockCrystal extends BlockElysium {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockCrystal.CrystalType.class);

    public BlockCrystal() {
        super(Material.glass);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockCrystal.CrystalType.PURE));
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundTypeGlass);
    }

    public IBlockState getStateFromMeta(int meta) {
        return meta < BlockCrystal.CrystalType.values().length ? this.getDefaultState().withProperty(VARIANT, BlockCrystal.CrystalType.values()[meta]) : this.getDefaultState();
    }

    public int getMetaFromState(IBlockState state) {
        int meta = ((BlockCrystal.CrystalType) state.getValue(VARIANT)).ordinal();
        return meta;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT});
    }

    public IProperty[] getProperties() {
        return new IProperty[]{VARIANT};
    }

    public String getStateName(IBlockState state, boolean fullName) {
        BlockCrystal.CrystalType type = (BlockCrystal.CrystalType) state.getValue(VARIANT);
        return type.getName() + (fullName ? "_crystal" : "");
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (this == BlocksElysium.energyCrystal) {
            if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate) {
                return true;
            }

            if (block == this) {
                return false;
            }
        }

        return block != this && super.shouldSideBeRendered(worldIn, pos, side);
    }

    public enum CrystalType implements IStringSerializable {
        PURE,
        CORRUPTED;

        CrystalType() {
        }

        public String getName() {
            return this.name().toLowerCase();
        }

        public String toString() {
            return this.getName();
        }
    }
}
