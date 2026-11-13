package andres451.potionsmod.mixin;

import andres451.potionsmod.EntityCameraAstral;
import andres451.potionsmod.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.camera.EntityCamera;
import net.minecraft.client.render.camera.EntityCameraFirstPerson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, remap = false)
public abstract class WorldRendererMixinAstral {

	@Inject(method = "setupCamera", at = @At("TAIL"))
	private void potions$camera(float partialTick, CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null || !(mc.activeCamera instanceof EntityCamera)) return;
		boolean want = ModEffects.has(mc.thePlayer, ModEffects.ASTRAL) && !mc.thePlayer.isPlayerSleeping();
		boolean has = mc.activeCamera instanceof EntityCameraAstral;
		if (want && !has) {
			mc.activeCamera = new EntityCameraAstral(mc, mc.thePlayer);
		} else if (!want && has) {
			mc.activeCamera = new EntityCameraFirstPerson(mc, mc.thePlayer);
		}
	}

	@Redirect(method = "updateCameraAndRender",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/entity/player/PlayerLocal;interpolateTurn(FF)V"))
	private void potions$turn(PlayerLocal p, float yaw, float pitch) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.activeCamera instanceof EntityCameraAstral cam && !GameSettings.KEY_SPRINT.isPressed()) {
			cam.turn(yaw, pitch);
		} else {
			p.interpolateTurn(yaw, pitch);
		}
	}
}
