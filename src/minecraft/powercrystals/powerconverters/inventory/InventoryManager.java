package powercrystals.powerconverters.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.common.ForgeDirection;

public class InventoryManager {
    @SuppressWarnings("deprecation")
    public static IInventoryManager create(IInventory inventory, ForgeDirection targetSide) {
        if (inventory instanceof ISidedInventory) {
            return new InventoryManagerSided((ISidedInventory) inventory, targetSide);
        } else if (inventory != null) {
            return new InventoryManagerStandard(inventory, targetSide);
        } else {
            return null;
        }
    }
}
