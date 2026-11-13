package andres451.potionsmod;

import net.minecraft.core.entity.player.Player;

import static andres451.potionsmod.ModEffects.*;

import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.provider.ChunkProvider;
import net.minecraft.core.world.pos.TilePos;

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

	public static final int ASTRAL_DURATION = 400;
	public static final int POPPING_DURATION = 200;

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
			case "ultramarine": startAstral(player); break;
			case "black": startReposition(player); break;
			case "chartreuse": startPopping(player); break;
			case "white":
				POPPING_ORIGIN.remove(player);
				ModEffects.clear(player);
				break;
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

	private static void startAstral(Player p) {
		if (ModEffects.has(p, ModEffects.ASTRAL)) return;
		ModEffects.give(p, ASTRAL, ASTRAL_DURATION, 1);
	}

	public record Hop(int dim, int mode, double x, double y, double z) {}

	private static final java.util.Map<Player, double[]> POPPING_ORIGIN = new java.util.WeakHashMap<>();

	private static void loadChunk(World w, int x, int z) {
		ChunkProvider cp = w.getChunkProvider();
		cp.setCurrentChunkOver(x >> 4, z >> 4);
		cp.prepareChunk(x >> 4, z >> 4);
	}

	public static double[] destination(World w, Hop h) {
		if (h.mode() == 0) { // COORDS: SOMNIA OR DRINK POINT
			loadChunk(w, MathHelper.floor(h.x()), MathHelper.floor(h.z()));
			return new double[]{h.x(), h.y(), h.z()};
		}
		TilePos pos = null;
		if (h.mode() == 1) { // VALID BED
			TilePos sp = new TilePos(MathHelper.floor(h.x()), MathHelper.floor(h.y()), MathHelper.floor(h.z()));
			loadChunk(w, sp.x, sp.z);
			pos = Player.getValidBedSpawnCoordinates(w, sp);
		}
		if (pos == null) { // SPAWN POINT
			TilePos ws = w.getSpawnPoint();
			loadChunk(w, ws.x, ws.z);
			int ty = w.findTopSolidBlock(ws.x, ws.z);
			if (ty > 0) pos = new TilePos(ws.x, ty, ws.z);
		}
		return pos == null ? null : new double[]{pos.x + 0.5, pos.y + 0.1, pos.z + 0.5};
	}

	private static boolean travel(Player p, Hop h) {
		if (p.dimension != h.dim()) {
			((IDimensionHop) p).potions$hop(h);
			return true;
		}
		double[] d = destination(p.world, h);
		if (d == null) return false;
		p.moveTo(d[0], d[1], d[2], p.yRot, p.xRot);
		if (p instanceof ITeleportSync s) s.potions$syncTeleport();
		return true;
	}

	private static void startPopping(Player p) {
		if (POPPING_ORIGIN.containsKey(p)) return;
		int bedDim = ((ISomnia) p).potions$getBedlessDim();
		int dim = bedDim >= 0 ? bedDim : 0;
		TilePos sp = p.getPlayerSpawnPoint();
		Hop h = (bedDim >= 0 && sp != null) ? new Hop(dim, 0, sp.x + 0.5, sp.y + 0.1, sp.z + 0.5)
			: sp != null ? new Hop(dim, 1, sp.x, sp.y, sp.z)
			: new Hop(dim, 2, 0, 0, 0);

		POPPING_ORIGIN.put(p, new double[]{p.x, p.y, p.z, p.yRot, p.xRot, p.dimension});
		if (!travel(p, h)) { POPPING_ORIGIN.remove(p); return; }
		p.fallDistance = 0.0F;
		p.world.playSoundAtEntity(null, p, REPOSITION_SOUND, 1.0F, 1.0F);
		ModEffects.give(p, POPPING, POPPING_DURATION, 1);
	}

	public static void finishPopping(Player p) {
		double[] o = POPPING_ORIGIN.remove(p);
		if (o == null) return;
		travel(p, new Hop((int) o[5], 0, o[0], o[1], o[2]));
		p.fallDistance = 0.0F;
		p.world.playSoundAtEntity(null, p, REPOSITION_SOUND, 1.0F, 1.0F);
	}
}
