package powercrystals.powerconverters.power.thermalexpansion;

import cofh.api.energy.IEnergyHandler;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.mods.ThermalExpansion;
import powercrystals.powerconverters.power.TileEntityEnergyConsumer;

/**
 * @author samrg472
 */
public class TileEntityRFConsumer extends TileEntityEnergyConsumer<IEnergyHandler> implements IEnergyHandler {

    private int lastReceivedRF;

    public TileEntityRFConsumer() {
        super(ThermalExpansion.INSTANCE.powerSystem, 0, IEnergyHandler.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public double getInputRate() {
        int last = lastReceivedRF;
        lastReceivedRF = 0;
        return last;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        TileEntityEnergyBridge bridge = getFirstBridge();
        if (bridge == null)
            return 0;
        float energyToReceive = getPowerSystem().getInternalEnergyPerInput() * maxReceive;
        int received = (int) (energyToReceive - storeEnergy(energyToReceive, simulate));
        if (!simulate) {
            lastReceivedRF = (int) (received / getPowerSystem().getInternalEnergyPerInput());
            return lastReceivedRF;
        }
        return (int) (received / getPowerSystem().getInternalEnergyPerInput());
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        TileEntityEnergyBridge bridge = getFirstBridge();
        if (bridge == null)
            return 0;
        return (int) (bridge.getEnergyStored() / getPowerSystem().getInternalEnergyPerInput());
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        TileEntityEnergyBridge bridge = getFirstBridge();
        if (bridge == null)
            return 0;
        return (int) (bridge.getEnergyStoredMax() / getPowerSystem().getInternalEnergyPerInput());
    }
}
