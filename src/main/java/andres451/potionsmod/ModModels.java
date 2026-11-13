package andres451.potionsmod;

import static andres451.potionsmod.PotionsMod.MOD_ID;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.effects.api.effect.Effect;
import sunsetsatellite.catalyst.effects.api.effect.render.EffectRenderer;
import sunsetsatellite.catalyst.effects.api.effect.render.EffectRendererDispatcher;

public class ModModels {

	public void initBlockModels(BlockModelDispatcher dispatcher) {
		addTableModel(dispatcher, ModBlocks.alchemyTable, "");
		addTableModel(dispatcher, ModBlocks.alchemyTableActive, "_active");
	}

	private static void addTableModel(BlockModelDispatcher dispatcher, Block<?> block, String suffix) {
		dispatcher.addDispatch(new BlockModelHorizontalRotation<>(block)
			.setTex(MOD_ID + ":block/alchemy_front" + suffix, Side.NORTH)
			.setTex(MOD_ID + ":block/alchemy_back" + suffix, Side.SOUTH)
			.setTex(MOD_ID + ":block/alchemy_left" + suffix, Side.EAST)
			.setTex(MOD_ID + ":block/alchemy_right" + suffix, Side.WEST)
			.setTex(MOD_ID + ":block/alchemy_top" + suffix, Side.TOP)
			.setTex(MOD_ID + ":block/alchemy_bottom" + suffix, Side.BOTTOM));
	}

	public void initItemModels(ItemModelDispatcher dispatcher) {
		for (int i = 0; i < ModItems.JAR_COLORS.length; i++) {
			dispatcher.addDispatch(makeModel(ModItems.jars[i], "jar_" + ModItems.JAR_COLORS[i]));
		}
	}

	public void registerEffectRenderers() {
		addEffectRenderer(ModEffects.REGEN, "regen.png");
		addEffectRenderer(ModEffects.SPEED, "speed.png");
		addEffectRenderer(ModEffects.DAMAGE_BOOST, "damage.png");
		addEffectRenderer(ModEffects.DIG_SPEED, "mining.png");
		addEffectRenderer(ModEffects.RESISTANCE, "resistance.png");
		addEffectRenderer(ModEffects.SMALL, "size.png");
		addEffectRenderer(ModEffects.FLIGHT, "fly.png");
		addEffectRenderer(ModEffects.NIGHT_VISION, "vision.png");
		addEffectRenderer(ModEffects.INVISIBLE, "lapsus.png");
		addEffectRenderer(ModEffects.INVENTORY, "inventory.png");
		addEffectRenderer(ModEffects.SOMNIA, "somnia.png");
		addEffectRenderer(ModEffects.REACH, "reach.png");
		addEffectRenderer(ModEffects.REPOSITION, "reposition.png");
		addEffectRenderer(ModEffects.ASTRAL, "astral.png");
	}

	private static void addEffectRenderer(Effect effect, String iconFile) {
		EffectRenderer<Effect> renderer = new EffectRenderer<Effect>(effect).setIcon(iconFile);
		EffectRendererDispatcher.getInstance().addDispatch(effect, renderer);
	}

	public static @NotNull ItemModelStandard makeModel(@NotNull final Item item, @NotNull final String textureValue) {
		return setIcon(new ItemModelStandard(item, null), NamespaceID.getTemp(MOD_ID, "item/" + textureValue));
	}

	public static <T extends ItemModelStandard> @NotNull T setIcon(@NotNull final T model, @NotNull final String texture) {
		model.icon = TextureRegistry.getTexture(texture);
		return model;
	}

	public static <T extends ItemModelStandard> @NotNull T setIcon(@NotNull final T model, @NotNull final NamespaceID texture) {
		model.icon = TextureRegistry.getTexture(texture);
		return model;
	}
}
