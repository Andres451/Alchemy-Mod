package andres451.potionsmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
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
		CommonEvents.BEFORE_GAME_START.listen(Key.of(MOD_ID), this::beforeGameStart);
		CommonEvents.AFTER_GAME_START.listen(Key.of(MOD_ID), this::afterGameStart);
		ModEffects.init();
		LOGGER.info("PotionsMod initialized.");
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
