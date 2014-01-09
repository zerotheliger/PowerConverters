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
	powerSystem = new PowerSystem("BuildCraft", "BC", 1000, 1000,/*4375, 4375*/null, null, "MJ/t");
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
	} catch (Throwable t)
	{
	    t.printStackTrace(System.err);
	}
    }
}
