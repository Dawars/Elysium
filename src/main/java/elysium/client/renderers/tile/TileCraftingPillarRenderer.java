package elysium.client.renderers.tile;

import elysium.common.blocks.machines.BlockCraftingPillar;
import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.wands.IWand;

@SideOnly(Side.CLIENT)
public class TileCraftingPillarRenderer extends TileEntitySpecialRenderer {
    public TileCraftingPillarRenderer() {
    }

    public void renderTileEntityAt(TileCraftingPillar table, double xPos, double yPos, double zPos, float partialTick) {
        if(table.getWorld() != null && table.inventory.getStackInSlot(TileCraftingPillar.WAND_SLOT) != null && table.inventory.getStackInSlot(TileCraftingPillar.WAND_SLOT).getItem() instanceof IWand) {
            int meta = table.getBlockMetadata();

            GL11.glPushMatrix();
            GL11.glTranslatef((float)xPos + 0.55F, (float)yPos + 0.45F, (float)zPos + 0.1F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-45.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0, -0.25F, 0);

            switch(table.facing) {
                case 2:
                    GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case 3:
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                case 4:
                default:
                    break;
                case 5:
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }


            ItemStack is = table.inventory.getStackInSlot(TileCraftingPillar.WAND_SLOT).copy();
            is.stackSize = 1;
            EntityItem entityitem = new EntityItem(table.getWorld(), 0.0D, 0.0D, 0.0D, is);
            entityitem.hoverStart = 0.0F;
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            GL11.glPopMatrix();
        }

    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int q) {
        this.renderTileEntityAt((TileCraftingPillar) par1TileEntity, par2, par4, par6, par8);
    }
}
