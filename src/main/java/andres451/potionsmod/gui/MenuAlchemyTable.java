package andres451.potionsmod.gui;

import andres451.potionsmod.AlchemyRecipes;
import andres451.potionsmod.ModPotionItem;
import andres451.potionsmod.TileEntityAlchemyTable;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class MenuAlchemyTable extends MenuAbstract {
	private static final int MACHINE_SLOTS = 5;
	private static final int INV_START = 5;
	private static final int HOTBAR_START = 32;
	private static final int INV_END = 41;

	private final TileEntityAlchemyTable table;
	private int lastProgress, lastMaxProgress, lastBurn, lastMaxBurn;

	public MenuAlchemyTable(@NotNull ContainerInventory inventory, TileEntityAlchemyTable table) {
		this.table = table;
		this.addSlot(new SlotFiltered(table, AlchemyRecipes.SLOT_A, 44, 18, AlchemyRecipes::isIngredient));
		this.addSlot(new SlotFiltered(table, AlchemyRecipes.SLOT_B, 80, 18, AlchemyRecipes::isIngredient));
		this.addSlot(new SlotFiltered(table, AlchemyRecipes.SLOT_BASE, 62, 54, AlchemyRecipes::isBase, 1));
		this.addSlot(new SlotFiltered(table, AlchemyRecipes.SLOT_BUCKET, 134, 18, s -> s.getItem() instanceof ItemBucket));
		this.addSlot(new SlotFiltered(table, AlchemyRecipes.SLOT_FUEL, 134, 54, s -> s.getItem() == Items.NETHERCOAL));
		for (int i = 0; i < 3; ++i) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
			}
		}
		for (int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
		}
	}

	private int machineSlotFor(ItemStack s) {
		if (s == null) return -1;
		if (s.getItem() == Items.NETHERCOAL) return AlchemyRecipes.SLOT_FUEL;
		if (s.getItem() instanceof ItemBucket) return AlchemyRecipes.SLOT_BUCKET;
		if (AlchemyRecipes.isBase(s)) return AlchemyRecipes.SLOT_BASE;
		if (AlchemyRecipes.isIngredient(s)) return AlchemyRecipes.SLOT_A;
		return -1;
	}

	@Override
	public IntList getMoveSlots(@NotNull InventoryAction action, @NotNull Slot slot, int target, Player player) {
		if (slot.index >= 0 && slot.index < MACHINE_SLOTS) {
			return this.getSlots(slot.index, 1, false);
		}
		if (action == InventoryAction.MOVE_ALL) {
			if (slot.index >= INV_START && slot.index < HOTBAR_START) return this.getSlots(INV_START, 27, false);
			if (slot.index >= HOTBAR_START && slot.index < INV_END) return this.getSlots(HOTBAR_START, 9, false);
		}
		return slot.index >= INV_START && slot.index < INV_END ? this.getSlots(INV_START, 36, false) : null;
	}

	@Override
	public IntList getTargetSlots(@NotNull InventoryAction action, @NotNull Slot slot, int target, Player player) {
		if (slot.index >= INV_START && slot.index < INV_END) {
			if (action != InventoryAction.MOVE_ALL) {
				ItemStack moving = slot.getItemStack();
				int dest = machineSlotFor(moving);
				if (dest == AlchemyRecipes.SLOT_A) return this.getSlots(AlchemyRecipes.SLOT_A, 2, false);
				if (dest == AlchemyRecipes.SLOT_BASE) {
					IntList list = new IntArrayList(this.getSlots(AlchemyRecipes.SLOT_BASE, 1, false));
					if (moving != null && moving.getItem() instanceof ModPotionItem) {
						list.addAll(this.getSlots(AlchemyRecipes.SLOT_A, 2, false));
					}
					return list;
				}
				if (dest >= 0) return this.getSlots(dest, 1, false);
			}
			if (slot.index < HOTBAR_START) return this.getSlots(HOTBAR_START, 9, false);
			return this.getSlots(INV_START, 27, false);
		}
		if (slot.index >= 0 && slot.index < MACHINE_SLOTS) {
			return this.getSlots(INV_START, 36, slot.index == AlchemyRecipes.SLOT_BASE);
		}
		return IntLists.EMPTY_LIST;
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		for (ContainerListener crafter : this.containerListeners) {
			if (lastProgress != table.getProgress()) crafter.updateCraftingInventoryInfo(this, 0, table.getProgress());
			if (lastMaxProgress != table.getMaxProgress()) crafter.updateCraftingInventoryInfo(this, 1, table.getMaxProgress());
			if (lastBurn != table.getBurnTime()) crafter.updateCraftingInventoryInfo(this, 2, table.getBurnTime());
			if (lastMaxBurn != table.getMaxBurnTime()) crafter.updateCraftingInventoryInfo(this, 3, table.getMaxBurnTime());
		}
		lastProgress = table.getProgress();
		lastMaxProgress = table.getMaxProgress();
		lastBurn = table.getBurnTime();
		lastMaxBurn = table.getMaxBurnTime();
	}

	@Override
	public void setData(int id, int value) {
		switch (id) {
			case 0 -> table.setProgress(value);
			case 1 -> table.setMaxProgress(value);
			case 2 -> table.setBurnTime(value);
			case 3 -> table.setMaxBurnTime(value);
			default -> { }
		}
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return table.stillValid(player);
	}
}
