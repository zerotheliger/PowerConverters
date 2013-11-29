package powercrystals.powerconverters.power.buildcraft;

import buildcraft.api.gates.IAction;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.core.IMachine;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.mods.BuildCraft;
import powercrystals.powerconverters.power.TileEntityEnergyConsumer;

public class TileEntityBuildCraftConsumer extends TileEntityEnergyConsumer<IPowerReceptor> implements IPowerReceptor, IPipeConnection, IMachine {
    private PowerHandler _powerProvider;
    private int _mjLastTick = 0;
    private long _lastTickInjected;

    public TileEntityBuildCraftConsumer() {
        super(BuildCraft.INSTANCE.powerSystem, 0, IPowerReceptor.class);
        _powerProvider = new PowerHandler(this, PowerHandler.Type.MACHINE);
        _powerProvider.configure(0, 100, 0, 1000); // 25 latency
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.getWorldTime() - _lastTickInjected > 1) {
            _lastTickInjected = worldObj.getWorldTime();
            _mjLastTick = 0;
        }
        receiveEnergy();
    }

    public void receiveEnergy() {
        if (_lastTickInjected != worldObj.getWorldTime()) {
            _lastTickInjected = worldObj.getWorldTime();
            _mjLastTick = 0;
        }

        float consumed = _powerProvider.useEnergy(0, _powerProvider.getEnergyStored(), false);
        float energyToUse = consumed * getPowerSystem().getInternalEnergyPerInput();
        float leftOvers = (float) storeEnergy(energyToUse, false);

        float finalConsumption = consumed - (leftOvers * getPowerSystem().getInternalEnergyPerInput());
        if (finalConsumption > 0) {
            _powerProvider.useEnergy(0, finalConsumption, true);
            _mjLastTick = (int) finalConsumption;
        }
    }

    @Override
    public PowerReceiver getPowerReceiver(ForgeDirection dir) {
        return _powerProvider.getPowerReceiver();
    }

    @Override
    public void doWork(PowerHandler handler) {
    }

    @Override
    public double getInputRate() {
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
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean manageFluids() {
        return false;
    }

    @Override
    public boolean manageSolids() {
        return false;
    }

    @Override
    public boolean allowAction(IAction iAction) {
        return false;
    }
}
