package andres451.potionsmod.mixin.catalyst;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.CatalystClient;
import sunsetsatellite.catalyst.CatalystEffectsClient;
import sunsetsatellite.catalyst.effects.Options;

@Environment(EnvType.CLIENT)
@Mixin(value = CatalystEffectsClient.class, remap = false)
public abstract class CatalystEffectsClientMixin {

	@Inject(method = "addSettingsPage", at = @At("HEAD"))
	private static void potions$addDisplayPlace(CallbackInfo ci) {
		CatalystClient.effectsCategory
			.withComponent(new ToggleableOptionComponent<>(Options.effectDisplayPlaceEnumOption));
	}
}
