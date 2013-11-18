package powercrystals.powerconverters.common;

import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.power.PowerSystem;

public interface IChargeHandler {

    /**
     * @return power system
     */
    public PowerSystem getPowerSystem();

    /**
     *
     * @param stack stack to check before calling charge/discharge methods
     * @return whether the stack is applicable to receive/give energy
     */
    boolean canHandle(ItemStack stack);

    /**
     *
     * @param stack applicable stack
     * @param energyInput energy to give
     * @return energy remaining after charge
     */
    int charge(ItemStack stack, int energyInput);

    // TODO: implement a discharger
    int discharge(ItemStack stack, int energyRequest);
}
