package andres451.potionsmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.dependency.Key;
import net.minecraft.core.sound.SoundTypes;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.util.collection.NamespaceID;

public class PotionsMod implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("potionsmod", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int ALCHEMY_TABLE_SCREEN_ID = 50;

	@Override
	public void onInitialize() {
		Key key = Key.of(MOD_ID);
		CommonEvents.BEFORE_GAME_START.listen(key, this::beforeGameStart);
		CommonEvents.AFTER_GAME_START.listen(key, this::afterGameStart);
		CommonEvents.RECIPES_NAMESPACE_INIT.listen(key, () -> RecipeBuilder.initNameSpace(MOD_ID));
		CommonEvents.RECIPES_READY.listen(key, ModRecipes::init);

		ModEffects.init();
	}

	public void beforeGameStart() {
		ModItems.initItems();
		ModBlocks.initBlocks();
		TileEntityDispatcher.addMapping(TileEntityAlchemyTable.class, NamespaceID.fromPool(MOD_ID, "alchemy_table"));
		SoundTypes.register("potionsmod.drink");
		SoundTypes.loadSoundsJson("potionsmod");
	}

	public void afterGameStart() {
		AlchemyRecipes.init();
	}
}
