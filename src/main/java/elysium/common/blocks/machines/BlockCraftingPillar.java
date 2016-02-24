package elysium.common.blocks.machines;

import elysium.common.Elysium;
import elysium.common.blocks.BlockElysiumMachine;
import elysium.common.blocks.IBlockFacingHorizontal;
import elysium.common.container.InventoryCraftingPillar;
import elysium.common.lib.utils.InventoryUtils;
import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockCraftingPillar extends BlockElysiumMachine implements IBlockFacingHorizontal{
    public BlockCraftingPillar() {
        super(Material.rock, TileCraftingPillar.class);
        this.setStepSound(Block.soundTypeStone);

        IBlockState bs = this.blockState.getBaseState();
        bs.withProperty(IBlockFacingHorizontal.FACING, EnumFacing.NORTH);
        this.setDefaultState(bs);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState bs = this.getDefaultState();
        bs = bs.withProperty(IBlockFacingHorizontal.FACING, placer.getHorizontalFacing());

        ((TileCraftingPillar)worldIn.getTileEntity(pos)).facing = placer.getHorizontalFacing().getIndex();
        return bs;
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileCraftingPillar) {
            InventoryCraftingPillar inventory = ((TileCraftingPillar) tileEntity).inventory;

            for (int i = 0; i < 11; ++i) {
                if (i != 9) {
                    ItemStack item = inventory.getStackInSlot(i);
                    if (item != null && item.stackSize > 0) {
                        float rx = world.rand.nextFloat() * 0.8F + 0.1F;
                        float ry = world.rand.nextFloat() * 0.8F + 0.1F;
                        float rz = world.rand.nextFloat() * 0.8F + 0.1F;
                        EntityItem entityItem = new EntityItem(world, (double) ((float) pos.getX() + rx), (double) ((float) pos.getY() + ry), (double) ((float) pos.getZ() + rz), item.copy());
                        float factor = 0.05F;
                        entityItem.motionX = world.rand.nextGaussian() * (double) factor;
                        entityItem.motionY = world.rand.nextGaussian() * (double) factor + 0.20000000298023224D;
                        entityItem.motionZ = world.rand.nextGaussian() * (double) factor;
                        world.spawnEntityInWorld(entityItem);
                        inventory.setInventorySlotContentsSoftly(i, null);
                    }
                }
            }
        }

        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    /*@Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote)
        {
            TileCraftingPillar tile = (TileCraftingPillar) worldIn.getTileEntity(pos);

            if (playerIn.isSneaking())
            {
                while (tile.inventory.getStackInSlot(tile.inventory.getSizeInventory()) != null)
                    tile.craftItem(playerIn);
            } else if (tile.inventory.getStackInSlot(tile.inventory.getSizeInventory()) != null)
            {
                tile.craftItem(playerIn);
            }
        } else
        {
//            if(CraftingPillars.valentine)
//            {
//                for (int i = 0; i < 7; ++i)
//                {
//                    double d0 = world.rand.nextGaussian() * 0.02D;
//                    double d1 = world.rand.nextGaussian() * 0.02D;
//                    double d2 = world.rand.nextGaussian() * 0.02D;
//                    world.spawnParticle("heart", x + (double) (world.rand.nextFloat()), y + 1, z + (double) (world.rand.nextFloat()), d0, d1, d2);
//                }
//            }
        }
    }*/

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(Elysium.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());

        if (worldIn.isRemote)
            return true;

        Elysium.log.info("right click");

        TileCraftingPillar tile = (TileCraftingPillar) worldIn.getTileEntity(pos);

        ItemStack equipped = playerIn.getCurrentEquippedItem();

        // Thaumcraft start
            if (hitY < 1F && hitY > 0F)
            {
                if (!playerIn.isSneaking())
                {
                    if (equipped != null && tile.inventory.getStackInSlot(TileCraftingPillar.WAND_SLOT) == null && tile.inventory.isItemValidForSlot(TileCraftingPillar.WAND_SLOT, equipped))
                    {
                        ItemStack in = equipped.copy();
                        in.stackSize = 1;
                        tile.inventory.setInventorySlotContents(TileCraftingPillar.WAND_SLOT, in);
                        playerIn.inventory.decrStackSize(TileCraftingPillar.WAND_SLOT, 1);

                        return true;
                    }
                }
                else
                {
                    if (tile.inventory.getStackInSlot(TileCraftingPillar.WAND_SLOT) != null) {

                        InventoryUtils.dropItemAtEntity(worldIn, tile.inventory.getStackInSlot(TileCraftingPillar.WAND_SLOT), playerIn);
                        tile.inventory.setInventorySlotContents(TileCraftingPillar.WAND_SLOT, null);
                    }

                    return true;
                }

            }
        // Thaumcraft end

        if (hitY < 1F && !playerIn.isSneaking())
        {
            tile.showNum = !tile.showNum;
            tile.onInventoryChanged();
        }

        if (hitY == 1F)
        {
            if (playerIn.isSneaking())
            {
                hitX = (int) Math.floor(hitX / 0.33F);
                if (hitX > 2)
                    hitX = 2;
                if (hitX < 0)
                    hitX = 0;
                hitZ = (int) Math.floor(hitZ / 0.33F);
                if (hitZ > 2)
                    hitZ = 2;
                if (hitZ < 0)
                    hitZ = 0;

                int i = (int) (hitX * 3 + hitZ);

                if (tile.inventory.getStackInSlot(i) != null)
                    tile.inventory.dropItemFromSlot(i, playerIn);
            } else if (equipped != null)
            {
                hitX = (int) Math.floor(hitX / 0.33F);
                if (hitX > 2)
                    hitX = 2;
                if (hitX < 0)
                    hitX = 0;
                hitZ = (int) Math.floor(hitZ / 0.33F);
                if (hitZ > 2)
                    hitZ = 2;
                if (hitZ < 0)
                    hitZ = 0;

                int i = (int) (hitX * 3 + hitZ);

                if (tile.inventory.getStackInSlot(i) == null)
                {

                    ItemStack in = equipped.copy();
                    if (!playerIn.capabilities.isCreativeMode)
                        equipped.stackSize--;

                    in.stackSize = 1;
                    tile.inventory.setInventorySlotContents(i, in);
                } else if ((tile.inventory.getStackInSlot(i).isItemEqual(equipped)) && (tile.inventory.getStackInSlot(i).stackSize < tile.inventory.getStackInSlot(i).getMaxStackSize()))
                {
                    if (!playerIn.capabilities.isCreativeMode)
                        equipped.stackSize--;

                    tile.inventory.decrStackSize(i, -1);
                }
            }
        }
        return true;
    }
}
