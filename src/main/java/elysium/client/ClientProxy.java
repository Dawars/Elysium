package elysium.client;

import elysium.api.blocks.BlocksElysium;
import elysium.client.lib.ModelHelper;
import elysium.client.renderers.tile.TileCraftingPillarRenderer;
import elysium.common.CommonProxy;
import elysium.common.Elysium;
import elysium.common.tiles.crafting.TileCraftingPillar;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dawar on 2016. 02. 01..
 */
public class ClientProxy extends CommonProxy {

    //    public RenderEventHandler renderEventHandler;
    //    private static ModelResourceLocation fluidGooLocation = new ModelResourceLocation("Thaumcraft:flux_goo", "fluid");
//    KeyHandler kh = new KeyHandler();
    private static ArrayList<ModelEntry> blocksToRegister = new ArrayList();
    private static ArrayList<ClientProxy.ItemModelEntry> itemsToRegister = new ArrayList();
//    private FXDispatcher fx = new FXDispatcher();

    public ClientProxy() {
    }

    public void registerHandlers() {
//        this.renderEventHandler = new RenderEventHandler();
//        MinecraftForge.EVENT_BUS.register(this.renderEventHandler);
//        FMLCommonHandler.instance().bus().register(this.renderEventHandler);
//        MinecraftForge.EVENT_BUS.register(ParticleEngine.instance);
//        FMLCommonHandler.instance().bus().register(ParticleEngine.instance);
//        ShaderHelper.initShaders();
//        Thaumcraft.modelRegistrationHelper = new ModelRegistrationHelper();
//        ModelLoaderRegistry.registerLoader(new BlockModelLoader());
//        MinecraftForge.EVENT_BUS.register(BlocksElysium.levitator);
//        MinecraftForge.EVENT_BUS.register(BlocksElysium.dioptra);
//        MinecraftForge.EVENT_BUS.register(BlocksElysium.tube);
//        MinecraftForge.EVENT_BUS.register(BlocksElysium.redstoneRelay);
//        Item fluid0 = Item.getItemFromBlock(BlocksElysium.fluxGoo);
//        ModelBakery.addVariantName(fluid0, new String[0]);
//        ModelLoader.setCustomMeshDefinition(fluid0, new ItemMeshDefinition() {
//            public ModelResourceLocation getModelLocation(ItemStack stack) {
//                return ClientProxy.fluidGooLocation;
//            }
//        });
//        ModelLoader.setCustomStateMapper(BlocksElysium.fluxGoo, new StateMapperBase() {
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return ClientProxy.fluidGooLocation;
//            }
//        });
//        Item fluid1 = Item.getItemFromBlock(BlocksElysium.taintDust);
//        ModelBakery.addVariantName(fluid1, new String[0]);
//        ModelLoader.setCustomMeshDefinition(fluid1, new ItemMeshDefinition() {
//            public ModelResourceLocation getModelLocation(ItemStack stack) {
//                return ClientProxy.taintDustLocation;
//            }
//        });
//        ModelLoader.setCustomStateMapper(BlocksElysium.taintDust, new StateMapperBase() {
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return ClientProxy.taintDustLocation;
//            }
//        });
//        Item fluid = Item.getItemFromBlock(BlocksElysium.liquidDeath);
//        ModelBakery.addVariantName(fluid, new String[0]);
//        ModelLoader.setCustomMeshDefinition(fluid, new ItemMeshDefinition() {
//            public ModelResourceLocation getModelLocation(ItemStack stack) {
//                return ClientProxy.fluidDeathLocation;
//            }
//        });
//        ModelLoader.setCustomStateMapper(BlocksElysium.liquidDeath, new StateMapperBase() {
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return ClientProxy.fluidDeathLocation;
//            }
//        });
//        Item fluid2 = Item.getItemFromBlock(BlocksElysium.purifyingFluid);
//        ModelBakery.addVariantName(fluid2, new String[0]);
//        ModelLoader.setCustomMeshDefinition(fluid2, new ItemMeshDefinition() {
//            public ModelResourceLocation getModelLocation(ItemStack stack) {
//                return ClientProxy.fluidPureLocation;
//            }
//        });
//        ModelLoader.setCustomStateMapper(BlocksElysium.purifyingFluid, new StateMapperBase() {
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return ClientProxy.fluidPureLocation;
//            }
//        });
    }

