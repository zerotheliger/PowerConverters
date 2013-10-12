package powercrystals.powerconverters.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;
import java.util.Map;

public class InventoryManagerStandard implements IInventoryManager {
    private IInventory _inv;
    protected ForgeDirection _targetSide;

    public InventoryManagerStandard(IInventory inventory, ForgeDirection targetSide) {
        _inv = inventory;
        _targetSide = targetSide;
    }

    protected boolean canAddItem(ItemStack stack, int slot) {
        return _inv.isItemValidForSlot(slot, stack);
    }

    @Override
    public ItemStack addItem(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        int quantitytoadd = stack.stackSize;
        ItemStack remaining = stack.copy();
        int[] slots = getSlots();
        if (slots == null) {
            return remaining;
        }

        for (int i : slots) {
            int maxStackSize = Math.min(_inv.getInventoryStackLimit(), stack.getMaxStackSize());
            ItemStack s = getSlotContents(i);
            if (s == null) {
                ItemStack add = stack.copy();
                add.stackSize = Math.min(quantitytoadd, maxStackSize);

                if (canAddItem(add, i)) {
                    quantitytoadd -= add.stackSize;
                    _inv.setInventorySlotContents(i, add);
                }
            } else if (InventoryUtil.stacksEqual(s, stack)) {
                ItemStack add = stack.copy();
                add.stackSize = Math.min(quantitytoadd, maxStackSize - s.stackSize);

                if (add.stackSize > 0 && canAddItem(add, i)) {
                    s.stackSize += add.stackSize;
                    quantitytoadd -= add.stackSize;
                    _inv.setInventorySlotContents(i, s);
                }
            }
            if (quantitytoadd == 0) {
                break;
            }
        }

        remaining.stackSize = quantitytoadd;
        if (remaining.stackSize == 0) {
            return null;
        } else {
            return remaining;
        }
    }

    @Override
    public ItemStack getSlotContents(int slot) {
        return _inv.getStackInSlot(slot);
    }

    @Override
    public int[] getSlots() {
        int[] slots = new int[_inv.getSizeInventory()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public Map<Integer, ItemStack> getContents() {
        Map<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>();
        for (int i : getSlots()) {
            contents.put(i, _inv.getStackInSlot(i));
        }
        return contents;
    }
}
