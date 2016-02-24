package elysium.common;

import elysium.common.blocks.RegBlocks;
import elysium.common.config.Config;
import elysium.common.items.RegItems;
import elysium.common.lib.CreativeTabElysium;
import elysium.common.lib.world.biomes.BiomeHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by dawar on 2016. 02. 01..
 */

@Mod(
        modid = Elysium.MODID,
        name = Elysium.MODNAME,
        version = Elysium.VERSION,
        guiFactory = "elysium.client.ElysiumGuiFactory"
//        ,dependencies = "required-after:Forge@[11.15.0.1684,)"
)
public class Elysium {
    public static final String MODID = "elysium";
    public static final String MODNAME = "Elysium";
    public static final String VERSION = "2.0.0";
    @SidedProxy(
            clientSide = "elysium.client.ClientProxy",
            serverSide = "elysium.common.CommonProxy"
    )
    public static CommonProxy proxy;
    @Instance(Elysium.MODID)
    public static Elysium instance;
    //    public WorldEvents worldEvents;
//    public ChunkEvents chunkEvents;
//    public ServerTickEventsFML serverTickEvents;
//    public CraftingEvents craftingEvents;
//    public EventHandlerNetwork networkEventHandler;
//    public ThaumcraftWorldGenerator worldGen;
//    public PlayerEvents playerEvents;
//    public EntityEvents entityEvents;
//    public static ModelRegistrationHelper modelRegistrationHelper;
    public File modDir;
    public static final Logger log = LogManager.getLogger("ELYSIUM");
    //    public static boolean isHalloween = false;
    public static CreativeTabs tabElysium = new CreativeTabElysium(CreativeTabs.getNextID(), "elysium");

    public Elysium() {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        event.getModMetadata().version = Elysium.VERSION;
        this.modDir = event.getModConfigurationDirectory();

        try {
            Config.initialize(event.getSuggestedConfigurationFile());
        } catch (Exception var6) {
            log.error("Elysium has a problem loading its configuration");
        } finally {
            if (Config.config != null) {
                Config.save();
            }

        }

//        ThaumcraftApi.internalMethods = new InternalMethodHandler();
//        this.worldEvents = new WorldEvents();
//        this.chunkEvents = new ChunkEvents();
//        this.serverTickEvents = new ServerTickEventsFML();
//        this.craftingEvents = new CraftingEvents();
//        this.playerEvents = new PlayerEvents();
//        this.entityEvents = new EntityEvents();
//        PacketHandler.init();
//        this.networkEventHandler = new EventHandlerNetwork();
//        MinecraftForge.EVENT_BUS.register(this.networkEventHandler);
//        MinecraftForge.EVENT_BUS.register(this.serverTickEvents);
//        MinecraftForge.EVENT_BUS.register(this.worldEvents);
//        MinecraftForge.TERRAIN_GEN_BUS.register(this.worldEvents);
//        MinecraftForge.EVENT_BUS.register(this.chunkEvents);
//        MinecraftForge.EVENT_BUS.register(this.playerEvents);
//        MinecraftForge.EVENT_BUS.register(this.entityEvents);
//        GameRegistry.registerFuelHandler(this.craftingEvents);
//        MinecraftForge.EVENT_BUS.register(this.craftingEvents);
//        GameRegistry.registerWorldGenerator(this.worldGen = new ThaumcraftWorldGenerator(), 0);
        Config.save();
        RegBlocks.init();
        RegItems.preInit();
//        OreDictionaryEntries.initializeOreDictionary();
        proxy.registerHandlers();
//        this.worldGen.initialize();
        MinecraftForge.EVENT_BUS.register(instance);
        BiomeHandler.registerBiomes();
        proxy.registerDisplayInformationPreInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {/*
        RegEntities.init();*/
        RegItems.init();
        proxy.registerDisplayInformationInit();
//        BlockDispenser.dispenseBehaviorRegistry.putObject(ItemsTC.alumentum, new BehaviorDispenseAlumetum());
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.registerKeyBindings();
//        DimensionManager.registerProviderType(Config.dimensionElysiumId, WorldProviderElysium.class, false);
//        DimensionManager.registerDimension(Config.dimensionElysiumId, Config.dimensionElysiumId);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        Config.initModCompatibility();

        RegItems.postInit();

    }

//    @Mod.EventHandler
//    public void serverLoad(FMLServerStartingEvent event) {
//        event.registerServerCommand(new CommandThaumcraft());
//    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equals(Elysium.MODID)) {
            Config.syncConfigurable();
            if (Config.config != null && Config.config.hasChanged()) {
                Config.save();
            }
        }

    }
}
