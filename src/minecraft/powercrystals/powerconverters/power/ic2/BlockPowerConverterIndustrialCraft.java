package powercrystals.powerconverters.power.ic2;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.gui.PCCreativeTab;
import powercrystals.powerconverters.power.BlockPowerConverter;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerConverterIndustrialCraft extends BlockPowerConverter
{
	public BlockPowerConverterIndustrialCraft(int blockId)
	{
		super(blockId, 8);
		setUnlocalizedName("powerconverters.ic2");
		setCreativeTab(PCCreativeTab.tab);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if(metadata == 0) return new TileEntityIndustrialCraftConsumer(0);
		else if(metadata == 1) return new TileEntityIndustrialCraftProducer(0);
		else if(metadata == 2) return new TileEntityIndustrialCraftConsumer(1);
		else if(metadata == 3) return new TileEntityIndustrialCraftProducer(1);
		else if(metadata == 4) return new TileEntityIndustrialCraftConsumer(2);
		else if(metadata == 5) return new TileEntityIndustrialCraftProducer(2);
		else if(metadata == 6) return new TileEntityIndustrialCraftConsumer(3);
		else if(metadata == 7) return new TileEntityIndustrialCraftProducer(3);
		
		return createNewTileEntity(world);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		_icons[0] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".lv.consumer.off");
		_icons[1] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".lv.consumer.on");
		_icons[2] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".lv.producer.off");
		_icons[3] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".lv.producer.on");
		_icons[4] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".mv.consumer.off");
		_icons[5] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".mv.consumer.on");
		_icons[6] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".mv.producer.off");
		_icons[7] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".mv.producer.on");
		_icons[8] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".hv.consumer.off");
		_icons[9] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".hv.consumer.on");
		_icons[10] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".hv.producer.off");
		_icons[11] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".hv.producer.on");
		_icons[12] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".ev.consumer.off");
		_icons[13] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".ev.consumer.on");
		_icons[14] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".ev.producer.off");
		_icons[15] = ir.registerIcon(PowerConverterCore.texturesFolder + getUnlocalizedName() + ".ev.producer.on");
	}
}
