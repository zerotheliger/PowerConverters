package powercrystals.powerconverters.power.railcraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyConsumer;

public class TileEntityRailCraftConsumer extends TileEntityEnergyConsumer<IFluidContainerItem> implements IFluidContainerItem
{
	private FluidTank _steamTank;
	private int _mBLastTick;
	
	public TileEntityRailCraftConsumer()
	{
		super(PowerConverterCore.powerSystemSteam, 0, IFluidContainerItem.class);
		_steamTank = new FluidTank(1 * FluidContainerRegistry.BUCKET_VOLUME);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(_steamTank != null && _steamTank.getFluid() != null)
		{
			int amount = Math.min(_steamTank.getFluidAmount(), PowerConverterCore.throttleSteamConsumer.getInt());
			int energy = amount * PowerConverterCore.powerSystemSteam.getInternalEnergyPerInput();
			energy = (int) storeEnergy(energy);
			int toDrain = amount - (energy / PowerConverterCore.powerSystemSteam.getInternalEnergyPerInput());
			_steamTank.drain(toDrain, true);
			_mBLastTick = toDrain;
		}
		else
		{
			_mBLastTick = 0;
		}
	}
	
	@Override
	public int getVoltageIndex()
	{
		return 0;
	}
	
	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
        return _steamTank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		return null;
	}

    @Override
    public int getCapacity(ItemStack container) {
        return _steamTank.getCapacity();
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return _steamTank.getFluid();
    }

	@Override
	public double getInputRate()
	{
		return _mBLastTick;
	}
}
