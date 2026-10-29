package andres451.potionsmod;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModPotionItem extends Item {
	private final String color;

	public ModPotionItem(String name, String namespaceId, int id, String color) {
		super(name, namespaceId, id);
		this.color = color;
		this.setMaxStackSize(1);
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		if (!world.isClientSide) {
			PotionEffects.apply(color, player);
		}
		selfStack.stackSize--;
		return selfStack;
	}
}
