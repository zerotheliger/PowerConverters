package powercrystals.powerconverters.power.thermalexpansion;

import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.IChargeHandler;
import powercrystals.powerconverters.power.PowerSystem;
import thermalexpansion.block.energycell.ItemBlockEnergyCell;

/**
 * @author samrg472
 */
public class ChargeHandlerRF implements IChargeHandler {

    @Override
    public PowerSystem getPowerSystem() {
        return PowerConverterCore.powerSystemThermalExpansion;
    }

    @Override
    public boolean canHandle(ItemStack stack) {
        return stack.getItem() instanceof ItemBlockEnergyCell;
    }

    @Override
    public int charge(ItemStack stack, int energyInput) {
        final int energyToUse = energyInput / getPowerSystem().getInternalEnergyPerOutput();
        final int energyUsed = ((ItemBlockEnergyCell) stack.getItem()).receiveEnergy(stack, energyToUse, false);
        return (energyToUse - energyUsed) * getPowerSystem().getInternalEnergyPerOutput();
    }

    @Override
    public int discharge(ItemStack stack, int energyRequest) {
        return (((ItemBlockEnergyCell) stack.getItem()).extractEnergy(stack, energyRequest / getPowerSystem().getInternalEnergyPerOutput(), false)) * getPowerSystem().getInternalEnergyPerOutput();
    }
}
