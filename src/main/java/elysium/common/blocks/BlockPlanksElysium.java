package elysium.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

/**
 * Created by dawar on 2016. 02. 02..
 */
public class BlockPlanksElysium extends BlockElysium {
    static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlanksElysium.PlankType.class);

    public BlockPlanksElysium() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, PlankType.FOSTIMBER));
        this.setHarvestLevel("axe", 0);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundTypeWood);
    }

    public IBlockState getStateFromMeta(int meta) {
        return meta < BlockPlanksElysium.PlankType.values().length ? this.getDefaultState().withProperty(VARIANT, BlockPlanksElysium.PlankType.values()[meta]) : this.getDefaultState();
    }

    public int getMetaFromState(IBlockState state) {
        int meta = ((BlockPlanksElysium.PlankType) state.getValue(VARIANT)).ordinal();
        return meta;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT});
    }

    public IProperty[] getProperties() {
        return new IProperty[]{VARIANT};
    }

    public String getStateName(IBlockState state, boolean fullName) {
        BlockPlanksElysium.PlankType type = (BlockPlanksElysium.PlankType) state.getValue(VARIANT);
        return type.getName() + (fullName ? "_plank" : "");
    }

    public static enum PlankType implements IStringSerializable {
        FOSTIMBER,
        GILDENSILV;

        private PlankType() {
        }

        public String getName() {
            return this.name().toLowerCase();
        }

        public String toString() {
            return this.getName();
        }
    }
}
