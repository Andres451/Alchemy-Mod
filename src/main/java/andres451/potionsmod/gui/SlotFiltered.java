package andres451.potionsmod.gui;

import java.util.function.Predicate;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotFiltered extends Slot {
	private final Predicate<ItemStack> filter;
	private final int maxStack;

	public SlotFiltered(Container container, int index, int x, int y, Predicate<ItemStack> filter) {
		this(container, index, x, y, filter, -1);
	}

	public SlotFiltered(Container container, int index, int x, int y, Predicate<ItemStack> filter, int maxStack) {
		super(container, index, x, y);
		this.filter = filter;
		this.maxStack = maxStack;
	}

	@Override
	public boolean mayPlace(ItemStack itemstack) {
		return itemstack != null && filter.test(itemstack);
	}

	@Override
	public int getMaxStackSize() {
		return maxStack > 0 ? maxStack : super.getMaxStackSize();
	}
}
