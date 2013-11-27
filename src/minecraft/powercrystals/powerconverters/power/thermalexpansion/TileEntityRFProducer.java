package powercrystals.powerconverters.power.thermalexpansion;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.position.BlockPosition;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.List;

/**
 * @author samrg472
 */
public class TileEntityRFProducer extends TileEntityEnergyProducer<IEnergyHandler> implements IEnergyHandler {

    public TileEntityRFProducer() {
        super(PowerConverterCore.powerSystemThermalExpansion, 0, IEnergyHandler.class);
    }

    @Override
    public double produceEnergy(double energy) {
        final double energyToUse = energy / getPowerSystem().getInternalEnergyPerOutput();

        if (energyToUse > 0) {
            List<BlockPosition> positions = new BlockPosition(xCoord, yCoord, zCoord).getAdjacent(true);
            for (BlockPosition p : positions) {
                TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
                if ((te instanceof IEnergyHandler) && !((te instanceof TileEntityRFConsumer) || (te instanceof TileEntityEnergyBridge))) {
                    IEnergyHandler eHandler = (IEnergyHandler) te;
                    final double received = eHandler.receiveEnergy(p.orientation, (int) (energyToUse), false);
                    energy -= received * getPowerSystem().getInternalEnergyPerOutput();
                    if (energy <= 0)
                        break; // no more energy to give, so stop scanning
                }
            }
        }

        return energy;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return 0;
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
