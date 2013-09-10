package powercrystals.powerconverters.power.buildcraft;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;
import buildcraft.api.power.PowerHandler.PowerReceiver;

import java.util.Map.Entry;

public class TileEntityBuildCraftProducer extends TileEntityEnergyProducer<IPowerReceptor> implements IPowerReceptor
{
	private PowerHandler _powerProvider;
	
	public TileEntityBuildCraftProducer()
	{
		super(PowerConverterCore.powerSystemBuildCraft, 0, IPowerReceptor.class);
		_powerProvider = new PowerHandler(this, PowerHandler.Type.ENGINE);
		_powerProvider.configure(0, 0, 0, 0);
	}
	
	@Override
	public double produceEnergy(double energy)
	{
		double mj = energy / PowerConverterCore.powerSystemBuildCraft.getInternalEnergyPerOutput();
		
		for(Entry<ForgeDirection, IPowerReceptor> output : getTiles().entrySet())
		{
			PowerReceiver pp = output.getValue().getPowerReceiver(output.getKey());
			if(pp != null /*&& pp.preConditions(output.getValue())*/ && pp.getMinEnergyReceived() <= mj)
			{
				float mjUsed = (float) Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int)Math.floor(pp.getEnergyStored()));
				pp.receiveEnergy(PowerHandler.Type.MACHINE, mjUsed, output.getKey());
				
				mj -= mjUsed;
				if(mj <= 0)
				{
					return 0;
				}
			}
		}
		return mj * PowerConverterCore.powerSystemBuildCraft.getInternalEnergyPerOutput();
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection dir)
	{
		return _powerProvider.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler handler)
	{
	}

	@Override
	public World getWorld()
	{
		return worldObj;
	}
}
