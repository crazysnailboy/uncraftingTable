package org.jglrxavpok.mods.decraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBook extends Slot
{
	private ContainerUncraftingTable container;

	public SlotBook(IInventory inventoryIn, int index, int xPosition, int yPosition, ContainerUncraftingTable containerIn)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.container = containerIn;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() == Items.BOOK;
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
		this.container.onCraftMatrixChanged(this.inventory);
	}

}
