package andres451.potionsmod.mixin.catalyst;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sunsetsatellite.catalyst.effects.Options;
import sunsetsatellite.catalyst.effects.api.effect.options.EffectDisplayPlace;

@Environment(EnvType.CLIENT)
@Mixin(value = Options.class, remap = false)
public abstract class CatalystOptionsMixin {

	@Redirect(method = "<clinit>",
		at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
			target = "Lsunsetsatellite/catalyst/effects/api/effect/options/EffectDisplayPlace;INVENTORY:Lsunsetsatellite/catalyst/effects/api/effect/options/EffectDisplayPlace;"))
	private static EffectDisplayPlace potions$defaultBoth() {
		return EffectDisplayPlace.BOTH;
	}
}
