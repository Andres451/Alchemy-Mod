package andres451.potionsmod.registry;

import andres451.potionsmod.PotionsMod;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.RecipeBuilder;

public class ModRecipes {
	public static void init() {
		RecipeBuilder.Shaped(PotionsMod.MOD_ID, "SSS", "B B", "BBB")
			.addInput('S', Items.INGOT_STEEL)
			.addInput('B', Blocks.BRICK_CLAY)
			.create("alchemy_table", new ItemStack(ModBlocks.alchemyTable));
	}
}
