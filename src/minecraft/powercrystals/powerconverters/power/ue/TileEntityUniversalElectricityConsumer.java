package powercrystals.powerconverters.power.ue;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyConsumer;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.electricity.ElectricityPack;

public class TileEntityUniversalElectricityConsumer extends TileEntityEnergyConsumer<IConductor> implements IConnector, IElectrical
{
	private float _wattsLastTick;
	
	public TileEntityUniversalElectricityConsumer()
	{
		this(0);
	}
	
	public TileEntityUniversalElectricityConsumer(int voltageIndex)
	{
		super(PowerConverterCore.powerSystemUniversalElectricity, voltageIndex, IConductor.class);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(!worldObj.isRemote)
		{
			int desiredWatts = getTotalEnergyDemand() / PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerInput();
		
			ElectricityPack powerRequested = new ElectricityPack(desiredWatts / getVoltage(), getVoltage());
			//ElectricityPack powerPack = ElectricityNetworkHelper.consumeFromMultipleSides(this, powerRequested);
			double watts = powerRequested.getWatts();
		
			storeEnergy(MathHelper.floor_double(watts * PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerInput()));
			_wattsLastTick = (int)watts;
		}
	}

    /**
     * Adds electricity to an block. Returns the quantity of electricity that was accepted. This
     * should always return 0 if the block cannot be externally charged.
     *
     * @param from      Orientation the electricity is sent in from.
     * @param receive   Maximum amount of electricity to be sent into the block.
     * @param doReceive If false, the charge will only be simulated.
     * @return Amount of energy that was accepted by the block.
     */
    @Override
    public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive) {
        float energy = receive.getWatts() / PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerInput();
        if (doReceive)
            _wattsLastTick = energy;
        return energy;
    }

    /**
     * Adds electricity to an block. Returns the ElectricityPack, the electricity provided. This
     * should always return null if the block cannot be externally discharged.
     *
     * @param from      Orientation the electricity is requested from.
     * @param energy    Maximum amount of energy to be sent into the block.
     * @param doReceive If false, the charge will only be simulated.
     * @return Amount of energy that was given out by the block.
     */
    @Override
    public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide) {
        return null;
    }

    /**
     * @return How much energy does this TileEntity want?
     */
    @Override
    public float getRequest(ForgeDirection direction) {
        if (_wattsLastTick < getTotalEnergyDemand())
            return 100;
        return 0;
    }

    /**
     * @return How much energy does this TileEntity want to provide?
     */
    @Override
    public float getProvide(ForgeDirection direction) {
        return 0;
    }

    @Override /* IVoltage */
	public float getVoltage()
	{
		return getPowerSystem().getVoltageValues()[getVoltageIndex()];
	}

	@Override
	public double getInputRate()
	{
		return _wattsLastTick;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}
}
