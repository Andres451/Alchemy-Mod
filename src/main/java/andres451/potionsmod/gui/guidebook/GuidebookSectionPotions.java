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

	private List<AlchemyRecipes.Recipe> unique = Collections.emptyList();
	private int uniqueFor = -1;

	public GuidebookSectionPotions(String translationKey, ItemStack tabIcon, int bgColor, int fgColor) {
		super(translationKey, tabIcon, bgColor, fgColor);
	}

	@Override
	public List<GuidebookPage> getPages() {
		List<AlchemyRecipes.Recipe> all = unique();
		if (builtFor != all.size()) {
			pages.clear();
			pages.addAll(buildPages(all));
			builtFor = all.size();
		}
		return pages;
	}

	@Override
	public List<GuidebookPage> searchPages(SearchQuery query) {
		if (query.query.getLeft() == SearchQuery.QueryType.GROUP) return Collections.emptyList();
		if (query.scope.getLeft() != SearchQuery.SearchScope.NONE) return Collections.emptyList();

		String q = query.query.getRight().trim().toLowerCase();
		if (q.isEmpty()) return getPages();

		boolean wantResult = query.mode != SearchQuery.SearchMode.USAGE;
		boolean wantInputs = query.mode != SearchQuery.SearchMode.RECIPE;

		List<AlchemyRecipes.Recipe> found = new ArrayList<>();
		for (AlchemyRecipes.Recipe r : unique()) {
			boolean hit = (wantResult && nameMatches(new ItemStack(r.result()), q, query.strict))
				|| (wantInputs && (nameMatches(new ItemStack(r.base()), q, query.strict)
				|| nameMatches(r.a().toStack(), q, query.strict)
				|| nameMatches(r.b().toStack(), q, query.strict)));
			if (hit) found.add(r);
		}
		if (found.isEmpty()) return Collections.emptyList();
		return buildPages(found);
	}

	@Override
	public List<GuidebookSection.Index> getIndices() {
		return Collections.emptyList();
	}

	private List<AlchemyRecipes.Recipe> unique() {
		int n = AlchemyRecipes.all().size();
		if (uniqueFor != n) {
			unique = uniqueRecipes();
			uniqueFor = n;
		}
		return unique;
	}

	private List<GuidebookPage> buildPages(List<AlchemyRecipes.Recipe> recipes) {
		List<GuidebookPage> out = new ArrayList<>();
		if (recipes.isEmpty()) {
			out.add(new GuidebookPagePotions(this, recipes));
			return out;
		}
		for (int i = 0; i < recipes.size(); i += PER_PAGE) {
			out.add(new GuidebookPagePotions(this, new ArrayList<>(recipes.subList(i, Math.min(i + PER_PAGE, recipes.size())))));
		}
		return out;
	}

	private static boolean nameMatches(ItemStack s, String q, boolean strict) {
		String n = s.getDisplayName().toLowerCase();
		return strict ? n.equals(q) : n.contains(q);
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
}
