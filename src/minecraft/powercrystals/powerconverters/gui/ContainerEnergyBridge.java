package powercrystals.powerconverters.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.common.BridgeSideData;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.power.PowerSystem;

public class ContainerEnergyBridge extends Container {
    private static final int _numParams = 7;

    private TileEntityEnergyBridge _bridge;

    public ContainerEnergyBridge(TileEntityEnergyBridge bridge, InventoryPlayer inv) {
        _bridge = bridge;
        bindPlayerInventory(inv);
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return true;
    }

    @Override
    public void updateProgressBar(int var, int value) {
        if (var == 1000) _bridge.setIsInputLimited(value != 0);
        else if (var == 1001) _bridge.setEnergyScaled(value);
        else {
            int side = var / _numParams;
            int sidevar = var % _numParams;

            if (sidevar == 0) _bridge.getDataForSide(ForgeDirection.getOrientation(side)).voltageNameIndex = value;
            if (sidevar == 1) _bridge.getDataForSide(ForgeDirection.getOrientation(side)).isConsumer = (value != 0);
            if (sidevar == 2) _bridge.getDataForSide(ForgeDirection.getOrientation(side)).isProducer = (value != 0);
            if (sidevar == 3)
                _bridge.getDataForSide(ForgeDirection.getOrientation(side)).powerSystem = PowerSystem.getPowerSystemById(value);
            if (sidevar == 4) _bridge.getDataForSide(ForgeDirection.getOrientation(side)).isConnected = (value != 0);
            if (sidevar == 5) _bridge.getDataForSide(ForgeDirection.getOrientation(side)).outputRate = value;
            if (sidevar == 6) _bridge.setEnergyStored(value);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int side = 0; side < 6; side++) {
            ForgeDirection d = ForgeDirection.getOrientation(side);
            BridgeSideData data = _bridge.getDataForSide(d);
            for (Object _crafter : crafters) {
                ICrafting crafter = (ICrafting) _crafter;
                crafter.sendProgressBarUpdate(this, side * _numParams, data.voltageNameIndex);
                crafter.sendProgressBarUpdate(this, side * _numParams + 1, data.isConsumer ? 1 : 0);
                crafter.sendProgressBarUpdate(this, side * _numParams + 2, data.isProducer ? 1 : 0);
                if (data.powerSystem != null) {
                    crafter.sendProgressBarUpdate(this, side * _numParams + 3, data.powerSystem.getId());
                }
                crafter.sendProgressBarUpdate(this, side * _numParams + 4, data.isConnected ? 1 : 0);
                crafter.sendProgressBarUpdate(this, side * _numParams + 5, (int) data.outputRate);
                crafter.sendProgressBarUpdate(this, side * _numParams + 6, _bridge.getEnergyStored());
            }
        }

        for (Object _crafter : crafters) {
            ICrafting crafter = (ICrafting) _crafter;
            crafter.sendProgressBarUpdate(this, 1000, _bridge.isInputLimited() ? 1 : 0);
            crafter.sendProgressBarUpdate(this, 1001, _bridge.getEnergyScaled());
        }
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 113 + i * 18));
        }
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 171));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }
}
