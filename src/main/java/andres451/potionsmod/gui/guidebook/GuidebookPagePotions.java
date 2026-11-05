package andres451.potionsmod.gui.guidebook;

import andres451.potionsmod.AlchemyRecipes;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.gui.guidebook.SlotGuidebook;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;

@Environment(EnvType.CLIENT)
public class GuidebookPagePotions extends GuidebookPage {
	private static final int TEXT = -12566464;
	private static final int ROWS_Y = 28;
	private static final int ROW_STEP = 28;
	private static final int BASE_X = 16, A_X = 36, B_X = 56, BUCKET_X = 76, ARROW_X = 98, RESULT_X = 126;

	private final Minecraft mc = Minecraft.getMinecraft();
	private final ItemElement itemElement = new ItemElement(mc);
	private final TooltipElement tooltipElement = new TooltipElement(mc);

	private final int recipeCount;
	private final List<SlotGuidebook> slots = new ArrayList<>();

	public GuidebookPagePotions(GuidebookSection section, List<AlchemyRecipes.Recipe> recipes) {
		super(section);
		this.recipeCount = recipes.size();
		for (int i = 0; i < recipeCount; i++) {
			AlchemyRecipes.Recipe r = recipes.get(i);
			int py = ROWS_Y + i * ROW_STEP;
			addSlot(new ItemStack(r.base()), BASE_X, py);
			addSlot(r.a().toStack(), A_X, py);
			addSlot(r.b().toStack(), B_X, py);
			addSlot(ItemBucket.createRecipeInput(Items.BUCKET_IRON, r.liquid()), BUCKET_X, py);
			addSlot(new ItemStack(r.result()), RESULT_X, py);
		}
	}

	private void addSlot(ItemStack stack, int px, int py) {
		slots.add(new SlotGuidebook(slots.size(), px, py, new RecipeSymbol(stack), false, null));
	}

	private boolean isOver(SlotGuidebook slot, int x, int y, int mouseX, int mouseY) {
		return mouseX >= x + slot.x - 1 && mouseX < x + slot.x + 17
			&& mouseY >= y + slot.y - 1 && mouseY < y + slot.y + 17;
	}

	@Override
	public void renderBackground(TextureManager re, int x, int y) {
		super.renderBackground(re, x, y);
		re.bindTexture(re.loadTexture("/assets/minecraft/textures/gui/container/guidebook/guidebook.png"));
		for (int i = 0; i < recipeCount; i++) {
			this.drawTexturedModalRect(x + ARROW_X, y + ROWS_Y + i * ROW_STEP + 1, 234, 0, 22, 15);
		}
	}

	@Override
	public void renderForeground(TextureManager re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
		this.drawStringCenteredNoShadow(fr, "Alchemy", x + 79, y + 12, TEXT);
		for (SlotGuidebook slot : slots) {
			this.drawSlot(x + slot.x - 1, y + slot.y - 1, -1);
			itemElement.render(slot.getItemStack(), x + slot.x, y + slot.y, isOver(slot, x, y, mouseX, mouseY), slot);
		}
	}

	@Override
	public void renderOverlay(TextureManager re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
		super.renderOverlay(re, fr, x, y, mouseX, mouseY, partialTicks);
		for (SlotGuidebook slot : slots) {
			if (isOver(slot, x, y, mouseX, mouseY) && slot.hasItem()) {
				GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				String text = tooltipElement.getTooltipText(slot.getItemStack(), DescriptionPromptEnum.showDescription(), slot);
				if (!text.isEmpty()) tooltipElement.render(text, mouseX, mouseY, 8, -8);
				return;
			}
		}
	}
}
