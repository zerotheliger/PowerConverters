package powercrystals.powerconverters.common;

import net.minecraft.item.ItemStack;
import powercrystals.powerconverters.power.PowerSystem;

public interface IChargeHandler {
    public PowerSystem getPowerSystem();

    boolean canHandle(ItemStack stack);

    int charge(ItemStack stack, int energyInput);

    int discharge(ItemStack stack, int energyRequest);
}
