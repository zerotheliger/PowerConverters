package powercrystals.powerconverters.power.buildcraft;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.mods.BuildCraft;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.Map.Entry;

public class TileEntityBuildCraftProducer extends TileEntityEnergyProducer<IPowerReceptor> implements IPipeConnection, IPowerEmitter {

    public TileEntityBuildCraftProducer() {
        super(BuildCraft.INSTANCE.powerSystem, 0, IPowerReceptor.class);
    }

    @Override
    public double produceEnergy(double energy) {
        double mj = energy / getPowerSystem().getInternalEnergyPerOutput();

        for (Entry<ForgeDirection, IPowerReceptor> output : getTiles().entrySet()) {
            PowerReceiver pp = output.getValue().getPowerReceiver(output.getKey());
            if (pp != null && pp.getMinEnergyReceived() <= mj) {
                float mjUsed = (float) Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int) Math.floor(pp.getEnergyStored()));
                pp.receiveEnergy(PowerHandler.Type.MACHINE, mjUsed, output.getKey());

                mj -= mjUsed;
                if (mj <= 0) {
                    return 0;
                }
            }
        }
        return mj * getPowerSystem().getInternalEnergyPerOutput();
    }

    @Override
    public ConnectOverride overridePipeConnection(IPipeTile.PipeType pipeType, ForgeDirection direction) {
        return pipeType == IPipeTile.PipeType.POWER ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT;
    }

    @Override
    public boolean canEmitPowerFrom(ForgeDirection direction) {
        return true;
    }
}
