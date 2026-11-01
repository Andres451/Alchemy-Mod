package andres451.potionsmod;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DyeColor;

public class AlchemyRecipes {
	public static final int SLOT_A = 0;
	public static final int SLOT_B = 1;
	public static final int SLOT_BASE = 2;
	public static final int SLOT_BUCKET = 3;
	public static final int SLOT_FUEL = 4;

	public record Ingredient(IItemConvertible item, int meta) {
		public boolean matches(ItemStack s) {
			if (item == null || s == null) return false;
			if (s.itemID != item.getDefaultStack().itemID) return false;
			return meta < 0 || s.getMetadata() == meta;
		}
	}

	public record Recipe(Item base, Ingredient a, Ingredient b, NamespaceID liquid, Item result) {}

	private static final List<Recipe> RECIPES = new ArrayList<>();

	private static Ingredient any(IItemConvertible item) { return new Ingredient(item, -1); }
	private static Ingredient meta(IItemConvertible item, int m) { return new Ingredient(item, m); }

	private static void add(Item base, Ingredient a, Ingredient b, NamespaceID liquid, Item result) {
		if (base == null || a.item() == null || b.item() == null || result == null) {
			PotionsMod.LOGGER.warn("AlchemyRecipes: recipe error (base={}, a={}, b={}, result={})",
				base != null, a.item() != null, b.item() != null, result != null);
			return;
		}
		RECIPES.add(new Recipe(base, a, b, liquid, result));
	}

	// ADD BOTH PERMUTATIONS OF A RECIPE
	private static void pair(Ingredient extra, Item p1, Item p2, Item result, NamespaceID liquid) {
		add(p2, extra, any(p1), liquid, result);
		add(p1, extra, any(p2), liquid, result);
	}

	public static void init() {
		Item antidote = ModItems.jar("white");
		Item damage = ModItems.jar("red");
		Item healing = ModItems.jar("green");
		Item speed = ModItems.jar("blue");
		Item mining = ModItems.jar("purple");
		Item resistance = ModItems.jar("yellow");
		Item vision = ModItems.jar("cyan");
		Item size = ModItems.jar("orange");
		Item lapsus = ModItems.jar("lime");
		Item flying = ModItems.jar("skyblue");

		Ingredient boneMeal = meta(Items.DYE, DyeColor.WHITE.itemMeta);
		Ingredient lapis = meta(Items.DYE, DyeColor.BLUE.itemMeta);
		NamespaceID water = ItemBucket.STATE_WATER;
		NamespaceID acid = ItemBucket.STATE_ACID;
		NamespaceID milk = ItemBucket.STATE_MILK;

		add(Items.JAR, any(Blocks.MUSHROOM_RED), boneMeal, water, antidote);
		add(antidote, any(Items.DUST_REDSTONE), any(Items.RUBYGLASS), water, damage);
		add(antidote, any(Items.OLIVINE), any(Items.SLIMEBALL), water, healing);
		add(antidote, lapis, any(Blocks.FLOWER_LIGHT_BLUE), water, speed);

		pair(any(Items.QUARTZ), damage, speed, mining, acid);
		pair(any(Items.DUST_GLOWSTONE), damage, healing, resistance, acid);
		pair(any(Items.GUNPOWDER), speed, healing, vision, acid);
		pair(any(Blocks.MUSHROOM_BROWN), mining, resistance, size, milk);
		pair(any(Items.SULFUR), resistance, vision, lapsus, milk);
		pair(any(Items.DUST_SUGAR), vision, mining, flying, milk);
	}

	public static void apply(ItemStack[] slots, Recipe r) {
		slots[SLOT_A] = consumeIngredient(slots[SLOT_A]);
		slots[SLOT_B] = consumeIngredient(slots[SLOT_B]);
		ItemStack bucket = slots[SLOT_BUCKET];
		slots[SLOT_BUCKET] = ((ItemBucket) bucket.getItem()).getContainerItemStack(bucket);
		slots[SLOT_BASE] = new ItemStack(r.result());
	}

	private static ItemStack consumeIngredient(ItemStack s) {
		if (s.getItem() instanceof ModPotionItem) return new ItemStack(Items.JAR);
		s.stackSize--;
		return s.stackSize <= 0 ? null : s;
	}

	public static Recipe find(ItemStack a, ItemStack b, ItemStack base, ItemStack bucket) {
		if (a == null || b == null || base == null || bucket == null) return null;
		if (base.stackSize != 1 || !(bucket.getItem() instanceof ItemBucket)) return null;
		NamespaceID liquid = ItemBucket.getState(bucket);
		for (Recipe r : RECIPES) {
			if (base.getItem() != r.base() || !liquid.equals(r.liquid())) continue;
			if ((r.a().matches(a) && r.b().matches(b)) || (r.a().matches(b) && r.b().matches(a))) return r;
		}
		return null;
	}

	public static boolean isIngredient(ItemStack s) {
		if (s == null) return false;
		for (Recipe r : RECIPES) {
			if (r.a().matches(s) || r.b().matches(s)) return true;
		}
		return false;
	}

	public static boolean isBase(ItemStack s) {
		return s != null && (s.getItem() == Items.JAR || s.getItem() instanceof ModPotionItem);
	}
}
