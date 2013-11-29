package powercrystals.powerconverters.power.railcraft;

import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.common.Optional;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.mods.reference.InterfaceReference;
import powercrystals.powerconverters.mods.reference.ModIDReference;
import powercrystals.powerconverters.power.TileEntityEnergyConsumer;

@Optional.Interface(modid = ModIDReference.BUILDCRAFT, iface = InterfaceReference.BuildCraft.IPipeConnection)
public class TileEntityRailCraftConsumer extends TileEntityEnergyConsumer<IFluidContainerItem> implements IFluidHandler, IPipeConnection {
    private FluidTank _steamTank;
    private int _mBLastTick;

    public TileEntityRailCraftConsumer() {
        super(PowerConverterCore.powerSystemSteam, 0, IFluidContainerItem.class);
        _steamTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (_steamTank.getFluidAmount() > 0) {
            int amount = Math.min(_steamTank.getFluidAmount(), PowerConverterCore.throttleSteamConsumer);
            float energy = amount * PowerConverterCore.powerSystemSteam.getInternalEnergyPerInput();
            energy = (int) storeEnergy(energy, false);
            int toDrain = (int) (amount - (energy / PowerConverterCore.powerSystemSteam.getInternalEnergyPerInput()));
            _steamTank.drain(toDrain, true);
            _mBLastTick = toDrain;
        } else {
            _mBLastTick = 0;
        }
    }

    @Override
    public int getVoltageIndex() {
        return 0;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid().getName().equalsIgnoreCase("steam"))
            return _steamTank.fill(resource, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{_steamTank.getInfo()};
    }

    @Override
    public double getInputRate() {
        return _mBLastTick;
    }

    @Override
    @Optional.Method(modid = ModIDReference.BUILDCRAFT)
    public ConnectOverride overridePipeConnection(IPipeTile.PipeType pipeType, ForgeDirection direction) {
        if (pipeType == IPipeTile.PipeType.FLUID)
            return ConnectOverride.CONNECT;
        return ConnectOverride.DISCONNECT;
    }
}
