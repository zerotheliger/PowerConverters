package powercrystals.powerconverters.power.railcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;
import powercrystals.core.position.BlockPosition;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

public class TileEntityRailCraftProducer extends TileEntityEnergyProducer<IFluidContainerItem> implements IFluidContainerItem
{
	private FluidTank _tank;
	
	public TileEntityRailCraftProducer()
	{
		super(PowerConverterCore.powerSystemSteam, 0, IFluidContainerItem.class);
		_tank = new FluidTank(1);
	}
	
	@Override
	public double produceEnergy(double energy)
	{
		double steam = Math.min(energy / PowerConverterCore.powerSystemSteam.getInternalEnergyPerOutput(), PowerConverterCore.throttleSteamProducer.getInt());
		for(int i = 0; i < 6; i++)
		{
			BlockPosition bp = new BlockPosition(this);
			bp.orientation = ForgeDirection.getOrientation(i);
			bp.moveForwards(1);
			TileEntity te = worldObj.getBlockTileEntity(bp.x, bp.y, bp.z);
			
			if(te != null && te instanceof IFluidContainerItem)
			{
				steam -= ((IFluidContainerItem)te).fill(null, FluidRegistry.getFluidStack(FluidRegistry.getFluidName(PowerConverterCore.steamId), (int) steam), true);
			}
			if(steam <= 0)
			{
				return 0;
			}
		}

		return steam * PowerConverterCore.powerSystemSteam.getInternalEnergyPerOutput();
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		return null;
	}

    @Override
    public int getCapacity(ItemStack container) {
        return _tank.getCapacity();
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return _tank.getFluid();
    }
}