    public void registerKeyBindings() {
//        MinecraftForge.EVENT_BUS.register(this.kh);
    }

//    public KeyHandler getKeyBindings() {
//        return this.kh;
//    }

//    public RenderEventHandler getRenderEventHandler() {
//        return this.renderEventHandler;
//    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (world instanceof WorldClient) {
            switch (ID) {
                case 1:
                    return null /*new GuiPech(player.inventory, world, (EntityPech) ((WorldClient) world).getEntityByID(x))*/;

                default:
                    break;
            }
        }

        return null;
    }

    public void registerDisplayInformationPreInit() {
    }

    public void registerDisplayInformationInit() {

        for (ItemModelEntry modelEntry : itemsToRegister) {
            if (modelEntry.variant) {
                this.registerVariantName(modelEntry.item, modelEntry.name);
            }
            ModelHelper.getItemModelMesher().register(modelEntry.item, modelEntry.metadata, new ModelResourceLocation(Elysium.MODID + ":" + modelEntry.name, "inventory"));
        }

        this.setOtherItemRenderers();
        for (ModelEntry model : blocksToRegister) {
            ModelHelper.registerBlock(model.block, model.metadata, Elysium.MODID + ":" + model.name);
        }

        this.setOtherBlockRenderers();
        this.setupEntityRenderers();
        this.setupTileRenderers();
    }

    public void registerVariantName(Item item, String name) {
        ModelBakery.registerItemVariants(item, new ResourceLocation[]{new ResourceLocation(Elysium.MODID, name)});
    }

    public void registerBlockMesh(Block block, int metadata, String name) {
        blocksToRegister.add(new ClientProxy.ModelEntry(block, metadata, name));
    }

    public void registerItemMesh(Item item, int metadata, String name) {
        itemsToRegister.add(new ClientProxy.ItemModelEntry(item, metadata, name, false));
    }

    public void registerItemMesh(Item item, int metadata, String name, boolean variant) {
        itemsToRegister.add(new ClientProxy.ItemModelEntry(item, metadata, name, variant));
    }

    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

//    public FXDispatcher getFX() {
//        return this.fx;
//    }

    public boolean isShiftKeyDown() {
        return GuiScreen.isShiftKeyDown();
    }

    public void setOtherBlockRenderers() {

        ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.sapling), 0, new ModelResourceLocation("elysium:fostimber_sapling", "inventory"));
        ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.sapling), 1, new ModelResourceLocation("elysium:gildensilv_sapling", "inventory"));
        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlocksElysium.leaf), new ResourceLocation(Elysium.MODID, "fostimber_sapling"), new ResourceLocation(Elysium.MODID, "gildensilv_leaves"));
        ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.leaf), 0, new ModelResourceLocation("elysium:fostimber_leaves", "inventory"));
        ModelBakery.registerItemVariants(Item.getItemFromBlock(BlocksElysium.leaf), new ResourceLocation(Elysium.MODID, "fostimber_sapling"), new ResourceLocation(Elysium.MODID, "gildensilv_leaves"));
        ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.leaf), 1, new ModelResourceLocation("elysium:gildensilv_leaves", "inventory"));

//        ModelRegistrationHelper helper = Elysium.modelRegistrationHelper;
//
//        int loc;
//        for (loc = 0; loc < 16; ++loc) {
//            ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.candle), loc, new ModelResourceLocation("Thaumcraft:candle", "inventory"));
//            ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.nitor), loc, new ModelResourceLocation("Thaumcraft:nitor", "inventory"));
//        }
//
//        ModelBakery.addVariantName(Item.getItemFromBlock(BlocksElysium.slabWood), new String[]{"Thaumcraft:greatwood_half_slab"});
//        ModelBakery.addVariantName(Item.getItemFromBlock(BlocksElysium.slabWood), new String[]{"Thaumcraft:silverwood_half_slab"});
//        ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.slabWood), 0, new ModelResourceLocation("Thaumcraft:greatwood_half_slab", "inventory"));
//        ModelHelper.getItemModelMesher().register(Item.getItemFromBlock(BlocksElysium.slabWood), 1, new ModelResourceLocation("Thaumcraft:silverwood_half_slab", "inventory"));
//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().registerBuiltInBlocks(new Block[]{BlocksElysium.arcaneWorkbenchCharger});

    }

