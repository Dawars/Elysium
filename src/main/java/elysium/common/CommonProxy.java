package elysium.common;

import elysium.common.container.ContainerCraftingPillar;
import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by dawar on 2016. 02. 01..
 */
public class CommonProxy implements IGuiHandler {

    public CommonProxy() {
    }

    public void registerBlockMesh(Block block, int metadata, String name) {
    }

    public void registerVariantName(Item item, String name) {
    }

    public void registerItemMesh(Item block, int metadata, String name) {
    }

    public void registerItemMesh(Item item, int metadata, String name, boolean variant) {
    }

    public void registerDisplayInformationInit() {
    }

    public void registerHandlers() {
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                return new ContainerCraftingPillar(player.inventory, (TileCraftingPillar)world.getTileEntity(new BlockPos(x, y, z)));

            default:
                return null;
        }
    }

    public World getClientWorld() {
        return null;
    }

    public void registerKeyBindings() {
    }

    public boolean isShiftKeyDown() {
        return false;
    }

//    public FXDispatcher getFX() {
//        return null;
//    }
//
//    public RenderEventHandler getRenderEventHandler() {
//        return null;
//    }

    public void registerFromSubItems(Item item, String name) {
    }

//    public KeyHandler getKeyBindings() {
//        return null;
//    }

    public void registerDisplayInformationPreInit() {
    }
}
