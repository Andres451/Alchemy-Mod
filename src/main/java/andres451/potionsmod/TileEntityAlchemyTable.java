package andres451.potionsmod;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.sound.SoundCategory;

public class TileEntityAlchemyTable extends TileEntity implements Container {
	private static final int COOKING_TIME = 300;

	protected ItemStack[] slots = new ItemStack[5];
	private int progress = 0;
	private int maxProgress = COOKING_TIME;
	private int burnTime = 0;
	private int maxBurnTime = 0;

	//CONTAINER

	public int getContainerSize() {
		return slots.length;
	}

	public @Nullable ItemStack getItem(int index) {
		return slots[index];
	}

	public @Nullable ItemStack removeItem(int index, int takeAmount) {
		if (slots[index] == null) return null;
		if (slots[index].stackSize <= takeAmount) {
			ItemStack s = slots[index];
			slots[index] = null;
			return s;
		}
		ItemStack split = slots[index].splitStack(takeAmount);
		if (slots[index].stackSize <= 0) slots[index] = null;
		return split;
	}

	public void setItem(int index, @Nullable ItemStack itemStack) {
		slots[index] = itemStack;
		if (itemStack != null && itemStack.stackSize > getMaxStackSize()) {
			itemStack.stackSize = getMaxStackSize();
		}
	}

	public @NotNull String getNameTranslationKey() {
		return "container.potionsmod.alchemy_table.name";
	}

	public int getMaxStackSize() {
		return 64;
	}

	public boolean stillValid(Player player) {
		if (this.worldObj != null && this.worldObj.getTileEntity(this.tilePos) == this) {
			return player.distanceToSqr(this.tilePos.x + 0.5, this.tilePos.y + 0.5, this.tilePos.z + 0.5) <= 64.0;
		}
		return false;
	}

	@Override
	public void sort() {
	}

	//STATE

	public int getProgress() { return progress; }
	public void setProgress(int v) { progress = v; }
	public int getMaxProgress() { return maxProgress; }
	public void setMaxProgress(int v) { maxProgress = v; }
	public int getBurnTime() { return burnTime; }
	public void setBurnTime(int v) { burnTime = v; }
	public int getMaxBurnTime() { return maxBurnTime; }
	public void setMaxBurnTime(int v) { maxBurnTime = v; }

	public boolean isBurning() {
		return burnTime > 0;
	}

	public int getBurnRemainingScaled(int i) {
		return maxBurnTime == 0 ? 0 : burnTime * i / maxBurnTime;
	}

	public int getProgressScaled(int i) {
		return maxProgress == 0 ? 0 : progress * i / maxProgress;
	}

	//LOGIC

	@Override
	public void tick() {
		if (this.worldObj != null && this.worldObj.isClientSide) return;

		boolean changed = false;
		boolean wasBurning = burnTime > 0;
		if (burnTime > 0) burnTime--;

		AlchemyRecipes.Recipe recipe = AlchemyRecipes.find(
			slots[AlchemyRecipes.SLOT_A], slots[AlchemyRecipes.SLOT_B],
			slots[AlchemyRecipes.SLOT_BASE], slots[AlchemyRecipes.SLOT_BUCKET]);

		ItemStack fuel = slots[AlchemyRecipes.SLOT_FUEL];
		if (burnTime == 0 && recipe != null && fuel != null) {
			int yield = LookupFuelFurnace.instance.getFuelYield(fuel.getItem().id);
			if (yield > 0) {
				burnTime = yield;
				maxBurnTime = yield;
				fuel.stackSize--;
				if (fuel.stackSize <= 0) slots[AlchemyRecipes.SLOT_FUEL] = null;
				changed = true;
			}
		}

		if (burnTime > 0 && recipe != null) {
			progress++;

			if (this.worldObj != null && progress % 30 == 1) {
				float pitch = 0.8F + this.worldObj.rand.nextFloat() * 0.4F;
				this.worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS,
					this.tilePos.x + 0.5, this.tilePos.y + 0.5, this.tilePos.z + 0.5,
					"tile.acidbubble", 0.4F, pitch);
			}

			if (progress >= maxProgress) {
				progress = 0;
				AlchemyRecipes.apply(slots, recipe);
				changed = true;
			}
		} else {
			progress = 0;
		}

		boolean isBurningNow = burnTime > 0;
		if (this.worldObj != null && wasBurning != isBurningNow) {
			BlockLogicAlchemyTable.updateBlockState(this.worldObj, this.tilePos, isBurningNow);
			changed = true;
		}

		if (changed) this.setChanged();
	}

	//PRESERVE STATE

	@Override
	public void readAdditionalData(@NotNull CompoundTag tag) {
		ListTag list = tag.getList("Items");
		slots = new ItemStack[getContainerSize()];
		for (int i = 0; i < list.tagCount(); ++i) {
			CompoundTag t = (CompoundTag) list.tagAt(i);
			byte slot = t.getByte("Slot");
			if (slot >= 0 && slot < slots.length) {
				slots[slot] = ItemStack.readItemStackFromNbt(t);
			}
		}
		progress = tag.getInteger("Progress");
		burnTime = tag.getInteger("BurnTime");
		maxBurnTime = tag.getInteger("MaxBurnTime");
	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag tag) {
		tag.putInt("Progress", progress);
		tag.putInt("BurnTime", burnTime);
		tag.putInt("MaxBurnTime", maxBurnTime);
		ListTag list = new ListTag();
		for (int i = 0; i < slots.length; ++i) {
			if (slots[i] != null) {
				CompoundTag t = new CompoundTag();
				t.putByte("Slot", (byte) i);
				slots[i].writeToNBT(t);
				list.addTag(t);
			}
		}
		tag.put("Items", list);
	}

	@Override
	public void dropContents(World world, int x, int y, int z) {
		super.dropContents(world, x, y, z);
		for (int l = 0; l < getContainerSize(); ++l) {
			ItemStack stack = getItem(l);
			if (stack == null) continue;
			float fx = world.rand.nextFloat() * 0.8F + 0.1F;
			float fy = world.rand.nextFloat() * 0.8F + 0.1F;
			float fz = world.rand.nextFloat() * 0.8F + 0.1F;
			while (stack.stackSize > 0) {
				int n = Math.min(stack.stackSize, world.rand.nextInt(21) + 10);
				stack.stackSize -= n;
				ItemStack drop = stack.copy();
				drop.stackSize = n;
				EntityItem e = new EntityItem(world, x + fx, y + fy, z + fz, drop);
				float f3 = 0.05F;
				e.xd = (float) world.rand.nextGaussian() * f3;
				e.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
				e.zd = (float) world.rand.nextGaussian() * f3;
				world.entityJoinedWorld(e);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		return null;
	}
}
