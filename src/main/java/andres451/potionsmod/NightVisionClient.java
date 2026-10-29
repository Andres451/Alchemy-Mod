package andres451.potionsmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;

@Environment(EnvType.CLIENT)
public class NightVisionClient {
	private static final float BRIGHTNESS_LEVEL = 1.0F;
	private static final float GAMMA_LEVEL = 0.65F;

	private static boolean active = false;
	private static float savedBrightness;
	private static float savedGamma;

	public static void apply() {
		if (!active) {
			savedBrightness = GameSettings.BRIGHTNESS.value;
			savedGamma = GameSettings.GAMMA.value;
			active = true;
		}
		GameSettings.BRIGHTNESS.value = Math.max(savedBrightness, BRIGHTNESS_LEVEL);
		GameSettings.GAMMA.value = Math.max(savedGamma, GAMMA_LEVEL);
	}

	public static void restore() {
		if (active) {
			GameSettings.BRIGHTNESS.value = savedBrightness;
			GameSettings.GAMMA.value = savedGamma;
			active = false;
		}
	}

	public static void update(Minecraft mc) {
		boolean want = mc.thePlayer != null && ModEffects.has(mc.thePlayer, ModEffects.NIGHT_VISION);
		if (want) {
			apply();
		} else {
			restore();
		}
	}
}
