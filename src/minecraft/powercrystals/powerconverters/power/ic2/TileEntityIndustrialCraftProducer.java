package powercrystals.powerconverters.power.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.core.position.BlockPosition;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.List;

public class TileEntityIndustrialCraftProducer extends TileEntityEnergyProducer<IEnergyAcceptor> implements IEnergySource
{
    private final double maxEnergy;
    private double energy;
	private boolean _isAddedToEnergyNet;
	private boolean _didFirstAddToNet;
	
	private int _packetCount;
	
	@SuppressWarnings("UnusedDeclaration")
    public TileEntityIndustrialCraftProducer()
	{
		this(0);
	}
	
	public TileEntityIndustrialCraftProducer(int voltageIndex)
	{
		super(PowerConverterCore.powerSystemIndustrialCraft, voltageIndex, IEnergyAcceptor.class);
		if(voltageIndex == 0)
		{
			_packetCount = PowerConverterCore.throttleIC2LVProducer.getInt();
            maxEnergy = 32;
		}
		else if(voltageIndex == 1)
		{
			_packetCount = PowerConverterCore.throttleIC2MVProducer.getInt();
            maxEnergy = 128;
		}
		else if(voltageIndex == 2)
		{
			_packetCount = PowerConverterCore.throttleIC2HVProducer.getInt();
            maxEnergy = 512;
		}
		else if(voltageIndex == 3)
		{
			_packetCount = PowerConverterCore.throttleIC2EVProducer.getInt();
            maxEnergy = 2048;
		} else {
            maxEnergy = 0;
        }
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!_didFirstAddToNet && !worldObj.isRemote)
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			_didFirstAddToNet = true;
			_isAddedToEnergyNet = true;
		}
	}
	
	@Override
	public void validate()
	{
		super.validate();
		if(!_isAddedToEnergyNet)
		{
			_didFirstAddToNet = false;
		}
	}
	
	@Override
	public void invalidate()
	{
		if(_isAddedToEnergyNet)
		{
			if(!worldObj.isRemote)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
			_isAddedToEnergyNet = false;
		}
		super.invalidate();
	}
	
	@Override
	public double produceEnergy(double _energy) {
		if(!_isAddedToEnergyNet)
			return _energy;
		
		double eu = _energy / PowerConverterCore.powerSystemIndustrialCraft.getInternalEnergyPerOutput();
		for(int i = 0; i < _packetCount; i++)
		{
            if (energy >= maxEnergy)
                break;
			double producedEu = Math.min(eu, getMaxEnergyOutput());
            _energy += producedEu;
            eu -= producedEu;
            if ((eu < getMaxEnergyOutput()))
                break;
		}
        if (energy < maxEnergy)
		    energy = eu * PowerConverterCore.powerSystemIndustrialCraft.getInternalEnergyPerOutput();
        if (energy > 0) {
            List<BlockPosition> positions = new BlockPosition(xCoord, yCoord, zCoord).getAdjacent(true);
            for (BlockPosition p : positions) {
                TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
                if ((te instanceof IEnergySink) && !(te instanceof TileEntityIndustrialCraftConsumer)) {
                    IEnergySink sink = (IEnergySink) te;
                    double demands = sink.getMaxSafeInput();
                    if (energy < demands)
                        demands = energy;

                    double leftOver = sink.injectEnergyUnits(p.orientation, demands);
                    energy = 0;
                    energy += leftOver;
                    if (energy <= 0)
                        break; // no more energy to give, so stop scanning
                }
            }
        }
		return this.energy;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return true;
	}

	//@Override
	public int getMaxEnergyOutput()
	{
		return getPowerSystem().getVoltageValues()[getVoltageIndex()];
	}

    /**
     * Energy output provided by the source this tick.
     * This is typically Math.min(stored energy, max output/tick).
     *
     * @return Energy offered this tick
     */
    @Override
    public double getOfferedEnergy() {
        return Math.min(energy, maxEnergy);
    }

    /**
     * Draw energy from this source's buffer.
     * <p/>
     * If the source doesn't have a buffer, this is a no-op.
     *
     * @param amount amount of EU to draw, may be negative
     */
    @Override
    public void drawEnergy(double amount) {
        energy -= amount;
    }
}