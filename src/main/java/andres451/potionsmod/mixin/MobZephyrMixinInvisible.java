package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "teamport.aether.entity.monster.zephyr.MobZephyr", remap = false)
public class MobZephyrMixinInvisible {
	@Shadow private Entity targetedEntity;

	@Inject(method = "updateAI", at = @At("HEAD"))
	private void potions$dropInvisible(CallbackInfo ci) {
		if (targetedEntity instanceof Player p && ModEffects.has(p, ModEffects.INVISIBLE)) {
			targetedEntity = null;
		}
	}
}
