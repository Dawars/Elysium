package elysium.common.container;

import elysium.api.crafting.ICraftingPillar;
import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import thaumcraft.common.items.wands.ItemWand;

/**
 * Created by dawar on 2016. 02. 06..
 */
public class InventoryCraftingPillar extends InventoryCrafting implements ISidedInventory, ICraftingPillar {

    public ItemStack[] stackList;
    public Container eventHandler;
    private final int inventoryWidth;
    private final int inventoryHeight;

    public InventoryCraftingPillar(Container container, int width, int height) {
        super(container, width, height);
        int k = width * height;
        this.stackList = new ItemStack[k + 2];
        this.eventHandler = container;
        this.inventoryWidth = width;
        this.inventoryHeight = height;
    }

    public int getSizeInventory() {
        return 9;
    }

    public ItemStack getStackInSlot(int index) {
        return index > 10 ? null : this.stackList[index];
    }

    public ItemStack getStackInRowAndColumn(int row, int column) {
        return row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight ? this.getStackInSlot(row + column * this.inventoryWidth) : null;
    }

    public String getName() {
        return "container.craftingpillar";
    }

    public ItemStack removeStackFromSlot(int index) {
        if (this.stackList[index] != null) {
            ItemStack itemstack = this.stackList[index];
            this.stackList[index] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack decrStackSize(int index, int count) {
        if (this.stackList[index] != null) {
            ItemStack itemstack;
            if (this.stackList[index].stackSize <= count) {
                itemstack = this.stackList[index];
                this.stackList[index] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = this.stackList[index].splitStack(count);
                if (this.stackList[index].stackSize == 0) {
                    this.stackList[index] = null;
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackList[index] = stack;
//        this.eventHandler.onCraftMatrixChanged(this);
    }

    public void clear() {
        for (int i = 0; i < this.stackList.length; ++i) {
            this.stackList[i] = null;
        }

    }

    public int getHeight() {
        return this.inventoryHeight;
    }

    public int getWidth() {
        return this.inventoryWidth;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return i == TileCraftingPillar.WAND_SLOT && itemstack.getItem() instanceof ItemWand;
    }

    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing direction) {
        return this.isItemValidForSlot(i, itemstack);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing direction) {
        return i == TileCraftingPillar.WAND_SLOT;
    }

    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{TileCraftingPillar.WAND_SLOT};
    }

    public void setInventorySlotContentsSoftly(int index, ItemStack stack) {
        this.stackList[index] = stack;
    }

    public void dropItemFromSlot(int wandSlot, EntityPlayer playerIn) {

    }
}
