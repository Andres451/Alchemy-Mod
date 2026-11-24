package andres451.potionsmod.alchemy_table;

import java.util.ArrayList;
import java.util.List;

import andres451.potionsmod.PotionVariant;
import andres451.potionsmod.PotionsMod;
import andres451.potionsmod.registry.ModItems;
import andres451.potionsmod.registry.ModPotionItem;
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
		public static final int ANY = -1;
		public boolean matches(ItemStack s) {
			if (item == null || s == null) return false;
			if (s.itemID != item.getDefaultStack().itemID) return false;
			return meta == ANY || s.getMetadata() == meta;
		}
		public ItemStack toStack() {
			return meta == ANY ? item.getDefaultStack() : new ItemStack(item, 1, meta);
		}
	}

	public record Recipe(Item base, Ingredient a, Ingredient b, NamespaceID liquid, Item result) {}

	private static final List<Recipe> RECIPES = new ArrayList<>();

	private static Ingredient any(IItemConvertible item) { return new Ingredient(item, Ingredient.ANY); }

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

	private static void triple(Item p1, Item p2, Item p3, Item result, NamespaceID liquid) {
		add(p1, any(p2), any(p3), liquid, result);
		add(p2, any(p1), any(p3), liquid, result);
		add(p3, any(p1), any(p2), liquid, result);
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
		Item somnia = ModItems.jar("pink");
		Item inventory = ModItems.jar("magenta");
		Item reach = ModItems.jar("lightmagenta");
		Item reposition = ModItems.jar("black");
		Item astral = ModItems.jar("ultramarine");
		Item popping = ModItems.jar("chartreuse");
		Item vines = ModItems.jar("lightcyan");
		Item farm = ModItems.jar("bronze");

		Ingredient boneMeal = meta(Items.DYE, DyeColor.WHITE.itemMeta);
		Ingredient lapis = meta(Items.DYE, DyeColor.BLUE.itemMeta);
		Ingredient inkSack = meta(Items.DYE, DyeColor.BLACK.itemMeta);

		NamespaceID water = ItemBucket.STATE_WATER;
		NamespaceID acid = ItemBucket.STATE_ACID;
		NamespaceID milk = ItemBucket.STATE_MILK;

		add(Items.JAR, any(Blocks.MUSHROOM_RED), boneMeal, water, antidote);
		add(antidote, any(Items.DUST_REDSTONE), any(Items.RUBYGLASS), water, damage);
		add(antidote, any(Items.OLIVINE), any(Items.SLIMEBALL), water, healing);
		add(antidote, lapis, any(Blocks.FLOWER_LIGHT_BLUE), water, speed);

		pair(any(Items.QUARTZ), speed, damage, mining, acid);
		pair(any(Items.DUST_GLOWSTONE), damage, healing, resistance, acid);
		pair(any(Items.GUNPOWDER), healing, speed, vision, acid);
		triple(damage, healing, speed, inventory, acid);

		pair(any(Blocks.MUSHROOM_BROWN), mining, resistance, size, milk);
		pair(any(Items.SULFUR), resistance, vision, lapsus, milk);
		pair(any(Items.DUST_SUGAR), vision, mining, flying, milk);
		triple(mining, resistance, vision, somnia, milk);

		pair(any(Blocks.RUBYGLASS_GROWTH), size, damage, reach, milk);
		pair(any(Blocks.ALGAE), lapsus, healing, vines, milk);
		pair(inkSack, flying, speed, reposition, milk);

		pair(any(Blocks.DEADBUSH), size, mining, farm, acid);
		pair(any(Items.FEATHER_CHICKEN), lapsus, resistance, popping, acid);
		pair(any(Blocks.SOUL_CATCHER), flying, vision, astral, acid);

		Ingredient greenDye = meta(Items.DYE, DyeColor.GREEN.itemMeta);
		Ingredient brownDye = meta(Items.DYE, DyeColor.BROWN.itemMeta);

		for (String c : ModItems.JAR_COLORS) {
			if (c.equals("white")) continue;
			Item normal = ModItems.jar(c);
			Item diluted = ModItems.jar(c, PotionVariant.DILUTED);
			Item dense = ModItems.jar(c, PotionVariant.DENSE);

			add(normal, any(antidote), greenDye, water, diluted);
			add(normal, any(normal), brownDye, water, dense);

			add(diluted, any(antidote), boneMeal, water, normal);
			add(dense, any(antidote), boneMeal, water, normal);
		}
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

	public static List<Recipe> all() {
		return RECIPES;
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

	public static List<List<Recipe>> groupForGuidebook(List<Recipe> recipes) {
		List<List<Recipe>> rows = new ArrayList<>();
		List<Recipe> diluted = new ArrayList<>();
		List<Recipe> dense = new ArrayList<>();
		List<Recipe> revert = new ArrayList<>();
		for (Recipe r : recipes) {
			PotionVariant base = variantOfItem(r.base());
			PotionVariant result = variantOfItem(r.result());
			if (result == PotionVariant.NORMAL && base != PotionVariant.NORMAL) revert.add(r);
			else if (result == PotionVariant.DILUTED) diluted.add(r);
			else if (result == PotionVariant.DENSE) dense.add(r);
			else rows.add(List.of(r));
		}
		if (!diluted.isEmpty()) rows.add(diluted);
		if (!dense.isEmpty()) rows.add(dense);
		if (!revert.isEmpty()) rows.add(revert);
		return rows;
	}

	private static PotionVariant variantOfItem(Item item) {
		return item instanceof ModPotionItem m ? m.variant() : PotionVariant.NORMAL;
	}
}