//    public void registerCustomBlockModel(ModelRegistrationHelper helper, String blockName, String stateName) {
//        ModelResourceLocation loc = new ModelResourceLocation(Elysium.MODID + ":" + blockName, stateName);
//        CustomMeshModel model = new CustomMeshModel(blockName);
//        helper.registerCustomBlockModel(loc, model, blockName);
//    }

    public void registerBlockTexture(Block block, String blockName) {
        this.registerBlockTexture(block, 0, blockName);
    }

    public void registerBlockTexture(Block block, int meta, String blockName) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Elysium.MODID + ":" + blockName, "inventory"));
    }

    public void registerFromSubItems(Item item, String name) {
//        if (item instanceof ISubItems) {
//            int[] list = ((ISubItems) item).getSubItems();
//            int i$ = list.length;
//
//            for (int i = 0; i < i$; ++i) {
//                int i1 = list[i];
//                Elysium.proxy.registerItemMesh(item, i1, name);
//            }
//        } else {
        ArrayList var7 = new ArrayList();
        item.getSubItems(item, Elysium.tabElysium, var7);
        if (var7.size() > 0) {
            Iterator var8 = var7.iterator();

            while (var8.hasNext()) {
                ItemStack var9 = (ItemStack) var8.next();
                Elysium.proxy.registerItemMesh(item, var9.getItemDamage(), name);
            }
        } else {
            Elysium.proxy.registerItemMesh(item, 0, name);
        }
//        }

    }

    public void setOtherItemRenderers() {
//        ModelRegistrationHelper helper = Elysium.modelRegistrationHelper;
//        registerWandItemModel(helper, "wand");
//        this.registerItemTexture(ItemsTC.wand, 0, "wand");
//        ModelBakery.addVariantName(ItemsTC.sinisterStone, new String[]{"Thaumcraft:sinister_stone", "Thaumcraft:sinister_stone_on"});
    }

//    public void registerCustomItemModel(ModelRegistrationHelper helper, String itemName) {
//        ModelResourceLocation loc = new ModelResourceLocation("Thaumcraft:" + itemName, "inventory");
//        CustomMeshModel model = new CustomMeshModel(itemName);
//        helper.registerCustomItemModel(loc, model, itemName);
//    }

    public void registerItemTexture(Item item, String itemName) {
        this.registerItemTexture(item, 0, itemName);
    }

    public void registerItemTexture(Item item, int meta, String itemName) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(item, meta, new ModelResourceLocation(Elysium.MODID + ":" + itemName, "inventory"));
        ModelBakery.registerItemVariants(item,new ResourceLocation(Elysium.MODID, itemName));
    }


    public void setupTileRenderers() {
        this.registerTileEntitySpecialRenderer(TileCraftingPillar.class, new TileCraftingPillarRenderer());

    }

    private void registerTileEntitySpecialRenderer(Class tile, TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer);
    }

    public void setupEntityRenderers() {
//        RenderingRegistry.registerEntityRenderingHandler(EntityAuraNode.class, new RenderAuraNode(Minecraft.getMinecraft().getRenderManager()));
//        VillagerRegistry.instance().registerVillagerSkin(ConfigEntities.entWizardId, new ResourceLocation("thaumcraft", "textures/models/creature/wizard.png"));
        TileEntityItemStackRenderer.instance = new ClientProxy.ModeledBlockInventoryRenderer(TileEntityItemStackRenderer.instance);
    }

    public class ModeledBlockInventoryRenderer extends TileEntityItemStackRenderer {
        //        private TileHungryChest temc = new TileHungryChest();
        private TileEntityItemStackRenderer chainedTEISR;

        public ModeledBlockInventoryRenderer(TileEntityItemStackRenderer currentTEISR) {
            this.chainedTEISR = currentTEISR;
        }

        public void renderByItem(ItemStack itemStack) {
            Block block = Block.getBlockFromItem(itemStack.getItem());
//            if (block == BlocksElysium.hungryChest) {
//                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.temc, 0.0D, 0.0D, 0.0D, 0.0F);
//            }  else {
//                this.chainedTEISR.renderByItem(itemStack);
//            }

        }
    }

    private static class ItemModelEntry {
        public Item item;
        public int metadata;
        public String name;
        public boolean variant;

        public ItemModelEntry(Item item, int metadata, String name, boolean variant) {
            this.item = item;
            this.metadata = metadata;
            this.name = name;
            this.variant = variant;
        }
    }

    private static class ModelEntry {
        public Block block;
        public int metadata;
        public String name;

        public ModelEntry(Block block, int metadata, String name) {
            this.block = block;
            this.metadata = metadata;
            this.name = name;
        }
    }
}
