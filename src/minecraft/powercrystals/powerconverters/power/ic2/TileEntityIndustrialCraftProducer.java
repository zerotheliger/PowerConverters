package powercrystals.powerconverters.power.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import powercrystals.core.position.BlockPosition;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.List;

public class TileEntityIndustrialCraftProducer extends TileEntityEnergyProducer<IEnergyAcceptor> implements IEnergyTile {
    private final double maxSendEnergy;
    private boolean _isAddedToEnergyNet;
    private boolean _didFirstAddToNet;

    @SuppressWarnings("UnusedDeclaration")
    public TileEntityIndustrialCraftProducer() {
        this(0);
    }

    public TileEntityIndustrialCraftProducer(int voltageIndex) {
        super(PowerConverterCore.powerSystemIndustrialCraft, voltageIndex, IEnergyAcceptor.class);
        if (voltageIndex == 0)
            maxSendEnergy = 32;
        else if (voltageIndex == 1)
            maxSendEnergy = 128;
        else if (voltageIndex == 2)
            maxSendEnergy = 512;
        else if (voltageIndex == 3)
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
    public double produceEnergy(double _energy) {
        if (!_isAddedToEnergyNet)
            return _energy;

        final double energyToUse = _energy / getPowerSystem().getInternalEnergyPerOutput();
        if (energyToUse > 0) {
            List<BlockPosition> positions = new BlockPosition(xCoord, yCoord, zCoord).getAdjacent(true);
            for (BlockPosition p : positions) {
                TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
                if ((te instanceof IEnergySink) && !((te instanceof TileEntityIndustrialCraftConsumer) || (te instanceof TileEntityEnergyBridge))) {
                    IEnergySink sink = (IEnergySink) te;
                    final double demands = sink.demandedEnergyUnits();
                    final double toInject = Math.min(maxSendEnergy, Math.min(energyToUse, demands));
                    final double leftOver = sink.injectEnergyUnits(p.orientation, toInject);
                    _energy -= toInject * getPowerSystem().getInternalEnergyPerOutput();
                    _energy += leftOver * getPowerSystem().getInternalEnergyPerOutput();
                    if (_energy <= 0)
                        break; // no more energy to give, so stop scanning
                }
            }
        }
        return _energy;

    }
}