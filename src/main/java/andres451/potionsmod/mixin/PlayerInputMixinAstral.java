package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.PlayerInput;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerInput.class, remap = false)
public abstract class PlayerInputMixinAstral {
	@Inject(method = "tick", at = @At("TAIL"))
	private void potions$freeze(Player p, CallbackInfo ci) {
		if (ModEffects.has(p, ModEffects.ASTRAL)) {
			PlayerInput in = (PlayerInput) (Object) this;
			in.moveForward = 0.0F;
			in.moveStrafe = 0.0F;
			in.jump = false;
			in.sneak = false;
		}
	}
}
