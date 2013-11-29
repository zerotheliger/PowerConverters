package powercrystals.powerconverters.mods;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityCharger;
import powercrystals.powerconverters.mods.base.LoaderBase;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.thermalexpansion.*;
import thermalexpansion.block.TEBlocks;

/**
 * @author samrg472
 */
public final class ThermalExpansion extends LoaderBase {

    public static final ThermalExpansion INSTANCE = new ThermalExpansion();

    public BlockPowerConverterRF converterBlock;
    public PowerSystem powerSystem;

    private ThermalExpansion() {
        super("ThermalExpansion");
    }

    @Override
    protected void preInit() {
        powerSystem = new PowerSystem("Thermal Expansion", "RF", 437.5F, 437.5F, null, null, "RF/t");
        PowerSystem.registerPowerSystem(powerSystem);
        TileEntityCharger.registerChargeHandler(new ChargeHandlerRF());
    }

    @Override
    protected void init() {
        converterBlock = new BlockPowerConverterRF(PowerConverterCore.blockIdThermalExpansion);
        GameRegistry.registerBlock(converterBlock, ItemBlockPowerConverterRF.class, converterBlock.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityRFConsumer.class, "powerConverterRFConsumer");
        GameRegistry.registerTileEntity(TileEntityRFProducer.class, "powerConverterRFProducer");
    }

    @Override
    protected void postInit() {
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 1), new ItemStack(converterBlock, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 0), new ItemStack(converterBlock, 1, 1));
        GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0),
                "G G", " T ", "G G",
                'G', Item.ingotGold,
                'T', TEBlocks.blockDynamo);
    }
}
