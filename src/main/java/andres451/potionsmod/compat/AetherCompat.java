package andres451.potionsmod.compat;

import java.util.ArrayList;
import java.util.List;

import andres451.potionsmod.PotionsMod;
import net.fabricmc.loader.api.FabricLoader;

public class AetherCompat {

	private static final String[] BOSS_CLASSES = {
		"teamport.aether.entity.boss.slider.MobBossSlider",
		"teamport.aether.entity.boss.valkyrie.MobBossValkyrie",
		"teamport.aether.entity.boss.sunspirit.MobBossSunspirit"
	};

	private static List<Class<?>> bosses;

	private static void init() {
		bosses = new ArrayList<>();
		if (!FabricLoader.getInstance().isModLoaded("aether")) return;
		for (String name : BOSS_CLASSES) {
			try {
				bosses.add(Class.forName(name));
			} catch (ClassNotFoundException e) {
				PotionsMod.LOGGER.warn("PotionMod.AetherCompat: boss class not found: {}", name);
			}
		}
	}

	public static boolean isBoss(Object mob) {
		if (bosses == null) init();
		for (Class<?> c : bosses) {
			if (c.isInstance(mob)) return true;
		}
		return false;
	}
}
