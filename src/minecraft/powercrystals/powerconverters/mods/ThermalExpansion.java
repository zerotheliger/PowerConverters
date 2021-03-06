package powercrystals.powerconverters.mods;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityCharger;
import powercrystals.powerconverters.mods.base.LoaderBase;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.thermalexpansion.BlockPowerConverterRF;
import powercrystals.powerconverters.power.thermalexpansion.ChargeHandlerRF;
import powercrystals.powerconverters.power.thermalexpansion.ItemBlockPowerConverterRF;
import powercrystals.powerconverters.power.thermalexpansion.TileEntityRFConsumer;
import powercrystals.powerconverters.power.thermalexpansion.TileEntityRFProducer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author samrg472
 */
public final class ThermalExpansion extends LoaderBase
{

    public static final ThermalExpansion INSTANCE = new ThermalExpansion();

    public BlockPowerConverterRF converterBlock;
    public PowerSystem powerSystem;

    private ThermalExpansion()
    {
	super("ThermalExpansion");
    }

    @Override
    protected void preInit()
    {
	powerSystem = new PowerSystem("Thermal Expansion", "RF", /*437.5F, 437.5F*/1000, 1000, null, null, "RF/t");
	PowerSystem.registerPowerSystem(powerSystem);
	TileEntityCharger.registerChargeHandler(new ChargeHandlerRF());
    }

    @Override
    protected void init()
    {
	converterBlock = new BlockPowerConverterRF(PowerConverterCore.blockIdThermalExpansion);
	GameRegistry.registerBlock(converterBlock, ItemBlockPowerConverterRF.class, converterBlock.getUnlocalizedName());
	GameRegistry.registerTileEntity(TileEntityRFConsumer.class, "powerConverterRFConsumer");
	GameRegistry.registerTileEntity(TileEntityRFProducer.class, "powerConverterRFProducer");
    }

    @Override
    protected void postInit()
    {
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 1), new ItemStack(converterBlock, 1, 0));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 0), new ItemStack(converterBlock, 1, 1));
	if (Loader.isModLoaded("ThermalExpansion"))
	{
	    ItemStack cell = GameRegistry.findItemStack("ThermalExpansion", "cellBasic", 1);
	    ItemStack conduit = GameRegistry.findItemStack("ThermalExpansion", "conduitEnergyBasic", 1);
	    ItemStack transmit = GameRegistry.findItemStack("ThermalExpansion", "powerCoilSilver", 1);
	    ItemStack recieve = GameRegistry.findItemStack("ThermalExpansion", "powerCoilGold", 1);

	    GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0), "CTC", "RSR", "CTC", 'S', cell, 'C', conduit, 'R', recieve, 'T', transmit);

	    ItemStack fluid = GameRegistry.findItemStack("ThermalExpansion", "conduitFluidOpaque", 1);
	    ItemStack tank = GameRegistry.findItemStack("ThermalExpansion", "tankBasic", 1);
	    ItemStack frame = GameRegistry.findItemStack("ThermalExpansion", "machineFrame", 1);

	    ItemStack storage = GameRegistry.findItemStack("ThermalExpansion", "cellBasic", 1);
	    ItemStack charger = GameRegistry.findItemStack("ThermalExpansion", "charger", 1);
	    ItemStack hconduit = GameRegistry.findItemStack("ThermalExpansion", "conduitEnergyHardened", 1);
	    if (ThermalExpansion.INSTANCE.powerSystem.getRecipesEnabled())
	    {
		GameRegistry.addRecipe(new ItemStack(PowerConverterCore.converterBlockCommon, 1, 0), " T ", "SDS", " T ", 'T', transmit, 'S', storage, 'D', Item.diamond);
		GameRegistry.addRecipe(new ItemStack(PowerConverterCore.converterBlockCommon, 1, 2), "T#T", "CSC", "TCT", 'T', transmit, 'C', hconduit, 'S', Block.chest, '#', charger);
		if (PowerConverterCore.powerSystemSteamEnabled)
		    GameRegistry.addRecipe(new ItemStack(PowerConverterCore.converterBlockSteam, 1, 0), "CTC", "TST", "CTC", 'S', tank, 'C', fluid, 'T', frame);
	    }
	}
    }
}
