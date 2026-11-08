package andres451.potionsmod;

import net.minecraft.core.entity.player.Player;

import static andres451.potionsmod.ModEffects.*;
import sunsetsatellite.catalyst.effects.api.effect.Effect;

public class PotionEffects {

	public static final int DAMAGE_BOOST_DURATION = 300;
	public static final int REGEN_DURATION = 300;
	public static final int SPEED_DURATION = 300;

	public static final int DIG_SPEED_DURATION = 1200;
	public static final int RESISTANCE_DURATION = 1200;
	public static final int NIGHT_VISION_DURATION = 1200;

	public static final int SMALL_DURATION = 300;
	public static final int INVISIBLE_DURATION = 300;
	public static final int FLIGHT_DURATION = 300;

	public static final int MORTALITY_DURATION = 60;
	public static final int SOMNIA_DURATION = 60;

	public static void apply(String color, Player player) {
		switch (color) {
			case "red":     ModEffects.give(player, DAMAGE_BOOST, DAMAGE_BOOST_DURATION, 1); break;
			case "green":   ModEffects.give(player, REGEN, REGEN_DURATION, 1); break;
			case "blue":    ModEffects.give(player, SPEED, SPEED_DURATION, 1); break;
			case "purple":  ModEffects.give(player, DIG_SPEED, DIG_SPEED_DURATION, 1); break;
			case "yellow":  ModEffects.give(player, RESISTANCE, RESISTANCE_DURATION, 1); break;
			case "orange":  ModEffects.give(player, SMALL, SMALL_DURATION, 1);break;
			case "cyan":    ModEffects.give(player, NIGHT_VISION, NIGHT_VISION_DURATION, 1);break;
			case "lime":    ModEffects.give(player, INVISIBLE, INVISIBLE_DURATION, 1);break;
			case "skyblue": ModEffects.give(player, FLIGHT, FLIGHT_DURATION, 1);break;
			case "magenta": ModEffects.give(player, MORTALITY, MORTALITY_DURATION, 1);break;
			case "pink":    ModEffects.give(player, SOMNIA, SOMNIA_DURATION, 1); break;
			case "white":   ModEffects.clear(player); break;
			default: break;
		}
	}
}
