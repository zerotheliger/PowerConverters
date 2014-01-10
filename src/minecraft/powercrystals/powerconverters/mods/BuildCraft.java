package powercrystals.powerconverters.mods;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.mods.base.LoaderBase;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.buildcraft.BlockPowerConverterBuildCraft;
import powercrystals.powerconverters.power.buildcraft.ItemBlockPowerConverterBuildCraft;
import powercrystals.powerconverters.power.buildcraft.TileEntityBuildCraftConsumer;
import powercrystals.powerconverters.power.buildcraft.TileEntityBuildCraftProducer;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author samrg472
 */
public final class BuildCraft extends LoaderBase
{

    public static final BuildCraft INSTANCE = new BuildCraft();

    public BlockPowerConverterBuildCraft converterBlock;
    public PowerSystem powerSystem;

    private BuildCraft()
    {
	super("BuildCraft|Energy");
    }

    @Override
    protected void preInit()
    {
	powerSystem = new PowerSystem("BuildCraft", "BC", 10000, 10000,/*4375, 4375*/null, null, "MJ/t");
	PowerSystem.registerPowerSystem(powerSystem);
    }

    @Override
    protected void init()
    {
	converterBlock = new BlockPowerConverterBuildCraft(PowerConverterCore.blockIdBuildCraft);
	GameRegistry.registerBlock(converterBlock, ItemBlockPowerConverterBuildCraft.class, converterBlock.getUnlocalizedName());
	GameRegistry.registerTileEntity(TileEntityBuildCraftConsumer.class, "powerConverterBCConsumer");
	GameRegistry.registerTileEntity(TileEntityBuildCraftProducer.class, "powerConverterBCProducer");
    }

    @Override
    protected void postInit()
    {
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 1), new ItemStack(converterBlock, 1, 0));
	GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 0), new ItemStack(converterBlock, 1, 1));
	try
	{//pipePowerGold BuildCraftTransport pipeStructureCobblestone
	    ItemStack cable = new ItemStack((Item) Class.forName("buildcraft.BuildCraftTransport").getField("pipePowerGold").get(null), 1, 0);
	    ItemStack struct = new ItemStack((Item) Class.forName("buildcraft.BuildCraftTransport").getField("pipeStructureCobblestone").get(null), 1, 0);
	    ItemStack gear = new ItemStack((Item) Class.forName("buildcraft.BuildCraftCore").getField("goldGearItem").get(null), 1, 0);
	    ItemStack engine = new ItemStack((Block) Class.forName("buildcraft.BuildCraftEnergy").getField("engineBlock").get(null), 1, 1);
	    GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0), "GSG", "SES", "GSG", 'G', cable, 'S', struct, 'E', gear);
	    ItemStack pump = new ItemStack((Block) Class.forName("buildcraft.BuildCraftFactory").getField("pumpBlock").get(null), 1, 0);

	    ItemStack conduit = new ItemStack((Item) Class.forName("buildcraft.BuildCraftTransport").getField("pipePowerDiamond").get(null), 1, 0);
	    ItemStack chest = new ItemStack((Block) Class.forName("buildcraft.BuildCraftTransport").getField("filteredBufferBlock").get(null), 1, 0);
	    ItemStack fluid = new ItemStack((Item) Class.forName("buildcraft.BuildCraftTransport").getField("pipeFluidsGold").get(null), 1, 0);

	    if (BuildCraft.INSTANCE.powerSystem.getRecipesEnabled())
	    {
		GameRegistry.addRecipe(new ItemStack(PowerConverterCore.converterBlockCommon, 1, 0), "PPP", "PBP", "PPP", 'B', Block.blockRedstone, 'P', conduit);
		GameRegistry.addRecipe(new ItemStack(PowerConverterCore.converterBlockCommon, 1, 2), "TGT", "#S#", "T#T", 'T', cable, 'S', chest, '#', engine, 'G', gear);
		if (PowerConverterCore.powerSystemSteamEnabled)
		    GameRegistry.addRecipe(new ItemStack(PowerConverterCore.converterBlockSteam, 1, 0), "GSG", "SES", "GSG", 'G', fluid, 'S', struct, 'E', pump);
	    }
	} catch (Throwable t)
	{
	    t.printStackTrace(System.err);
	}
    }
}
