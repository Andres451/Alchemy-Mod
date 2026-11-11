package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = EntityRendererDispatcher.class, remap = false)
public abstract class EntityRendererDispatcherMixin {

	@Inject(method = "renderEntityWithPosYaw", at = @At("HEAD"), cancellable = true)
	private void hideInvisible(TessellatorGeneral tessellator, Entity entity,
										 double x, double y, double z, float yaw, float partialTick, CallbackInfo ci) {
		if (entity instanceof Player && ModEffects.has((Player) entity, ModEffects.INVISIBLE)) {
			ci.cancel();
		}
	}
}
