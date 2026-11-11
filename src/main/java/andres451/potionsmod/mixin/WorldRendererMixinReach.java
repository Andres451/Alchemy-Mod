package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, remap = false)
public abstract class WorldRendererMixinReach {

	@Redirect(method = "getMouseOver",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/player/gamemode/Gamemode;getBlockReachDistance()F"))
	private float potions$blockReach(Gamemode gamemode) {
		float reach = gamemode.getBlockReachDistance();
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer != null && ModEffects.has(mc.thePlayer, ModEffects.REACH)) return reach * 2.0F;
		return reach;
	}

	@Redirect(method = "getMouseOver",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/player/gamemode/Gamemode;getEntityReachDistance()F"))
	private float potions$entityReach(Gamemode gamemode) {
		float reach = gamemode.getEntityReachDistance();
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer != null && ModEffects.has(mc.thePlayer, ModEffects.REACH)) return reach * 2.0F;
		return reach;
	}
}
