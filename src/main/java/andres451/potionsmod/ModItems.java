package andres451.potionsmod;

import static andres451.potionsmod.PotionsMod.MOD_ID;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public class ModItems {
	public static final String[] JAR_COLORS = {
		"red", "green", "blue", "cyan", "lime",
		"orange", "purple", "skyblue", "white", "yellow", "magenta", "pink", "lightmagenta"
	};
	public static Item[] jars = new Item[JAR_COLORS.length];

    public static void initItems() {
		for (int i = 0; i < JAR_COLORS.length; i++) {
			String c = JAR_COLORS[i];
			jars[i] = new ItemBuilder(MOD_ID)
				.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Items.TOOL_SWORD_STEEL))
				.build(new ModPotionItem("jar_" + c, MOD_ID + ":item/jar_" + c, 17007 + i, c));
		}
    }

	public static Item jar(String color) {
		for (int i = 0; i < JAR_COLORS.length; i++) {
			if (JAR_COLORS[i].equals(color)) return jars[i];
		}
		throw new IllegalArgumentException("Wrong color: " + color);
	}
}
