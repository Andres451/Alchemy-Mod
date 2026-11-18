package andres451.potionsmod.mixin;

import andres451.potionsmod.AetherCompat;
import andres451.potionsmod.ModEffects;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "teamport.aether.entity.monster.MobMonsterAether", remap = false)
public class MobMonsterAetherMixinInvisible {
	@Inject(method = "tick", at = @At("HEAD"))
	private void potions$dropInvisible(CallbackInfo ci) {
		if (AetherCompat.isBoss(this)) return;
		MobPathfinderAccessor mob = (MobPathfinderAccessor) (Object) this;
		if (mob.potions$getTarget() instanceof Player p
			&& ModEffects.has(p, ModEffects.INVISIBLE)) {
			mob.potions$setTarget(null);
		}
	}
}
