package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(value = LightmapHelper.class, remap = false)
public abstract class LightmapHelperMixin {

	@Shadow public Minecraft mc;

	@ModifyVariable(method = "applyGamma", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float potions$nightVision(float gamma) {
		if (mc.thePlayer != null && ModEffects.has(mc.thePlayer, ModEffects.NIGHT_VISION)) {
			return Math.max(gamma, 0.8F);
		}
		return gamma;
	}
}
