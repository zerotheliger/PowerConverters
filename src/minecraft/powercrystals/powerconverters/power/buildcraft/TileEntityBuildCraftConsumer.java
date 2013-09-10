package powercrystals.powerconverters.power.buildcraft;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyConsumer;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.IPowerReceptor;

public class TileEntityBuildCraftConsumer extends TileEntityEnergyConsumer<IPowerReceptor> implements IPowerReceptor, IPowerEmitter, IPipeConnection
{
	private PowerHandler _powerProvider;
	private int _mjLastTick = 0;
	private long _lastTickInjected;
	
	public TileEntityBuildCraftConsumer()
	{
		super(PowerConverterCore.powerSystemBuildCraft, 0, IPowerReceptor.class);
		_powerProvider = new PowerHandler(this, PowerHandler.Type.MACHINE);
		_powerProvider.configure(2, 100, 1, 1000); // 25 latency
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(worldObj.getWorldTime() - _lastTickInjected > 1)
		{
			_lastTickInjected = worldObj.getWorldTime();
			_mjLastTick = 0;
		}
        receiveEnergy(10);
	}
	
	public void receiveEnergy(float energy)
	{
		if(_lastTickInjected != worldObj.getWorldTime())
		{
			_lastTickInjected = worldObj.getWorldTime();
			_mjLastTick = 0;
		}
		
		_mjLastTick += MathHelper.floor_float(energy);
        int used = (int)(energy * PowerConverterCore.powerSystemBuildCraft.getInternalEnergyPerInput());
        float consumed;
		if ((consumed = _powerProvider.useEnergy(used, used, true)) > 0) {
            storeEnergy(consumed);
        }
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

    /*
	public int powerRequest(ForgeDirection from)
	{
		return getTotalEnergyDemand() / PowerConverterCore.powerSystemBuildCraft.getInternalEnergyPerInput();
	}
	*/

	@Override
	public double getInputRate()
	{
		return _mjLastTick;
	}

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public ConnectOverride overridePipeConnection(IPipeTile.PipeType pipeType, ForgeDirection direction) {
        return pipeType == IPipeTile.PipeType.POWER ? ConnectOverride.DEFAULT : ConnectOverride.DISCONNECT;
    }

    @Override
    public boolean canEmitPowerFrom(ForgeDirection direction) {
        return true;
    }
}
