package powercrystals.powerconverters.power.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.mods.IndustrialCraft;
import powercrystals.powerconverters.power.ICustomHandler;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.Map;

public class TileEntityIndustrialCraftProducer extends TileEntityEnergyProducer<IEnergyAcceptor> implements IEnergySource, ICustomHandler {
    private double maxSendEnergy;
    private boolean _isAddedToEnergyNet;
    private boolean _didFirstAddToNet;

    private double lastSentEnergy;

    @SuppressWarnings("UnusedDeclaration")
    public TileEntityIndustrialCraftProducer() {
        this(0);
    }

    public TileEntityIndustrialCraftProducer(int voltageIndex) {
        super(IndustrialCraft.INSTANCE.powerSystem, voltageIndex, IEnergyAcceptor.class);
        setMaxSendEnergy(voltageIndex);
    }

    private void setMaxSendEnergy(int index) {
        if (index == 0) // lv
            maxSendEnergy = 32;
        else if (index == 1) // mv
            maxSendEnergy = 128;
        else if (index == 2) // hv
            maxSendEnergy = 512;
        else if (index == 3) // ev
            maxSendEnergy = 2048;
        else
            maxSendEnergy = 0;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!_didFirstAddToNet && !worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            _didFirstAddToNet = true;
            _isAddedToEnergyNet = true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        setMaxSendEnergy(_voltageIndex);
    }

    @Override
    public void validate() {
        super.validate();
        if (!_isAddedToEnergyNet) {
            _didFirstAddToNet = false;
        }
    }

    @Override
    public void invalidate() {
        if (_isAddedToEnergyNet) {
            if (!worldObj.isRemote) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }
            _isAddedToEnergyNet = false;
        }
        super.invalidate();
    }

    @Override
    public double produceEnergy(double energy) {
        return energy;
    }

    @Override
    public double getOfferedEnergy() {
        double eu = 0D;
        for (TileEntityEnergyBridge bridge : getBridges().values())
            eu += bridge.getEnergyStored();
        return Math.min(maxSendEnergy, eu / getPowerSystem().getInternalEnergyPerOutput());
    }

    @Override
    public void drawEnergy(double amount) {
        double drawn = 0D;
        for (Map.Entry<ForgeDirection, TileEntityEnergyBridge> bridge : getBridges().entrySet()) {
            drawn += bridge.getValue().useEnergy(amount * getPowerSystem().getInternalEnergyPerOutput(), false) / getPowerSystem().getInternalEnergyPerOutput();
            if (drawn >= amount)
                break;
        }
        lastSentEnergy = drawn;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return true;
    }

    @Override
    public boolean shouldHandle() {
        return true;
    }

    @Override
    public double getOutputRate() {
        double temp = lastSentEnergy;
        lastSentEnergy = 0;
        return temp;
    }
}