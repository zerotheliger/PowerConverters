package powercrystals.powerconverters.mods;

import cpw.mods.fml.common.registry.GameRegistry;
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

/**
 * @author samrg472
 */
public final class BuildCraft extends LoaderBase {

    public static final BuildCraft INSTANCE = new BuildCraft();

    public BlockPowerConverterBuildCraft converterBlock;
    public PowerSystem powerSystem;

    private BuildCraft() {
        super("BuildCraft|Energy");
    }

    @Override
    protected void preInit() {
        powerSystem = new PowerSystem("BuildCraft", "BC", 4375, 4375, null, null, "MJ/t");
        PowerSystem.registerPowerSystem(powerSystem);
    }

    @Override
    protected void init() {
        converterBlock = new BlockPowerConverterBuildCraft(PowerConverterCore.blockIdBuildCraft);
        GameRegistry.registerBlock(converterBlock, ItemBlockPowerConverterBuildCraft.class, converterBlock.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityBuildCraftConsumer.class, "powerConverterBCConsumer");
        GameRegistry.registerTileEntity(TileEntityBuildCraftProducer.class, "powerConverterBCProducer");
    }

    @Override
    protected void postInit() {
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 1), new ItemStack(converterBlock, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 0), new ItemStack(converterBlock, 1, 1));
        try {
            GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0),
                    "G G", " E ", "G G",
                    'G', Item.ingotGold,
                    'E', new ItemStack((Block) Class.forName("buildcraft.BuildCraftEnergy").getField("engineBlock").get(null), 1, 1));
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
