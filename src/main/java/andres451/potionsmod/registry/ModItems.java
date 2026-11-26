package andres451.potionsmod.registry;

import static andres451.potionsmod.PotionsMod.MOD_ID;

import andres451.potionsmod.PotionVariant;
import andres451.potionsmod.PotionsMod;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public class ModItems {
	public static final String[] JAR_COLORS = {
		"red", "green", "blue", "cyan", "lime", "orange",
		"purple", "skyblue", "white", "yellow", "magenta",
		"pink", "lightmagenta", "black", "ultramarine",
		"chartreuse", "lightcyan", "bronze"
	};
	private static final int BASE_ID = 17007;

	public static Item[] jars = new Item[JAR_COLORS.length];
	public static Item[] dilutedJars = new Item[JAR_COLORS.length];
	public static Item[] denseJars = new Item[JAR_COLORS.length];

	private static Item register(String color, int index, PotionVariant v) {
		String suffix = v == PotionVariant.NORMAL ? "" : "_" + v.name().toLowerCase();
		String name = "jar_" + color + suffix;
		int id = BASE_ID + JAR_COLORS.length * v.ordinal() + index;
		Item item = new ItemBuilder(MOD_ID)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Items.JAR))
			.build(new ModPotionItem(name, MOD_ID + ":item/" + name, id, color, v));
		PotionsMod.LOGGER.info("Potions: registered {} (id {}, texture {}:item/{})", name, id, MOD_ID, name);
		return item;
	}

	public static void initItems() {
		for (int i = 0; i < JAR_COLORS.length; i++) {
			String c = JAR_COLORS[i];
			jars[i] = register(c, i, PotionVariant.NORMAL);
			if (c.equals("white")) continue;
			dilutedJars[i] = register(c, i, PotionVariant.DILUTED);
			denseJars[i] = register(c, i, PotionVariant.DENSE);
		}
	}

	public static Item jar(String color) {
		return jar(color, PotionVariant.NORMAL);
	}

	public static Item jar(String color, PotionVariant v) {
		Item[] source = switch (v) {
			case DILUTED -> dilutedJars;
			case DENSE -> denseJars;
			default -> jars;
		};
		for (int i = 0; i < JAR_COLORS.length; i++) {
			if (JAR_COLORS[i].equals(color)) return source[i];
		}
		throw new IllegalArgumentException("Wrong color: " + color);
	}
}
