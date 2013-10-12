package powercrystals.powerconverters.inventory;

import net.minecraft.item.ItemStack;

import java.util.Map;

public interface IInventoryManager {
    public ItemStack addItem(ItemStack stack);

    public ItemStack getSlotContents(int slot);

    public int[] getSlots();

    public Map<Integer, ItemStack> getContents();
}
