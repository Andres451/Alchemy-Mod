package andres451.potionsmod.mixin;

import andres451.potionsmod.IDimensionHop;
import andres451.potionsmod.PoppingClient;
import andres451.potionsmod.PotionEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.player.PlayerLocal;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerLocal.class, remap = false)
public abstract class PlayerLocalMixinHop implements IDimensionHop {
	@Override
	public void potions$hop(PotionEffects.Hop h) {
		PoppingClient.hop(h);
	}
}
