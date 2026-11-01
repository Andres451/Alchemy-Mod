package andres451.potionsmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

@Environment(EnvType.CLIENT)
public class PotionsModClient implements ClientModInitializer {
	private static final ModModels MODELS = new ModModels();

	@Override
	public void onInitializeClient() {
		Key key = Key.of(PotionsMod.MOD_ID);
		ClientEvents.BLOCK_MODEL_RELOAD.listen(key, MODELS::initBlockModels);
		ClientEvents.ITEM_MODEL_RELOAD.listen(key, MODELS::initItemModels);
		ClientEvents.AFTER_CLIENT_START.listen(key, this::afterClientStart);
	}

	private void afterClientStart() {
		MODELS.registerEffectRenderers();
		ModEffects.nightVisionApply = NightVisionClient::apply;
		ModEffects.nightVisionRestore = NightVisionClient::restore;
	}
}
