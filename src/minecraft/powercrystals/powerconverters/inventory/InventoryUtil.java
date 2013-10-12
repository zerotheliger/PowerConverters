package powercrystals.powerconverters.inventory;

import net.minecraft.item.ItemStack;

public abstract class InventoryUtil {

    public static boolean stacksEqual(ItemStack s1, ItemStack s2) {
        return stacksEqual(s1, s2, true);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean stacksEqual(ItemStack s1, ItemStack s2, boolean nbtSensitive) {
        if (s1 == null || s2 == null) return false;
        if (!s1.isItemEqual(s2)) return false;

        if (nbtSensitive) {
            if (s1.getTagCompound() == null && s2.getTagCompound() == null) return true;
            if (s1.getTagCompound() == null || s2.getTagCompound() == null) return false;
            return s1.getTagCompound().equals(s2.getTagCompound());
        }

        return true;
    }
}