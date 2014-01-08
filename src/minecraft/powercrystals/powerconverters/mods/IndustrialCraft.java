package powercrystals.powerconverters.mods;

import ic2.api.item.Items;
import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityCharger;
import powercrystals.powerconverters.mods.base.LoaderBase;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.ic2.BlockPowerConverterIndustrialCraft;
import powercrystals.powerconverters.power.ic2.ChargeHandlerIndustrialCraft;
import powercrystals.powerconverters.power.ic2.ItemBlockPowerConverterIndustrialCraft;
import powercrystals.powerconverters.power.ic2.TileEntityIndustrialCraftConsumer;
import powercrystals.powerconverters.power.ic2.TileEntityIndustrialCraftProducer;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author samrg472
 */
public final class IndustrialCraft extends LoaderBase
{

    public static final IndustrialCraft INSTANCE = new IndustrialCraft();

    public BlockPowerConverterIndustrialCraft converterBlock;
    public PowerSystem powerSystem;

    private IndustrialCraft()
    {
	super("IC2");
    }

    @Override
    protected void preInit()
    {
	powerSystem = new PowerSystem("IndustrialCraft", "IC2", 400, 400,/*1800, 1800,*/new String[] { "LV", "MV", "HV", "EV", "AV" }, new int[] { 32, 128, 512, 2048, 8192 }, "EU/t");
	PowerSystem.registerPowerSystem(powerSystem);
	TileEntityCharger.registerChargeHandler(new ChargeHandlerIndustrialCraft());
    }

    @Override
    protected void init()
    {
	converterBlock = new BlockPowerConverterIndustrialCraft(PowerConverterCore.blockIdIndustrialCraft);
	GameRegistry.registerBlock(converterBlock, ItemBlockPowerConverterIndustrialCraft.class, converterBlock.getUnlocalizedName());
	GameRegistry.registerTileEntity(TileEntityIndustrialCraftConsumer.class, "powerConverterIC2Consumer");
	GameRegistry.registerTileEntity(TileEntityIndustrialCraftProducer.class, "powerConverterIC2Producer");
    }

    @Override
    protected void postInit()
    {
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 1), new ItemStack(converterBlock, 1, 0));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 0), new ItemStack(converterBlock, 1, 1));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 3), new ItemStack(converterBlock, 1, 2));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 2), new ItemStack(converterBlock, 1, 3));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 5), new ItemStack(converterBlock, 1, 4));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 4), new ItemStack(converterBlock, 1, 5));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 7), new ItemStack(converterBlock, 1, 6));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 6), new ItemStack(converterBlock, 1, 7));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 9), new ItemStack(converterBlock, 1, 8));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 8), new ItemStack(converterBlock, 1, 9));
	GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0), "CPC", "PTP", "CPC", 'C', Items.getItem("insulatedTinCableItem"), 'P', Items.getItem("platetin"), 'T', Items.getItem("reBattery"));
	GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 2), "CPC", "PTP", "CPC", 'C', Items.getItem("insulatedCopperCableItem"), 'P', Items.getItem("platecopper"), 'T', Items.getItem("lvTransformer"));
	GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 4), "CPC", "PTP", "CPC", 'C', Items.getItem("insulatedGoldCableItem"), 'P', Items.getItem("plategold"), 'T', Items.getItem("mvTransformer"));
	GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 6), "CPC", "PTP", "CPC", 'C', Items.getItem("insulatedIronCableItem"), 'P', Items.getItem("plateiron"), 'T', Items.getItem("hvTransformer"));
	GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 8), "CPC", "PTP", "CPC", 'C', Items.getItem("glassFiberCableItem"), 'P', Items.getItem("platelapi"), 'T', Items.getItem("evTransformer"));
	//GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0), "G G", " T ", "G G", 'G', Item.ingotGold, 'T', Items.getItem("lvTransformer"));
	//GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 2), "G G", " T ", "G G", 'G', Item.ingotGold, 'T', Items.getItem("mvTransformer"));
	//GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 4), "G G", " T ", "G G", 'G', Item.ingotGold, 'T', Items.getItem("hvTransformer"));
	//GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 6), "G G", " T ", "G G", 'G', Item.ingotGold, 'T', Items.getItem("evTransformer"));
    }
}
