package andres451.potionsmod.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.camera.EntityCameraFirstPerson;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.MathHelper;

import static andres451.potionsmod.Constants.*;

@Environment(EnvType.CLIENT)
public class EntityCameraAstral extends EntityCameraFirstPerson {

	private final Minecraft client;
	private double x, y, z, xo, yo, zo, vx, vy, vz;
	private float yRot, xRot, yRotO, xRotO;

	public EntityCameraAstral(Minecraft mc, Mob entity) {
		super(mc, entity);
		this.client = mc;
		x = xo = entity.x;
		y = yo = entity.y + entity.getHeadHeight();
		z = zo = entity.z;
		yRot = yRotO = entity.yRot;
		xRot = xRotO = entity.xRot;
	}

	@Override
	public void tick() {
		xo = x; yo = y; zo = z;
		yRotO = yRot; xRotO = xRot;
		if (client.currentScreen == null) {
			float f = (GameSettings.KEY_FORWARD.isPressed() ? 1 : 0) - (GameSettings.KEY_BACK.isPressed() ? 1 : 0);
			float s = (GameSettings.KEY_LEFT.isPressed() ? 1 : 0) - (GameSettings.KEY_RIGHT.isPressed() ? 1 : 0);
			double yaw = Math.toRadians(yRot);
			double sin = Math.sin(yaw), cos = Math.cos(yaw);
			vx += (-f * sin + s * cos) * ASTRAL_ACCEL;
			vz += (f * cos + s * sin) * ASTRAL_ACCEL;
			if (GameSettings.KEY_JUMP.isPressed()) vy += ASTRAL_ACCEL;
			if (GameSettings.KEY_SNEAK.isPressed()) vy -= ASTRAL_ACCEL;
		}
		x += vx; y += vy; z += vz;
		vx *= ASTRAL_HORIZONTAL_FRICTION; vz *= ASTRAL_HORIZONTAL_FRICTION; vy *= ASTRAL_VERTICAL_FRICTION;
	}

	public void turn(float yawDelta, float pitchDelta) {
		float xc = xRot, yc = yRot;
		yRot += yawDelta * 0.15F;
		xRot = MathHelper.clamp(xRot - pitchDelta * 0.15F, -90.0F, 90.0F);
		xRotO += xRot - xc;
		yRotO += yRot - yc;
	}

	@Override
	public boolean showPlayer() {
		double dx = x - mob.x, dy = y - (mob.y + mob.getHeadHeight()), dz = z - mob.z;
		return dx * dx + dy * dy + dz * dz > 0.5;
	}

	@Override public double getX(float t) { return xo + (x - xo) * t; }
	@Override public double getY(float t) { return yo + (y - yo) * t; }
	@Override public double getZ(float t) { return zo + (z - zo) * t; }
	@Override public double getXRot(float t) { return xRotO + (xRot - xRotO) * t; }
	@Override public double getYRot(float t) { return yRotO + (yRot - yRotO) * t; }
}
