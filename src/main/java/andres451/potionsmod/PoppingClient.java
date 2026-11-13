package andres451.potionsmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;

@Environment(EnvType.CLIENT)
public class PoppingClient {
	public static PotionEffects.Hop pending;

	public static void hop(PotionEffects.Hop h) {
		pending = h;
		try {
			Minecraft.getMinecraft().usePortal(h.dim(), null);
		} finally {
			pending = null;
		}
	}

	public static void place(World w, Entity e, PotionEffects.Hop h) {
		double[] d = PotionEffects.destination(w, h);
		if (d == null) return;
		e.moveTo(d[0], d[1], d[2], e.yRot, e.xRot);
		e.xd = e.yd = e.zd = 0.0;
		e.fallDistance = 0.0F;
	}
}
