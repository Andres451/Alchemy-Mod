package andres451.potionsmod.mixin.vision;

import andres451.potionsmod.registry.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static andres451.potionsmod.Constants.NIGHT_VISION_GAMMA;

@Environment(EnvType.CLIENT)
@Mixin(value = LightmapHelper.class, remap = false)
public abstract class LightmapHelperMixin {

	@Shadow public Minecraft mc;

	@ModifyVariable(method = "applyGamma", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float potions$nightVision(float gamma) {
		if (mc.thePlayer != null && ModEffects.has(mc.thePlayer, ModEffects.NIGHT_VISION)) {
			return Math.max(gamma, NIGHT_VISION_GAMMA);
		}
		return gamma;
	}
}
