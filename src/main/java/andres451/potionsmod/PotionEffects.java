package andres451.potionsmod;

import net.minecraft.core.entity.player.Player;

public class PotionEffects {
	public static void apply(String color, Player player) {
		switch (color) {
			case "red":     ModEffects.give(player, ModEffects.DAMAGE_BOOST, 200, 1); break;
			case "green":   ModEffects.give(player, ModEffects.REGEN, 200, 1); break;
			case "blue":    ModEffects.give(player, ModEffects.SPEED, 200, 1); break;
			case "purple":  ModEffects.give(player, ModEffects.DIG_SPEED, 200, 1); break;
			case "yellow":  ModEffects.give(player, ModEffects.RESISTANCE, 200, 1); break;
			case "orange":  ModEffects.give(player, ModEffects.SMALL, 300, 1);break;
			case "cyan":    ModEffects.give(player, ModEffects.NIGHT_VISION, 200, 1);break;
			case "lime":    ModEffects.give(player, ModEffects.INVISIBLE, 300, 1);break;
			case "skyblue": ModEffects.give(player, ModEffects.FLIGHT, 300, 1);break;
			case "white":   ModEffects.clear(player); break;
			default: break;
		}
	}
}
