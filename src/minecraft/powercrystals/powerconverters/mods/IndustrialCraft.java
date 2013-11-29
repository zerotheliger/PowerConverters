package powercrystals.powerconverters.mods;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.Ic2Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityCharger;
import powercrystals.powerconverters.mods.base.LoaderBase;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.ic2.*;

/**
 * @author samrg472
 */
public final class IndustrialCraft extends LoaderBase {

    public static final IndustrialCraft INSTANCE = new IndustrialCraft();

    public BlockPowerConverterIndustrialCraft converterBlock;
    public PowerSystem powerSystem;

    private IndustrialCraft() {
        super("IC2");
    }

    @Override
    protected void preInit() {
        powerSystem = new PowerSystem("IndustrialCraft", "IC2", 1800, 1800, new String[]{"LV", "MV", "HV", "EV"}, new int[]{32, 128, 512, 2048}, "EU/t");
        PowerSystem.registerPowerSystem(powerSystem);
        TileEntityCharger.registerChargeHandler(new ChargeHandlerIndustrialCraft());
    }

    @Override
    protected void init() {
        converterBlock = new BlockPowerConverterIndustrialCraft(PowerConverterCore.blockIdIndustrialCraft);
        GameRegistry.registerBlock(converterBlock, ItemBlockPowerConverterIndustrialCraft.class, converterBlock.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileEntityIndustrialCraftConsumer.class, "powerConverterIC2Consumer");
        GameRegistry.registerTileEntity(TileEntityIndustrialCraftProducer.class, "powerConverterIC2Producer");
    }

    @Override
    protected void postInit() {
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 1), new ItemStack(converterBlock, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 0), new ItemStack(converterBlock, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 3), new ItemStack(converterBlock, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 2), new ItemStack(converterBlock, 1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 5), new ItemStack(converterBlock, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 4), new ItemStack(converterBlock, 1, 5));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 7), new ItemStack(converterBlock, 1, 6));
        GameRegistry.addShapelessRecipe(new ItemStack(converterBlock, 1, 6), new ItemStack(converterBlock, 1, 7));
        GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 0),
                "G G", " T ", "G G",
                'G', Item.ingotGold,
                'T', Ic2Items.lvTransformer);
        GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 2),
                "G G", " T ", "G G",
                'G', Item.ingotGold,
                'T', Ic2Items.mvTransformer);
        GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 4),
                "G G", " T ", "G G",
                'G', Item.ingotGold,
                'T', Ic2Items.hvTransformer);
        GameRegistry.addRecipe(new ItemStack(converterBlock, 1, 6),
                "G G", " T ", "G G",
                'G', Item.ingotGold,
                'T', Ic2Items.mfsUnit);
    }
}
