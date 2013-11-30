package powercrystals.powerconverters.power.buildcraft;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.mods.BuildCraft;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.Map;

public class TileEntityBuildCraftProducer extends TileEntityEnergyProducer<IPowerReceptor> implements IPipeConnection, IPowerEmitter {

    public TileEntityBuildCraftProducer() {
        super(BuildCraft.INSTANCE.powerSystem, 0, IPowerReceptor.class);
    }

    @Override
    public double produceEnergy(double energy) {

        float mj = (float) (energy / getPowerSystem().getInternalEnergyPerOutput());

        for (Map.Entry<ForgeDirection, IPowerReceptor> output : getTiles().entrySet()) {
            PowerHandler.PowerReceiver pp = output.getValue().getPowerReceiver(output.getKey().getOpposite());
            if (pp != null && pp.getMinEnergyReceived() <= mj) {
                float mjUsed = Math.min(mj, pp.powerRequest());
                mjUsed = pp.receiveEnergy(PowerHandler.Type.STORAGE, mjUsed, output.getKey().getOpposite());

                mj -= mjUsed;
                if (mj <= 0)
                    return 0;

            }
        }
        return mj * getPowerSystem().getInternalEnergyPerOutput();
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
