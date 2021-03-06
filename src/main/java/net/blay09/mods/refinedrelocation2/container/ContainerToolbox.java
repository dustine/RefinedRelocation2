package net.blay09.mods.refinedrelocation2.container;

import net.blay09.mods.refinedrelocation2.item.toolbox.ToolboxItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerToolbox extends Container {

    private final ToolboxItemHandler toolboxInventory;

    public ContainerToolbox(EntityPlayer entityPlayer, ToolboxItemHandler toolboxInventory) {
        this.toolboxInventory = toolboxInventory;
        int offsetY = 51;

        for (int j = 0; j < toolboxInventory.getSlots(); ++j) {
            this.addSlotToContainer(new SlotItemHandler(toolboxInventory, j, 44 + j * 18, 20));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(entityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, i * 18 + offsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(entityPlayer.inventory, i, 8 + i * 18, 58 + offsetY));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return toolboxInventory.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index) {
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (index < toolboxInventory.getSlots()) {
                if (!mergeItemStack(slotStack, toolboxInventory.getSlots(), inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(slotStack, 0, toolboxInventory.getSlots(), false)) {
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        toolboxInventory.closeInventory(entityPlayer);
    }

}
