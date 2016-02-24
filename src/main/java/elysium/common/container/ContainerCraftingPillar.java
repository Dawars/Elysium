package elysium.common.container;

import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import thaumcraft.api.wands.IWand;

public class ContainerCraftingPillar extends Container {
    private TileCraftingPillar tileEntity;
    private InventoryPlayer ip;

    public ContainerCraftingPillar(InventoryPlayer par1InventoryPlayer, TileCraftingPillar e) {
        this.tileEntity = e;
        this.tileEntity.inventory.eventHandler = this;
        this.ip = par1InventoryPlayer;
//        this.addSlotToContainer(new SlotCraftingCraftingPillar(par1InventoryPlayer.player, this.tileEntity.inventory, this.tileEntity.inventory, 9, 160, 64));
//        this.addSlotToContainer(new SlotLimitedByWand(this.tileEntity.inventory, 10, 160, 24));

        int var6;
        int var7;
        for(var6 = 0; var6 < 3; ++var6) {
            for(var7 = 0; var7 < 3; ++var7) {
                this.addSlotToContainer(new Slot(this.tileEntity.inventory, var7 + var6 * 3, 40 + var7 * 24, 40 + var6 * 24));
            }
        }

        for(var6 = 0; var6 < 3; ++var6) {
            for(var7 = 0; var7 < 9; ++var7) {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 16 + var7 * 18, 151 + var6 * 18));
            }
        }

        for(var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 16 + var6 * 18, 209));
        }

        this.onCraftMatrixChanged(this.tileEntity.inventory);
    }

    public void onCraftMatrixChanged(IInventory par1IInventory) {
//        InventoryCrafting ic = new InventoryCrafting(new ContainerDummy(), 3, 3);
//
//        for(int wand = 0; wand < 9; ++wand) {
//            ic.setInventorySlotContents(wand, this.tileEntity.inventory.getStackInSlot(wand));
//        }
//
//        this.tileEntity.inventory.setInventorySlotContentsSoftly(9, CraftingManager.getInstance().findMatchingRecipe(ic, this.tileEntity.getWorld()));
//        if(this.tileEntity.inventory.getStackInSlot(9) == null && this.tileEntity.inventory.getStackInSlot(10) != null && this.tileEntity.inventory.getStackInSlot(10).getItem() instanceof IWand) {
//            IWand var4 = (IWand)this.tileEntity.inventory.getStackInSlot(10).getItem();
//            if(var4.consumeAllVis(this.tileEntity.inventory.getStackInSlot(10), this.ip.player, ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this.tileEntity.inventory, this.ip.player), false, true)) {
//                this.tileEntity.inventory.setInventorySlotContentsSoftly(9, ThaumcraftCraftingManager.findMatchingArcaneRecipe(this.tileEntity.inventory, this.ip.player));
//            }
//        }

        this.tileEntity.markDirty();
        this.tileEntity.getWorld().markBlockForUpdate(this.tileEntity.getPos());
        this.detectAndSendChanges();
    }

    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if(!this.tileEntity.getWorld().isRemote) {
            this.tileEntity.inventory.eventHandler = null;
        }

    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return this.tileEntity.getWorld().getTileEntity(this.tileEntity.getPos()) != this.tileEntity?false:par1EntityPlayer.getDistanceSqToCenter(this.tileEntity.getPos()) <= 64.0D;
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1) {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par1);
        if(var3 != null && var3.getHasStack()) {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if(par1 == 0) {
                if(!this.mergeItemStack(var4, 11, 47, true)) {
                    return null;
                }

                var3.onSlotChange(var4, var2);
            } else if(par1 >= 11 && par1 < 38) {
                if(var4.getItem() instanceof IWand && !((IWand)var4.getItem()).isStaff(var4)) {
                    if(!this.mergeItemStack(var4, 1, 2, false)) {
                        return null;
                    }

                    var3.onSlotChange(var4, var2);
                } else if(!this.mergeItemStack(var4, 38, 47, false)) {
                    return null;
                }
            } else if(par1 >= 38 && par1 < 47) {
                if(var4.getItem() instanceof IWand && !((IWand)var4.getItem()).isStaff(var4)) {
                    if(!this.mergeItemStack(var4, 1, 2, false)) {
                        return null;
                    }

                    var3.onSlotChange(var4, var2);
                } else if(!this.mergeItemStack(var4, 11, 38, false)) {
                    return null;
                }
            } else if(!this.mergeItemStack(var4, 11, 47, false)) {
                return null;
            }

            if(var4.stackSize == 0) {
                var3.putStack((ItemStack)null);
            } else {
                var3.onSlotChanged();
            }

            if(var4.stackSize == var2.stackSize) {
                return null;
            }

            var3.onPickupFromSlot(this.ip.player, var4);
        }

        return var2;
    }

    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
        if(par3 == 4) {
            byte par21 = 1;
            return super.slotClick(par1, par21, par3, par4EntityPlayer);
        } else {
            if((par1 == 0 || par1 == 1) && par2 > 0) {
                par2 = 0;
            }

            return super.slotClick(par1, par2, par3, par4EntityPlayer);
        }
    }

    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.tileEntity && super.canMergeSlot(stack, slot);
    }
}
