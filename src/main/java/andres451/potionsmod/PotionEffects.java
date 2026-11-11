package andres451.potionsmod;

import net.minecraft.core.entity.player.Player;

import static andres451.potionsmod.ModEffects.*;

import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.pos.TilePos;
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

	public static final int INVENTORY_DURATION = 60;
	public static final int SOMNIA_DURATION = 60;

	public static final int REACH_DURATION = 1200;
	public static final int REPOSITION_DURATION = 60;

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
			case "magenta": ModEffects.give(player, INVENTORY, INVENTORY_DURATION, 1);break;
			case "pink":    ModEffects.give(player, SOMNIA, SOMNIA_DURATION, 1); break;
			case "lightmagenta": ModEffects.give(player, REACH, REACH_DURATION, 1); break;
			case "black": startReposition(player); break;
			case "white":   ModEffects.clear(player); break;
			default: break;
		}
	}

	private static final double REPOSITION_RANGE = 200;
	private static final java.util.Map<Player, double[]> REPOSITION_TARGETS = new java.util.WeakHashMap<>();

	private static void startReposition(Player p) {
		HitResult hit = p.rayCast(REPOSITION_RANGE, 1.0F, false, false, true);
		var look = p.getViewVector(1.0F);
		double x, y, z;
		if (hit == null) {
			var eye = p.getPosition(1.0F, true);
			x = eye.x() + look.x() * REPOSITION_RANGE;
			y = eye.y() + look.y() * REPOSITION_RANGE;
			z = eye.z() + look.z() * REPOSITION_RANGE;
		} else {
			x = hit.location.x() - look.x() * 0.6;
			y = hit.location.y() - look.y() * 0.6;
			z = hit.location.z() - look.z() * 0.6;
		}
		int bx = MathHelper.floor(x), by = MathHelper.floor(y), bz = MathHelper.floor(z);
		if (!p.world.isAirBlock(new TilePos(bx, by, bz)) || !p.world.isAirBlock(new TilePos(bx, by + 1, bz))) {
			by += 1;
		}
		REPOSITION_TARGETS.put(p, new double[]{x, by, z, p.dimension});
		ModEffects.give(p, REPOSITION, REPOSITION_DURATION, 1);
	}

	private static final String REPOSITION_SOUND = "potionsmod:teleport";

	public static void finishReposition(Player p) {
		double[] t = REPOSITION_TARGETS.remove(p);
		if (t == null || (int) t[3] != p.dimension) return;

		p.world.playSoundAtEntity(null, p, REPOSITION_SOUND, 0.7F, 0.6F);

		p.moveTo(t[0], t[1], t[2], p.yRot, p.xRot);
		if (p instanceof ITeleportSync s) s.potions$syncTeleport();
		p.fallDistance = 0.0F;

		p.world.playSoundAtEntity(null, p, REPOSITION_SOUND, 0.7F, 1.0F);
	}
}
