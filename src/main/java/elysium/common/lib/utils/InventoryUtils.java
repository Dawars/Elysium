package elysium.common.lib.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class InventoryUtils {
    public InventoryUtils() {
    }

    public static boolean hasRoomFor(ItemStack stack, IInventory inventory, EnumFacing side) {
        if(stack != null) {
            ItemStack is = placeItemStackIntoInventory(stack, inventory, side, false);
            return !ItemStack.areItemStacksEqual(stack, is);
        } else {
            return false;
        }
    }

    public static ItemStack placeItemStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing side, boolean doit) {
        ItemStack itemstack = stack.copy();
        ItemStack itemstack1 = insertStack(inventory, itemstack, side, doit);
        if(itemstack1 != null && itemstack1.stackSize != 0) {
            return itemstack1.copy();
        } else {
            if(doit) {
                inventory.markDirty();
            }

            return null;
        }
    }

    public static ItemStack insertStack(IInventory inventory, ItemStack stack1, EnumFacing side, boolean doit) {
        int k2;
        if(inventory instanceof ISidedInventory && side != null) {
            ISidedInventory var8 = (ISidedInventory)inventory;
            int[] var10 = var8.getSlotsForFace(side);
            if(var10 != null) {
                for(k2 = 0; k2 < var10.length && stack1 != null && stack1.stackSize > 0; ++k2) {
                    if(inventory.getStackInSlot(var10[k2]) != null && inventory.getStackInSlot(var10[k2]).isItemEqual(stack1)) {
                        stack1 = attemptInsertion(inventory, stack1, var10[k2], side, doit);
                    }

                    if(stack1 == null || stack1.stackSize == 0) {
                        break;
                    }
                }
            }

            if(var10 != null && stack1 != null && stack1.stackSize > 0) {
                for(k2 = 0; k2 < var10.length && stack1 != null && stack1.stackSize > 0; ++k2) {
                    stack1 = attemptInsertion(inventory, stack1, var10[k2], side, doit);
                    if(stack1 == null || stack1.stackSize == 0) {
                        break;
                    }
                }
            }
        } else {
            int k = inventory.getSizeInventory();

            for(int dc = 0; dc < k && stack1 != null && stack1.stackSize > 0; ++dc) {
                if(inventory.getStackInSlot(dc) != null && inventory.getStackInSlot(dc).isItemEqual(stack1)) {
                    stack1 = attemptInsertion(inventory, stack1, dc, side, doit);
                }

                if(stack1 == null || stack1.stackSize == 0) {
                    break;
                }
            }

            if(stack1 != null && stack1.stackSize > 0) {
                TileEntityChest var9 = null;
                int l;
                if(inventory instanceof TileEntity) {
                    var9 = getDoubleChest((TileEntity)inventory);
                    if(var9 != null) {
                        k2 = var9.getSizeInventory();

                        for(l = 0; l < k2 && stack1 != null && stack1.stackSize > 0; ++l) {
                            if(var9.getStackInSlot(l) != null && var9.getStackInSlot(l).isItemEqual(stack1)) {
                                stack1 = attemptInsertion(var9, stack1, l, side, doit);
                            }

                            if(stack1 == null || stack1.stackSize == 0) {
                                break;
                            }
                        }
                    }
                }

                if(stack1 != null && stack1.stackSize > 0) {
                    for(k2 = 0; k2 < k && stack1 != null && stack1.stackSize > 0; ++k2) {
                        stack1 = attemptInsertion(inventory, stack1, k2, side, doit);
                        if(stack1 == null || stack1.stackSize == 0) {
                            break;
                        }
                    }

                    if(stack1 != null && stack1.stackSize > 0 && var9 != null) {
                        k2 = var9.getSizeInventory();

                        for(l = 0; l < k2 && stack1 != null && stack1.stackSize > 0; ++l) {
                            stack1 = attemptInsertion(var9, stack1, l, side, doit);
                            if(stack1 == null || stack1.stackSize == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(stack1 != null && stack1.stackSize == 0) {
            stack1 = null;
        }

        return stack1;
    }

    private static ItemStack attemptInsertion(IInventory inventory, ItemStack stack, int slot, EnumFacing side, boolean doit) {
        ItemStack slotStack = inventory.getStackInSlot(slot);
        if(canInsertItemToInventory(inventory, stack, slot, side)) {
            boolean flag = false;
            if(slotStack == null) {
                if(inventory.getInventoryStackLimit() < stack.stackSize) {
                    ItemStack k = stack.splitStack(inventory.getInventoryStackLimit());
                    if(doit) {
                        inventory.setInventorySlotContents(slot, k);
                    }
                } else {
                    if(doit) {
                        inventory.setInventorySlotContents(slot, stack);
                    }

                    stack = null;
                }

                flag = true;
            } else if(areItemStacksEqualStrict(slotStack, stack)) {
                int k1 = Math.min(inventory.getInventoryStackLimit() - slotStack.stackSize, stack.getMaxStackSize() - slotStack.stackSize);
                int l = Math.min(stack.stackSize, k1);
                stack.stackSize -= l;
                if(doit) {
                    slotStack.stackSize += l;
                }

                flag = l > 0;
            }

            if(flag && doit) {
                if(inventory instanceof TileEntityHopper) {
                    ((TileEntityHopper)inventory).setTransferCooldown(8);
                    inventory.markDirty();
                }

                inventory.markDirty();
            }
        }

        return stack;
    }

    public static ItemStack getFirstItemInInventory(IInventory inventory, int size, EnumFacing side, boolean doit) {
        ItemStack stack1 = null;
        if(inventory instanceof ISidedInventory && side != null) {
            ISidedInventory var8 = (ISidedInventory)inventory;
            int[] var9 = var8.getSlotsForFace(side);

            for(int j = 0; j < var9.length; ++j) {
                if(stack1 == null && inventory.getStackInSlot(var9[j]) != null) {
                    stack1 = inventory.getStackInSlot(var9[j]).copy();
                    if(size >= 0) {
                        stack1.stackSize = size;
                    }
                }

                if(stack1 != null) {
                    stack1 = attemptExtraction(inventory, stack1, var9[j], side, false, false, false, false, doit);
                }

                if(stack1 != null) {
                    break;
                }
            }
        } else {
            int k = inventory.getSizeInventory();

            for(int l = 0; l < k; ++l) {
                if(stack1 == null && inventory.getStackInSlot(l) != null) {
                    stack1 = inventory.getStackInSlot(l).copy();
                    if(size >= 0) {
                        stack1.stackSize = size;
                    }
                }

                if(stack1 != null) {
                    stack1 = attemptExtraction(inventory, stack1, l, side, false, false, false, false, doit);
                }

                if(stack1 != null) {
                    break;
                }
            }
        }

        if(stack1 != null && stack1.stackSize != 0) {
            return stack1.copy();
        } else {
            if(doit) {
                inventory.markDirty();
            }

            return null;
        }
    }

    public static boolean inventoryContains(IInventory inventory, ItemStack stack, EnumFacing side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean useMod) {
        ItemStack s = extractStack(inventory, stack, side, useOre, ignoreDamage, ignoreNBT, useMod, false);
        return s != null && s.stackSize > 0;
    }

    public static ItemStack extractStack(IInventory inventory, ItemStack stack1, EnumFacing side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean useMod, boolean doit) {
        ItemStack outStack = null;
        if(inventory instanceof ISidedInventory && side != null) {
            ISidedInventory var13 = (ISidedInventory)inventory;
            int[] var12 = var13.getSlotsForFace(side);

            for(int j = 0; j < var12.length && stack1 != null && stack1.stackSize > 0 && outStack == null; ++j) {
                outStack = attemptExtraction(inventory, stack1, var12[j], side, useOre, ignoreDamage, ignoreNBT, useMod, doit);
            }
        } else {
            int k = inventory.getSizeInventory();

            for(int l = 0; l < k && stack1 != null && stack1.stackSize > 0 && outStack == null; ++l) {
                outStack = attemptExtraction(inventory, stack1, l, side, useOre, ignoreDamage, ignoreNBT, useMod, doit);
            }
        }

        return outStack != null && outStack.stackSize != 0?outStack.copy():null;
    }

    public static ItemStack attemptExtraction(IInventory inventory, ItemStack stack, int slot, EnumFacing side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean useMod, boolean doit) {
        ItemStack slotStack = inventory.getStackInSlot(slot);
        ItemStack outStack = stack.copy();
        if(canExtractItemFromInventory(inventory, slotStack, slot, side)) {
            boolean flag = false;
            if(areItemStacksEqual(slotStack, stack, useOre, ignoreDamage, ignoreNBT, useMod)) {
                outStack = slotStack.copy();
                outStack.stackSize = stack.stackSize;
                int k = stack.stackSize - slotStack.stackSize;
                if(k >= 0) {
                    outStack.stackSize -= k;
                    if(doit) {
                        slotStack = null;
                        inventory.setInventorySlotContents(slot, (ItemStack)null);
                    }
                } else if(doit) {
                    slotStack.stackSize -= outStack.stackSize;
                    inventory.setInventorySlotContents(slot, slotStack);
                }

                flag = true;
                if(flag && doit) {
                    inventory.markDirty();
                }

                return outStack;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean canInsertItemToInventory(IInventory inventory, ItemStack stack1, int par2, EnumFacing side) {
        return stack1 != null && inventory.isItemValidForSlot(par2, stack1) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canInsertItem(par2, stack1, side));
    }

    public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack stack1, int par2, EnumFacing side) {
        return stack1 != null && (!(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canExtractItem(par2, stack1, side));
    }

    public static boolean compareMultipleItems(ItemStack c1, ItemStack[] c2) {
        if(c1 != null && c1.stackSize > 0) {
            ItemStack[] arr$ = c2;
            int len$ = c2.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                ItemStack is = arr$[i$];
                if(is != null && c1.isItemEqual(is) && ItemStack.areItemStackTagsEqual(c1, is)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean areItemStacksEqualStrict(ItemStack stack0, ItemStack stack1) {
        return areItemStacksEqual(stack0, stack1, false, false, false, false);
    }

    public static ItemStack findFirstMatchFromFilter(ItemStack[] filters, boolean blacklist, IInventory inv, EnumFacing face, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
        boolean empty = true;
        ItemStack[] blt = filters;
        int a = filters.length;

        for(int is = 0; is < a; ++is) {
            ItemStack arr$ = blt[is];
            if(arr$ != null) {
                empty = false;
                break;
            }
        }

        if(empty) {
            return getFirstItemInInventory(inv, -99, face, false);
        } else {
            ItemStack var17 = null;

            for(a = 0; a < inv.getSizeInventory(); ++a) {
                ItemStack var18 = inv.getStackInSlot(a);
                if(var18 != null) {
                    ItemStack[] var19 = filters;
                    int len$ = filters.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        ItemStack fs = var19[i$];
                        if(fs != null) {
                            boolean r = areItemStacksEqual(fs.copy(), var18.copy(), useOre, ignoreDamage, ignoreNBT, useMod);
                            if(blacklist) {
                                if(!r) {
                                    if(var17 == null && inventoryContains(inv, var18, face, useOre, ignoreDamage, ignoreNBT, useMod)) {
                                        var17 = var18;
                                    }
                                } else if(blacklist) {
                                    return null;
                                }
                            } else if(r && inventoryContains(inv, var18, face, useOre, ignoreDamage, ignoreNBT, useMod)) {
                                return var18;
                            }
                        }
                    }
                }
            }

            if(blacklist && var17 != null) {
                return var17;
            } else {
                return null;
            }
        }
    }

    public static ItemStack findFirstMatchFromFilter(ItemStack[] filters, boolean blacklist, ItemStack[] stacks, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
        boolean empty = true;
        ItemStack[] blt = filters;
        int arr$ = filters.length;

        int len$;
        ItemStack i$;
        for(len$ = 0; len$ < arr$; ++len$) {
            i$ = blt[len$];
            if(i$ != null) {
                empty = false;
                break;
            }
        }

        if(empty) {
            blt = stacks;
            arr$ = stacks.length;

            for(len$ = 0; len$ < arr$; ++len$) {
                i$ = blt[len$];
                if(i$ != null) {
                    return i$;
                }
            }

            return null;
        } else {
            ItemStack var18 = null;
            ItemStack[] var19 = filters;
            len$ = filters.length;

            for(int var20 = 0; var20 < len$; ++var20) {
                ItemStack fs = var19[var20];
                if(fs != null) {
                    ItemStack[] arr$1 = stacks;
                    int len$1 = stacks.length;

                    for(int i$1 = 0; i$1 < len$1; ++i$1) {
                        ItemStack is = arr$1[i$1];
                        if(is != null) {
                            boolean r = areItemStacksEqual(fs.copy(), is.copy(), useOre, ignoreDamage, ignoreNBT, useMod);
                            if(blacklist && !r) {
                                if(var18 == null) {
                                    var18 = is;
                                }
                            } else if(blacklist) {
                                return null;
                            }

                            if(!blacklist && r) {
                                return is;
                            }
                        }
                    }
                }
            }

            if(blacklist && var18 != null) {
                return var18;
            } else {
                return null;
            }
        }
    }

    public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean useMod) {
        if(stack0 == null && stack1 != null) {
            return false;
        } else if(stack0 != null && stack1 == null) {
            return false;
        } else if(stack0 == null && stack1 == null) {
            return true;
        } else if(useMod) {
            String var12 = "A";
            String var15 = "B";
            String var14 = GameRegistry.findUniqueIdentifierFor(stack0.getItem()).modId;
            if(var14 == null) {
                var14 = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(stack0.getItem())).modId;
            }

            if(var14 != null) {
                var12 = var14;
            }

            String var16 = GameRegistry.findUniqueIdentifierFor(stack1.getItem()).modId;
            if(var16 == null) {
                var16 = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(stack1.getItem())).modId;
            }

            if(var16 != null) {
                var15 = var16;
            }

            return var12.equals(var15);
        } else {
            if(useOre) {
                int[] t1 = OreDictionary.getOreIDs(stack0);
                int[] t2 = t1;
                int len$ = t1.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    int i = t2[i$];
                    if(ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{stack1}, OreDictionary.getOres(OreDictionary.getOreName(i)))) {
                        return true;
                    }
                }
            }

            boolean var11 = true;
            if(!ignoreNBT) {
                var11 = ItemStack.areItemStackTagsEqual(stack0, stack1);
            }

            boolean var13 = stack0.getItemDamage() != stack1.getItemDamage();
            if(ignoreDamage && stack0.isItemStackDamageable() && stack1.isItemStackDamageable()) {
                var13 = false;
            }

            if(var13 && ignoreDamage && (stack0.getItemDamage() == 32767 || stack1.getItemDamage() == 32767)) {
                var13 = false;
            }

            return stack0.getItem() != stack1.getItem()?false:(var13?false:var11);
        }
    }

    public static boolean consumeInventoryItems(EntityPlayer player, ItemStack item, boolean nocheck) {
        if(!nocheck && !isPlayerCarryingAmount(player, item)) {
            return false;
        } else {
            int count = item.stackSize;

            for(int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2) {
                if(player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].isItemEqual(item) && ItemStack.areItemStackTagsEqual(player.inventory.mainInventory[var2], item)) {
                    if(player.inventory.mainInventory[var2].stackSize > count) {
                        player.inventory.mainInventory[var2].stackSize -= count;
                        count = 0;
                    } else {
                        count -= player.inventory.mainInventory[var2].stackSize;
                        player.inventory.mainInventory[var2] = null;
                    }

                    if(count <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static boolean consumeInventoryItems(EntityPlayer player, Item item, int md, int amt) {
        if(!isPlayerCarryingAmount(player, new ItemStack(item, amt, md))) {
            return false;
        } else {
            int count = amt;

            for(int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2) {
                if(player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].getItem() == item && player.inventory.mainInventory[var2].getItemDamage() == md) {
                    if(player.inventory.mainInventory[var2].stackSize > count) {
                        player.inventory.mainInventory[var2].stackSize -= count;
                        count = 0;
                    } else {
                        count -= player.inventory.mainInventory[var2].stackSize;
                        player.inventory.mainInventory[var2] = null;
                    }

                    if(count <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static boolean consumeInventoryItem(EntityPlayer player, Item item, int md) {
        for(int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2) {
            if(player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].getItem() == item && player.inventory.mainInventory[var2].getItemDamage() == md) {
                if(--player.inventory.mainInventory[var2].stackSize <= 0) {
                    player.inventory.mainInventory[var2] = null;
                }

                return true;
            }
        }

        return false;
    }

    public static void dropItems(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory)tileEntity;

            for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack item = inventory.getStackInSlot(i);
                if(item != null && item.stackSize > 0) {
                    float rx = world.rand.nextFloat() * 0.8F + 0.1F;
                    float ry = world.rand.nextFloat() * 0.8F + 0.1F;
                    float rz = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityItem = new EntityItem(world, (double)((float)pos.getX() + rx), (double)((float)pos.getY() + ry), (double)((float)pos.getZ() + rz), item.copy());
                    float factor = 0.05F;
                    entityItem.motionX = world.rand.nextGaussian() * (double)factor;
                    entityItem.motionY = world.rand.nextGaussian() * (double)factor + 0.20000000298023224D;
                    entityItem.motionZ = world.rand.nextGaussian() * (double)factor;
                    world.spawnEntityInWorld(entityItem);
                    inventory.setInventorySlotContents(i, (ItemStack)null);
                }
            }

        }
    }

    public static void dropItemAtPos(World world, ItemStack item, BlockPos pos) {
        if(!world.isRemote && item != null && item.stackSize > 0) {
            EntityItem entityItem = new EntityItem(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, item.copy());
            world.spawnEntityInWorld(entityItem);
        }

    }

    public static void dropItemAtEntity(World world, ItemStack item, Entity entity) {
        if(!world.isRemote && item != null && item.stackSize > 0) {
            EntityItem entityItem = new EntityItem(world, entity.posX, entity.posY + (double)(entity.getEyeHeight() / 2.0F), entity.posZ, item.copy());
            world.spawnEntityInWorld(entityItem);
        }

    }

    public static void dropItemsAtEntity(World world, BlockPos pos, Entity entity) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof IInventory && !world.isRemote) {
            IInventory inventory = (IInventory)tileEntity;

            for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                if(!(tileEntity instanceof TileArcaneWorkbench) || i != 9) {
                    ItemStack item = inventory.getStackInSlot(i);
                    if(item != null && item.stackSize > 0) {
                        EntityItem entityItem = new EntityItem(world, entity.posX, entity.posY + (double)(entity.getEyeHeight() / 2.0F), entity.posZ, item.copy());
                        world.spawnEntityInWorld(entityItem);
                        inventory.setInventorySlotContents(i, (ItemStack)null);
                    }
                }
            }

        }
    }

    public static boolean isPlayerCarryingAmount(EntityPlayer player, ItemStack stack) {
        if(stack == null) {
            return false;
        } else {
            int count = stack.stackSize;

            for(int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2) {
                if(player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].isItemEqual(stack) && ItemStack.areItemStackTagsEqual(player.inventory.mainInventory[var2], stack)) {
                    count -= player.inventory.mainInventory[var2].stackSize;
                    if(count <= 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static int isPlayerCarrying(EntityPlayer player, ItemStack stack) {
        for(int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2) {
            if(player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].isItemEqual(stack)) {
                return var2;
            }
        }

        return -1;
    }

    public static ItemStack damageItem(int par1, ItemStack stack, World worldObj) {
        if(stack.isItemStackDamageable()) {
            if(par1 > 0) {
                int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                int var4 = 0;

                for(int var5 = 0; var3 > 0 && var5 < par1; ++var5) {
                    if(EnchantmentDurability.negateDamage(stack, var3, worldObj.rand)) {
                        ++var4;
                    }
                }

                par1 -= var4;
                if(par1 <= 0) {
                    return stack;
                }
            }

            stack.setItemDamage(stack.getItemDamage() + par1);
            if(stack.getItemDamage() > stack.getMaxDamage()) {
                --stack.stackSize;
                if(stack.stackSize < 0) {
                    stack.stackSize = 0;
                }

                stack.setItemDamage(0);
            }
        }

        return stack;
    }

    public static void dropItemsWithChance(World world, int x, int y, int z, float chance, int fortune, ArrayList<ItemStack> items) {
        Iterator i$ = items.iterator();

        while(i$.hasNext()) {
            ItemStack item = (ItemStack)i$.next();
            if(world.rand.nextFloat() <= chance && item.stackSize > 0 && !world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
                float var6 = 0.7F;
                double var7 = (double)(world.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
                double var9 = (double)(world.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
                double var11 = (double)(world.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
                EntityItem var13 = new EntityItem(world, (double)x + var7, (double)y + var9, (double)z + var11, item);
                var13.setPickupDelay(10);
                world.spawnEntityInWorld(var13);
            }
        }

    }

    public static TileEntityChest getDoubleChest(TileEntity tile) {
        if(tile != null && tile instanceof TileEntityChest) {
            if(((TileEntityChest)tile).adjacentChestXNeg != null) {
                return ((TileEntityChest)tile).adjacentChestXNeg;
            }

            if(((TileEntityChest)tile).adjacentChestXPos != null) {
                return ((TileEntityChest)tile).adjacentChestXPos;
            }

            if(((TileEntityChest)tile).adjacentChestZNeg != null) {
                return ((TileEntityChest)tile).adjacentChestZNeg;
            }

            if(((TileEntityChest)tile).adjacentChestZPos != null) {
                return ((TileEntityChest)tile).adjacentChestZPos;
            }
        }

        return null;
    }

    public static ItemStack cycleItemStack(Object input) {
        return cycleItemStack(input, 0);
    }

    public static ItemStack cycleItemStack(Object input, int counter) {
        ItemStack it = null;
        int idx;
        if(input instanceof ItemStack[]) {
            ItemStack[] q = (ItemStack[])((ItemStack[])input);
            if(q != null && q.length > 0) {
                idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)q.length);
                it = cycleItemStack(q[idx], counter++);
            }
        } else if(input instanceof ItemStack) {
            it = (ItemStack)input;
            if(it != null && it.getItem() != null && it.getItemDamage() == 32767 && it.getItem().getHasSubtypes()) {
                ArrayList var7 = new ArrayList();
                it.getItem().getSubItems(it.getItem(), it.getItem().getCreativeTab(), var7);
                if(var7 != null && var7.size() > 0) {
                    idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)var7.size());
                    ItemStack it2 = new ItemStack(it.getItem(), 1, idx);
                    it2.setTagCompound(it.getTagCompound());
                    it = it2;
                }
            } else if(it != null && it.getItem() != null && it.getItemDamage() == 32767 && it.isItemStackDamageable()) {
                int var6 = (int)(((long)counter + System.currentTimeMillis() / 10L) % (long)it.getMaxDamage());
                ItemStack var9 = new ItemStack(it.getItem(), 1, var6);
                var9.setTagCompound(it.getTagCompound());
                it = var9;
            }
        } else {
            List var8;
            if(input instanceof List) {
                var8 = (List)input;
                if(var8 != null && var8.size() > 0) {
                    idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)var8.size());
                    it = cycleItemStack(var8.get(idx), counter++);
                }
            } else if(input instanceof String) {
                var8 = OreDictionary.getOres((String)input);
                if(var8 != null && var8.size() > 0) {
                    idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)var8.size());
                    it = cycleItemStack(var8.get(idx), counter++);
                }
            }
        }

        return it;
    }
}
