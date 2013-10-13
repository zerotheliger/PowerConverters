package powercrystals.powerconverters;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import powercrystals.powerconverters.common.BlockPowerConverterCommon;
import powercrystals.powerconverters.common.ItemBlockPowerConverterCommon;
import powercrystals.powerconverters.common.TileEntityCharger;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.gui.PCGUIHandler;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.buildcraft.*;
import powercrystals.powerconverters.power.factorization.BlockPowerConverterFactorization;
import powercrystals.powerconverters.power.factorization.ItemBlockPowerConverterFactorization;
import powercrystals.powerconverters.power.factorization.TileEntityPowerConverterFactorizationConsumer;
import powercrystals.powerconverters.power.factorization.TileEntityPowerConverterFactorizationProducer;
import powercrystals.powerconverters.power.ic2.*;
import powercrystals.powerconverters.power.railcraft.BlockPowerConverterRailCraft;
import powercrystals.powerconverters.power.railcraft.ItemBlockPowerConverterRailCraft;
import powercrystals.powerconverters.power.railcraft.TileEntityRailCraftConsumer;
import powercrystals.powerconverters.power.railcraft.TileEntityRailCraftProducer;

import java.io.File;

@Mod(modid = PowerConverterCore.modId, name = PowerConverterCore.modName, version = PowerConverterCore.version,
        dependencies = "after:BuildCraft|Energy;after:factorization;after:IC2;after:Railcraft;after:ThermalExpansion")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class PowerConverterCore {
    public static final String modId = "PowerConverters";
    public static final String modName = "Power Converters";
    public static final String version = "1.6.4R2.3.2B1";

    public static final String texturesFolder = modId + ":";
    public static final String guiFolder = modId + ":" + "textures/gui/";

    public static Block converterBlockCommon;
    public static Block converterBlockBuildCraft;
    public static Block converterBlockIndustrialCraft;
    public static Block converterBlockSteam;
    public static Block converterBlockFactorization;

    @Mod.Instance(modId)
    public static PowerConverterCore instance;

    private static Property blockIdCommon;
    private static Property blockIdBuildCraft;
    private static Property blockIdIndustrialCraft;
    private static Property blockIdSteam;
    private static Property blockIdFactorization;

    public static Property bridgeBufferSize;

    public static Property throttleSteamConsumer;
    public static Property throttleSteamProducer;

    public static PowerSystem powerSystemBuildCraft;
    public static PowerSystem powerSystemIndustrialCraft;
    public static PowerSystem powerSystemSteam;
    public static PowerSystem powerSystemFactorization;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        powerSystemBuildCraft = new PowerSystem("BuildCraft", "BC", 4375, 4375, null, null, "MJ/t");
        powerSystemIndustrialCraft = new PowerSystem("IndustrialCraft", "IC2", 1800, 1800, new String[]{"LV", "MV", "HV", "EV"}, new int[]{32, 128, 512, 2048}, "EU/t");
        powerSystemSteam = new PowerSystem("Steam", "STEAM", 875, 875, null, null, "mB/t");
        powerSystemFactorization = new PowerSystem("Factorization", "FZ", 175, 175, null, null, "CG/t");

        PowerSystem.registerPowerSystem(powerSystemBuildCraft);
        PowerSystem.registerPowerSystem(powerSystemIndustrialCraft);
        PowerSystem.registerPowerSystem(powerSystemSteam);
        PowerSystem.registerPowerSystem(powerSystemFactorization);

        File dir = evt.getModConfigurationDirectory();
        loadConfig(dir);
        new LangHandler(modId, new File(new File(dir, modId.toLowerCase()), "lang")).init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) throws Exception {
        converterBlockCommon = new BlockPowerConverterCommon(blockIdCommon.getInt());
        GameRegistry.registerBlock(converterBlockCommon, ItemBlockPowerConverterCommon.class, converterBlockCommon.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityEnergyBridge.class, "powerConverterEnergyBridge");
        GameRegistry.registerTileEntity(TileEntityCharger.class, "powerConverterUniversalCharger");

        GameRegistry.addRecipe(new ItemStack(converterBlockCommon, 1, 0),
                "GRG", "LDL", "GRG",
                'G', Item.ingotGold,
                'R', Item.redstone,
                'L', Block.glass,
                'D', Item.diamond);

        GameRegistry.addRecipe(new ItemStack(converterBlockCommon, 1, 2),
                "GRG", "ICI", "GRG",
                'G', Item.ingotGold,
                'R', Item.redstone,
                'I', Item.ingotIron,
                'C', Block.chest);

        if (Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) {
            converterBlockBuildCraft = new BlockPowerConverterBuildCraft(blockIdBuildCraft.getInt());
            GameRegistry.registerBlock(converterBlockBuildCraft, ItemBlockPowerConverterBuildCraft.class, converterBlockBuildCraft.getUnlocalizedName());
            GameRegistry.registerTileEntity(TileEntityBuildCraftConsumer.class, "powerConverterBCConsumer");
            GameRegistry.registerTileEntity(TileEntityBuildCraftProducer.class, "powerConverterBCProducer");

            if (Loader.isModLoaded("BuildCraft|Energy")) {
                GameRegistry.addRecipe(new ItemStack(converterBlockBuildCraft, 1, 0),
                        "G G", " E ", "G G",
                        'G', Item.ingotGold,
                        'E', new ItemStack((Block) (Class.forName("buildcraft.BuildCraftEnergy").getField("engineBlock").get(null)), 1, 1));
            }
            if (Loader.isModLoaded("ThermalExpansion")) {
                GameRegistry.addRecipe(new ItemStack(converterBlockBuildCraft, 1, 0),
                        "G G", " E ", "G G",
                        'G', Item.ingotGold,
                        'E', new ItemStack((Block) (Class.forName("thermalexpansion.block.TEBlocks").getField("blockEngine").get(null)), 1, 1));
                TileEntityCharger.registerChargeHandler(new ChargeHandlerThermalExpansion());
            }

            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockBuildCraft, 1, 1), new ItemStack(converterBlockBuildCraft, 1, 0));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockBuildCraft, 1, 0), new ItemStack(converterBlockBuildCraft, 1, 1));
        }

        if (Loader.isModLoaded("IC2")) {
            converterBlockIndustrialCraft = new BlockPowerConverterIndustrialCraft(blockIdIndustrialCraft.getInt());
            GameRegistry.registerBlock(converterBlockIndustrialCraft, ItemBlockPowerConverterIndustrialCraft.class, converterBlockIndustrialCraft.getUnlocalizedName());
            GameRegistry.registerTileEntity(TileEntityIndustrialCraftConsumer.class, "powerConverterIC2Consumer");
            GameRegistry.registerTileEntity(TileEntityIndustrialCraftProducer.class, "powerConverterIC2Producer");

            GameRegistry.addRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 0),
                    "G G", " T ", "G G",
                    'G', Item.ingotGold,
                    'T', (Class.forName("ic2.core.Ic2Items").getField("lvTransformer").get(null)));
            GameRegistry.addRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 2),
                    "G G", " T ", "G G",
                    'G', Item.ingotGold,
                    'T', (Class.forName("ic2.core.Ic2Items").getField("mvTransformer").get(null)));
            GameRegistry.addRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 4),
                    "G G", " T ", "G G",
                    'G', Item.ingotGold,
                    'T', (Class.forName("ic2.core.Ic2Items").getField("hvTransformer").get(null)));
            GameRegistry.addRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 6),
                    "G G", " T ", "G G",
                    'G', Item.ingotGold,
                    'T', (Class.forName("ic2.core.Ic2Items").getField("mfsUnit").get(null)));

            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 1), new ItemStack(converterBlockIndustrialCraft, 1, 0));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 0), new ItemStack(converterBlockIndustrialCraft, 1, 1));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 3), new ItemStack(converterBlockIndustrialCraft, 1, 2));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 2), new ItemStack(converterBlockIndustrialCraft, 1, 3));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 5), new ItemStack(converterBlockIndustrialCraft, 1, 4));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 4), new ItemStack(converterBlockIndustrialCraft, 1, 5));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 7), new ItemStack(converterBlockIndustrialCraft, 1, 6));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockIndustrialCraft, 1, 6), new ItemStack(converterBlockIndustrialCraft, 1, 7));

            TileEntityCharger.registerChargeHandler(new ChargeHandlerIndustrialCraft());
        }

        if (Loader.isModLoaded("Railcraft") || Loader.isModLoaded("factorization")) {
            converterBlockSteam = new BlockPowerConverterRailCraft(blockIdSteam.getInt());
            GameRegistry.registerBlock(converterBlockSteam, ItemBlockPowerConverterRailCraft.class, converterBlockSteam.getUnlocalizedName());
            GameRegistry.registerTileEntity(TileEntityRailCraftConsumer.class, "powerConverterSteamConsumer");
            GameRegistry.registerTileEntity(TileEntityRailCraftProducer.class, "powerConverterSteamProducer");

            if (Loader.isModLoaded("Railcraft")) {
                GameRegistry.addRecipe(new ItemStack(converterBlockSteam, 1, 0),
                        "G G", " E ", "G G",
                        'G', Item.ingotGold,
                        'E', new ItemStack((Block) (Class.forName("mods.railcraft.common.blocks.RailcraftBlocks").getMethod("getBlockMachineBeta").invoke(null)), 1, 8));
            } else {
                Object fzRegistry = Class.forName("factorization.common.Core").getField("registry").get(null);
                GameRegistry.addRecipe(new ItemStack(converterBlockSteam, 1, 0),
                        "G G", " E ", "G G",
                        'G', Item.ingotGold,
                        'E', (Class.forName("factorization.common.Registry").getField("steamturbine_item").get(fzRegistry)));
            }

            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockSteam, 1, 1), new ItemStack(converterBlockSteam, 1, 0));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockSteam, 1, 0), new ItemStack(converterBlockSteam, 1, 1));
        }

        if (Loader.isModLoaded("factorization")) {
            converterBlockFactorization = new BlockPowerConverterFactorization(blockIdFactorization.getInt());
            GameRegistry.registerBlock(converterBlockFactorization, ItemBlockPowerConverterFactorization.class, converterBlockFactorization.getUnlocalizedName());
            GameRegistry.registerTileEntity(TileEntityPowerConverterFactorizationConsumer.class, "powerConverterFZConsumer");
            GameRegistry.registerTileEntity(TileEntityPowerConverterFactorizationProducer.class, "powerConverterFZProducer");

            Object fzRegistry = Class.forName("factorization.common.Core").getField("registry").get(null);

            GameRegistry.addRecipe(new ItemStack(converterBlockFactorization, 1, 0),
                    "I I", " B ", "I I",
                    'I', Item.ingotGold,
                    'B', (Class.forName("factorization.common.Registry").getField("solarboiler_item").get(fzRegistry)));

            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockFactorization, 1, 1), new ItemStack(converterBlockFactorization, 1, 0));
            GameRegistry.addShapelessRecipe(new ItemStack(converterBlockFactorization, 1, 0), new ItemStack(converterBlockFactorization, 1, 1));
        }

        NetworkRegistry.instance().registerGuiHandler(instance, new PCGUIHandler());
        MinecraftForge.EVENT_BUS.register(instance);
    }

    private static void loadConfig(File dir) {
        dir = new File(new File(dir, modId.toLowerCase()), "common.cfg");
        Configuration c = new Configuration(dir);

        blockIdCommon = c.getBlock("ID.BlockCommon", 2850);
        blockIdBuildCraft = c.getBlock("ID.BlockBuildcraft", 2851);
        blockIdIndustrialCraft = c.getBlock("ID.BlockIndustrialCraft", 2852);
        blockIdSteam = c.getBlock("ID.BlockSteam", 2853);
        blockIdFactorization = c.getBlock("ID.BlockFactorization", 2855);

        bridgeBufferSize = c.get(Configuration.CATEGORY_GENERAL, "BridgeBufferSize", 160000000);

        throttleSteamConsumer = c.get("Throttles", "Steam.Consumer", 1000);
        throttleSteamConsumer.comment = "mB/t";
        throttleSteamProducer = c.get("Throttles", "Steam.Producer", 1000);
        throttleSteamProducer.comment = "mB/t";

        PowerSystem.loadConfig(c);

        c.save();
    }
}
