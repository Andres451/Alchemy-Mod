package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.MobPathfinder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MobPathfinder.class, remap = false)
public abstract class MobPathfinderMixin {
	@Shadow protected Entity target;
	@Shadow protected Path pathToEntity;

	// MOB STOP FOLLOWING INVISIBLE
	@Inject(method = "updateAI", at = @At("HEAD"))
	private void dropInvisibleTarget(CallbackInfo ci) {
		if (target instanceof Player && ModEffects.has((Player) target, ModEffects.INVISIBLE)) {
			target = null;
			pathToEntity = null;
		}
	}
}
