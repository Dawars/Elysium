package elysium.common.tiles.crafting;

import elysium.common.container.InventoryCraftingPillar;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;

import java.util.Random;

/**
 * Created by dawar on 2016. 02. 06..
 */
public class TileCraftingPillar extends TileElysium implements ITickable{
    public InventoryCraftingPillar inventory = new InventoryCraftingPillar((Container) null, 3, 3);
    private Random random;

    public static int WAND_SLOT = 9;
    public static int RESULT_SLOT = 10;

    public float rot = 0F;
    public boolean showNum = false;
    public int facing;

    public TileCraftingPillar() {
        random = new Random();
    }

    @Override
    public void update() {
        if(this.worldObj.isRemote)
        {
            this.rot += 0.1F;
            if(this.rot >= 360F)
                this.rot -= 360F;
        }
    }

    public void readCustomNBT(NBTTagCompound par1NBTTagCompound) {
        NBTTagList tagList = par1NBTTagCompound.getTagList("Inventory", 10);
        this.inventory.stackList = new ItemStack[11];

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            int var5 = tagCompound.getByte("Slot") & 255;
            if (var5 >= 0 && var5 < this.inventory.stackList.length) {
                this.inventory.stackList[var5] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

    }

    public void writeCustomNBT(NBTTagCompound par1NBTTagCompound) {
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.inventory.stackList.length; ++i) {
            if (this.inventory.stackList[i] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) i);
                this.inventory.stackList[i].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        par1NBTTagCompound.setTag("Inventory", tagList);
    }


    public void onInventoryChanged() {

        this.markDirty();
        this.getWorld().markBlockForUpdate(this.getPos());
//        this.inventory.detectAndSendChanges();
    }

//    protected void onCrafting(ItemStack stack) {
//        if(this.amountCrafted > 0) {
//            stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
//        }
//
//        this.amountCrafted = 0;
//        if(stack.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
//            this.thePlayer.triggerAchievement(AchievementList.buildWorkBench);
//        }
//
//        if(stack.getItem() instanceof ItemPickaxe) {
//            this.thePlayer.triggerAchievement(AchievementList.buildPickaxe);
//        }
//
//        if(stack.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
//            this.thePlayer.triggerAchievement(AchievementList.buildFurnace);
//        }
//
//        if(stack.getItem() instanceof ItemHoe) {
//            this.thePlayer.triggerAchievement(AchievementList.buildHoe);
//        }
//
//        if(stack.getItem() == Items.bread) {
//            this.thePlayer.triggerAchievement(AchievementList.makeBread);
//        }
//
//        if(stack.getItem() == Items.cake) {
//            this.thePlayer.triggerAchievement(AchievementList.bakeCake);
//        }
//
//        if(stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe)stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
//            this.thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
//        }
//
//        if(stack.getItem() instanceof ItemSword) {
//            this.thePlayer.triggerAchievement(AchievementList.buildSword);
//        }
//
//        if(stack.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
//            this.thePlayer.triggerAchievement(AchievementList.enchantments);
//        }
//
//        if(stack.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
//            this.thePlayer.triggerAchievement(AchievementList.bookcase);
//        }
//
//        if(stack.getItem() == Items.golden_apple && stack.getMetadata() == 1) {
//            this.thePlayer.triggerAchievement(AchievementList.overpowered);
//        }
//
//    }
//
//    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
//        FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, this.craftMatrix);
//        this.onCrafting(stack);
//        ForgeHooks.setCraftingPlayer(playerIn);
//        AspectList aspects = ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this.craftMatrix, this.thePlayer);
//        if(aspects.size() > 0 && this.craftMatrix.getStackInSlot(10) != null) {
//            IWand aitemstack = (IWand)this.craftMatrix.getStackInSlot(10).getItem();
//            aitemstack.consumeAllVis(this.craftMatrix.getStackInSlot(10), playerIn, aspects, true, true);
//        }
//
//        ItemStack[] var8 = CraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);
//        ForgeHooks.setCraftingPlayer((EntityPlayer)null);
//
//        for(int i = 0; i < Math.min(9, var8.length); ++i) {
//            ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
//            ItemStack itemstack2 = var8[i];
//            if(itemstack1 != null) {
//                this.craftMatrix.decrStackSize(i, 1);
//            }
//
//            if(itemstack2 != null) {
//                if(this.craftMatrix.getStackInSlot(i) == null) {
//                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
//                } else if(!this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {
//                    this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
//                }
//            }
//        }
//
//    }
}
