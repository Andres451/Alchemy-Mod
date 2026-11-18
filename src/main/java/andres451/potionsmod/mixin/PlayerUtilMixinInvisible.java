package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "teamport.aether.entity.player.PlayerUtil", remap = false)
public class PlayerUtilMixinInvisible {
	@Inject(method = "isInvisible(Lnet/minecraft/core/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private static void potions$invisible(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && entity instanceof Player p && ModEffects.has(p, ModEffects.INVISIBLE)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isInvisible(Lnet/minecraft/core/entity/Entity;D)Z", at = @At("RETURN"), cancellable = true)
	private static void potions$invisibleClose(Entity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && entity instanceof Player p && ModEffects.has(p, ModEffects.INVISIBLE)) {
			cir.setReturnValue(true);
		}
	}
}
