package andres451.potionsmod.gui.guidebook;

import java.util.*;

import andres451.potionsmod.AlchemyRecipes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.gui.guidebook.SearchableGuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;

@Environment(EnvType.CLIENT)
public class GuidebookSectionPotions extends SearchableGuidebookSection {
	private static final int PER_PAGE = 6;

	private final List<GuidebookPage> pages = new ArrayList<>();
	private int builtFor = -1;

	public GuidebookSectionPotions(String translationKey, ItemStack tabIcon, int bgColor, int fgColor) {
		super(translationKey, tabIcon, bgColor, fgColor);
	}

	@Override
	public List<GuidebookPage> getPages() {
		List<AlchemyRecipes.Recipe> all = uniqueRecipes();
		if (builtFor != all.size()) {
			pages.clear();
			if (all.isEmpty()) {
				pages.add(new GuidebookPagePotions(this, all));
			} else {
				for (int i = 0; i < all.size(); i += PER_PAGE) {
					pages.add(new GuidebookPagePotions(this, new ArrayList<>(all.subList(i, Math.min(i + PER_PAGE, all.size())))));
				}
			}
			builtFor = all.size();
		}
		return pages;
	}

	private static String key(AlchemyRecipes.Ingredient i) {
		return i.item().getDefaultStack().itemID + ":" + i.meta();
	}

	private static List<AlchemyRecipes.Recipe> uniqueRecipes() {
		List<AlchemyRecipes.Recipe> out = new ArrayList<>();
		Set<String> seen = new HashSet<>();
		for (AlchemyRecipes.Recipe r : AlchemyRecipes.all()) {
			List<String> parts = new ArrayList<>();
			parts.add(r.base().id + ":" + AlchemyRecipes.Ingredient.ANY);
			parts.add(key(r.a()));
			parts.add(key(r.b()));
			Collections.sort(parts);
			String k = r.result().id + "|" + r.liquid() + "|" + parts;
			if (seen.add(k)) out.add(r);
		}
		return out;
	}

	@Override
	public List<GuidebookPage> searchPages(SearchQuery query) {
		return Collections.emptyList();
	}

	@Override
	public List<GuidebookSection.Index> getIndices() {
		return Collections.emptyList();
	}
}
