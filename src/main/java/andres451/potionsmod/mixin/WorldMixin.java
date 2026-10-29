package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {
	@Inject(method = "getClosestPlayerToEntity", at = @At("RETURN"), cancellable = true, require = 0)
	private void ignoreInvisible(Entity entity, double radius, CallbackInfoReturnable<Player> cir) {
		Player found = cir.getReturnValue();
		if (radius >= 0 && found != null && ModEffects.has(found, ModEffects.INVISIBLE)) {
			cir.setReturnValue(null);
		}
	}
}
