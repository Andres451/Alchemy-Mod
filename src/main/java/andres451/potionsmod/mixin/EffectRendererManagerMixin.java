package andres451.potionsmod.mixin;

import andres451.potionsmod.NightVisionClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.effects.api.effect.EffectContainer;
import sunsetsatellite.catalyst.effects.api.effect.render.EffectRendererManager;

@Environment(EnvType.CLIENT)
@Mixin(value = EffectRendererManager.class, remap = false)
public abstract class EffectRendererManagerMixin {

	@Inject(method = "drawScreenEffects", at = @At("HEAD"), require = 0)
	private void nightVisionUpdate(EffectContainer<?> container, Minecraft mc,
											 int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		NightVisionClient.update(mc);
	}
}
