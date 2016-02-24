package elysium.common.blocks;

import elysium.common.Elysium;
import elysium.common.lib.utils.BlockStateUtils;
import elysium.common.lib.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by dawar on 2016. 02. 06..
 */
public class BlockElysiumMachine extends BlockElysium implements ITileEntityProvider {
    protected final Class tileClass;
    protected static boolean keepInventory;

    public BlockElysiumMachine(Material mat, Class tile) {
        super(mat);
        this.setHardness(2.0F);
        this.setResistance(20.0F);
        this.tileClass = tile;
        this.isBlockContainer = true;
        IBlockState bs = this.blockState.getBaseState();
        if (this instanceof IBlockFacingHorizontal) {
            bs.withProperty(IBlockFacingHorizontal.FACING, EnumFacing.NORTH);
        } else if (this instanceof IBlockFacing) {
            bs.withProperty(IBlockFacing.FACING, EnumFacing.UP);
        }

        if (this instanceof IBlockEnabled) {
            bs.withProperty(IBlockEnabled.ENABLED, true);
        }

        this.setDefaultState(bs);
    }

    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return (TileEntity) this.tileClass.newInstance();
        } catch (InstantiationException var4) {
            Elysium.log.catching(var4);
        } catch (IllegalAccessException var5) {
            Elysium.log.catching(var5);
        }

        return null;
    }

    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.updateState(worldIn, pos, state);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity != null && tileentity instanceof IInventory && !keepInventory) {
            InventoryUtils.dropItems(worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.updateState(worldIn, pos, state);
    }

    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState bs = this.getDefaultState();
        if (this instanceof IBlockFacingHorizontal) {
            bs = bs.withProperty(IBlockFacingHorizontal.FACING, placer.isSneaking() ? placer.getHorizontalFacing() : placer.getHorizontalFacing().getOpposite());
        }

        if (this instanceof IBlockFacing) {
            bs = bs.withProperty(IBlockFacing.FACING, placer.isSneaking() ? BlockPistonBase.getFacingFromEntity(worldIn, pos, placer).getOpposite() : BlockPistonBase.getFacingFromEntity(worldIn, pos, placer));
        }

        if (this instanceof IBlockEnabled) {
            bs = bs.withProperty(IBlockEnabled.ENABLED, true);
        }

        return bs;
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
        if (this instanceof IBlockEnabled) {
            boolean flag = !worldIn.isBlockPowered(pos);
            if (flag != state.getValue(IBlockEnabled.ENABLED)) {
                worldIn.setBlockState(pos, state.withProperty(IBlockEnabled.ENABLED, flag), 3);
            }
        }

    }

    public void updateFacing(World world, BlockPos pos, EnumFacing face) {
        if (this instanceof IBlockFacing || this instanceof IBlockFacingHorizontal) {
            if (face == BlockStateUtils.getFacing(world.getBlockState(pos))) {
                return;
            }

            if (this instanceof IBlockFacingHorizontal && face.getHorizontalIndex() >= 0) {
                world.setBlockState(pos, world.getBlockState(pos).withProperty(IBlockFacingHorizontal.FACING, face), 3);
            }

            if (this instanceof IBlockFacing) {
                world.setBlockState(pos, world.getBlockState(pos).withProperty(IBlockFacing.FACING, face), 3);
            }
        }

    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState bs = this.getDefaultState();

        try {
            if (this instanceof IBlockFacingHorizontal) {
                bs = bs.withProperty(IBlockFacingHorizontal.FACING, BlockStateUtils.getFacing(meta));
            }

            if (this instanceof IBlockFacing) {
                bs = bs.withProperty(IBlockFacing.FACING, BlockStateUtils.getFacing(meta));
            }

            if (this instanceof IBlockEnabled) {
                bs = bs.withProperty(IBlockEnabled.ENABLED, BlockStateUtils.isEnabled(meta));
            }
        } catch (Exception var4) {
            ;
        }

        return bs;
    }

    public int getMetaFromState(IBlockState state) {
        byte b0 = 0;
        int i = this instanceof IBlockFacingHorizontal ? b0 | ((EnumFacing) state.getValue(IBlockFacingHorizontal.FACING)).getIndex() : (this instanceof IBlockFacing ? b0 | ((EnumFacing) state.getValue(IBlockFacing.FACING)).getIndex() : b0);
        if (this instanceof IBlockEnabled && ! state.getValue(IBlockEnabled.ENABLED)) {
            i |= 8;
        }

        return i;
    }

    protected BlockState createBlockState() {
        ArrayList ip = new ArrayList();
        if (this instanceof IBlockFacingHorizontal) {
            ip.add(IBlockFacingHorizontal.FACING);
        }

        if (this instanceof IBlockFacing) {
            ip.add(IBlockFacing.FACING);
        }

        if (this instanceof IBlockEnabled) {
            ip.add(IBlockEnabled.ENABLED);
        }

        return ip.size() == 0 ? super.createBlockState() : new BlockState(this, (IProperty[]) ip.toArray(new IProperty[ip.size()]));
    }
}
