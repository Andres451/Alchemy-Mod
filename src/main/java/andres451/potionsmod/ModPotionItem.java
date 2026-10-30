package andres451.potionsmod;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.entity.Entity;

public class ModPotionItem extends Item {
	private final String color;
	private static final String DRINK_SOUND = "potionsmod:potion_drink";

	public ModPotionItem(String name, String namespaceId, int id, String color) {
		super(name, namespaceId, id);
		this.color = color;
		this.setMaxStackSize(1);
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		if (!world.isClientSide) {
			PotionEffects.apply(color, player);
			float pitch = 0.9F + world.rand.nextFloat() * 0.2F;
			world.playSoundAtEntity((Entity) null, player, DRINK_SOUND, 1.0F, pitch);
		}
		selfStack.stackSize--;
		if (selfStack.stackSize <= 0) {
			return new ItemStack(emptyJar());
		}
		giveEmptyJar(player);
		return selfStack;
	}

	private static Item emptyJar() {
		return Items.JAR;
	}

	private static void giveEmptyJar(Player player) {
		ItemStack empty = new ItemStack(emptyJar());
		player.inventory.insertItem(empty, true);
		if (empty.stackSize > 0) {
			player.world.dropItem(player.x, player.y + (double) player.bbHeight, player.z, empty, 0.0);
		}
	}
}
