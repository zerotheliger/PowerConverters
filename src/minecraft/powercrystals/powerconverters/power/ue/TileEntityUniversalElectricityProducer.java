package powercrystals.powerconverters.power.ue;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.electricity.ElectricityPack;

public class TileEntityUniversalElectricityProducer extends TileEntityEnergyProducer<IConductor> implements IConnector, IElectrical
{
    private float energy;

	public TileEntityUniversalElectricityProducer()
	{
		this(0);
	}
	
	public TileEntityUniversalElectricityProducer(int voltageIndex)
	{
		super(PowerConverterCore.powerSystemUniversalElectricity, voltageIndex, IConductor.class);
	}

	@Override
	public double produceEnergy(double energy)
	{
		double watts = energy / PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerOutput();
		//ElectricityPack powerRemaining = ElectricityNetworkHelper.produceFromMultipleSides(this, new ElectricityPack((float) (watts / getVoltage()), (float) getVoltage()));
		this.energy = MathHelper.floor_double(watts * PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerOutput());
	    return (int) this.energy;
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
        return 0;
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
        float watts = request.getWatts() / PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerOutput();
        float _energy = ElectricityPack.getFromWatts(watts, getVoltage()).getWatts();
        if (_energy > energy)
            _energy = energy;
        if (doProvide)
            energy -= _energy;
        return ElectricityPack.getFromWatts(_energy, getVoltage());
        //ElectricityPack powerRemaining = ElectricityNetworkHelper.produceFromMultipleSides(this, new ElectricityPack((float) (watts / getVoltage()), (float) getVoltage()));
        //return MathHelper.floor_double(watts * PowerConverterCore.powerSystemUniversalElectricity.getInternalEnergyPerOutput());
    }

    /**
     * @return How much energy does this TileEntity want?
     */
    @Override
    public float getRequest(ForgeDirection direction) {
        return 0;
    }

    /**
     * @return How much energy does this TileEntity want to provide?
     */
    @Override
    public float getProvide(ForgeDirection direction) {
        return energy;
    }

    @Override
	public float getVoltage()
	{
		return getPowerSystem().getVoltageValues()[getVoltageIndex()];
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}
}