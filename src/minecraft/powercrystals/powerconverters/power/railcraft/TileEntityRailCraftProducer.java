package powercrystals.powerconverters.power.railcraft;

import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.common.Optional;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.mods.reference.InterfaceReference;
import powercrystals.powerconverters.mods.reference.ModIDReference;
import powercrystals.powerconverters.position.BlockPosition;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

@Optional.Interface(modid = ModIDReference.BUILDCRAFT, iface = InterfaceReference.BuildCraft.IPipeConnection)
public class TileEntityRailCraftProducer extends TileEntityEnergyProducer<IFluidHandler> implements IFluidHandler, IPipeConnection {

    public TileEntityRailCraftProducer() {
        super(PowerConverterCore.powerSystemSteam, 0, IFluidHandler.class);
    }

    @Override
    public double produceEnergy(double energy) {
        energy = energy / PowerConverterCore.powerSystemSteam.getInternalEnergyPerOutput();
        for (int i = 0; i < 6; i++) {
            BlockPosition bp = new BlockPosition(this);
            bp.orientation = ForgeDirection.getOrientation(i);
            bp.moveForwards(1);
            TileEntity te = worldObj.getBlockTileEntity(bp.x, bp.y, bp.z);

            if (te instanceof IFluidHandler) {
                final int steam = (int) Math.min(energy, PowerConverterCore.throttleSteamProducer.getInt());
                FluidStack stack = FluidRegistry.getFluidStack("steam", steam);
                if (stack == null)
                    FluidRegistry.getFluidStack("Steam", steam);
                energy -= ((IFluidHandler) te).fill(bp.orientation.getOpposite(), stack, true);
            }

            if (energy <= 0)
                return 0;
        }

        return energy * PowerConverterCore.powerSystemSteam.getInternalEnergyPerOutput();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
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
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    @Override
    @Optional.Method(modid = ModIDReference.BUILDCRAFT)
    public ConnectOverride overridePipeConnection(IPipeTile.PipeType pipeType, ForgeDirection direction) {
        if (pipeType == IPipeTile.PipeType.FLUID)
            return ConnectOverride.CONNECT;
        return ConnectOverride.DISCONNECT;
    }
}
