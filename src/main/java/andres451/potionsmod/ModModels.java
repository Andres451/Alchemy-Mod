package andres451.potionsmod;


import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.util.ModelEntrypoint;

import static andres451.potionsmod.PotionsMod.LOGGER;
import static andres451.potionsmod.PotionsMod.MOD_ID;

import sunsetsatellite.catalyst.effects.api.effect.Effect;
import sunsetsatellite.catalyst.effects.api.effect.render.EffectRenderer;
import sunsetsatellite.catalyst.effects.api.effect.render.EffectRendererDispatcher;

public class ModModels implements ModelEntrypoint {

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		LOGGER.info("Block Models initialized.");
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		for (int i = 0; i < ModItems.JAR_COLORS.length; i++) {
			dispatcher.addDispatch(makeModel(ModItems.jars[i], "jar_" + ModItems.JAR_COLORS[i]));
		}

		addEffectRenderer(ModEffects.REGEN, "jar_green", 0x8000AA00);
		addEffectRenderer(ModEffects.DAMAGE_BOOST, "jar_red", 0x80CC0000);
		addEffectRenderer(ModEffects.SPEED, "jar_blue", 0x800055FF);
		addEffectRenderer(ModEffects.DIG_SPEED, "jar_purple", 0x80AA00FF);
		addEffectRenderer(ModEffects.RESISTANCE, "jar_yellow", 0x80FFDD00);
		addEffectRenderer(ModEffects.SMALL, "jar_orange", 0x80FF8800);
		addEffectRenderer(ModEffects.FLIGHT, "jar_skyblue", 0x8055CCFF);
		addEffectRenderer(ModEffects.NIGHT_VISION, "jar_cyan", 0x8000CCCC);
		addEffectRenderer(ModEffects.INVISIBLE, "jar_lime", 0x8088FF00);
		ModEffects.nightVisionApply = NightVisionClient::apply;
		ModEffects.nightVisionRestore = NightVisionClient::restore;
		LOGGER.info("Item Models initialized.");
	}
public static @NotNull ItemModelStandard makeModel(@NotNull final Item item, @NotNull final String textureValue) {
		return setIcon(new ItemModelStandard(item, null), NamespaceID.getTemp(MOD_ID, "item/" + textureValue));
	}

	private static void addEffectRenderer(Effect effect, String texture, int argbColor) {
		EffectRenderer<Effect> renderer = new EffectRenderer<Effect>(effect)
			.setIcon(TextureRegistry.getTexture(MOD_ID + ":item/" + texture));
		renderer.setColor(argbColor);
		EffectRendererDispatcher.getInstance().addDispatch(effect, renderer);
	}

	public static <T extends ItemModelStandard> @NotNull T setIcon(@NotNull final T model, @NotNull final String texture) {
		model.icon = TextureRegistry.getTexture(texture);
		return model;
	}

	public static <T extends ItemModelStandard> @NotNull T setIcon(@NotNull final T model, @NotNull final NamespaceID texture) {
		model.icon = TextureRegistry.getTexture(texture);
		return model;
	}
	@Override
	public void initEntityModels(EntityRendererDispatcher dispatcher) {}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {}
}
