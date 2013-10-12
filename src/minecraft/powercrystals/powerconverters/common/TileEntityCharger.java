package powercrystals.powerconverters.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.power.PowerSystem;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;

import java.util.ArrayList;
import java.util.List;

public class TileEntityCharger extends TileEntityEnergyProducer<IInventory> {
    private static List<IChargeHandler> _chargeHandlers = new ArrayList<IChargeHandler>();
    private EntityPlayer _player;

    public static void registerChargeHandler(IChargeHandler handler) {
        _chargeHandlers.add(handler);
    }

    public TileEntityCharger() {
        super(PowerConverterCore.powerSystemIndustrialCraft, 0, IInventory.class);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (_player != null && _player.getDistance(xCoord, yCoord, zCoord) > 2D)
            setPlayer(null);
    }

    @Override
    public double produceEnergy(double energy) {
        if (energy == 0)
            return 0;

        int energyRemaining = (int) energy;
        if (_player != null)
            energyRemaining = chargeInventory(_player.inventory, energyRemaining);

        for (IInventory inv : getTiles().values())
            energyRemaining = chargeInventory(inv, energyRemaining);

        return energyRemaining;
    }

    private int chargeInventory(IInventory inventory, int energy) {
        PowerSystem nextPowerSystem = getPowerSystem();
        int energyRemaining = energy;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            for (IChargeHandler chargeHandler : _chargeHandlers) {
                ItemStack s = inventory.getStackInSlot(i);
                if (s == null)
                    continue;

                if (chargeHandler.canHandle(s)) {
                    energyRemaining = chargeHandler.charge(s, energyRemaining);
                    if (energyRemaining < energy) {
                        nextPowerSystem = chargeHandler.getPowerSystem();
                        energy = energyRemaining;
                    }
                }
            }
        }
        _powerSystem = nextPowerSystem;
        return energyRemaining;
    }

    public void setPlayer(EntityPlayer player) {
        if (worldObj.isRemote && _player != player) {
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            _player = player;
        }
    }

    @Override
    public boolean isConnected() {
        return super.isConnected() || _player != null;
    }

    @Override
    public boolean isSideConnected(int side) {
        return side == 1 && _player != null || super.isSideConnected(side);
    }

    @Override
    public boolean isSideConnectedClient(int side) {
        return side == 1 && _player != null || super.isSideConnectedClient(side);
    }
}
