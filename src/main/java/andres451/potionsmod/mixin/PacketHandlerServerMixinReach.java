package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.SERVER)
@Mixin(value = PacketHandlerServer.class, remap = false)
public abstract class PacketHandlerServerMixinReach {

	@Shadow private PlayerServer playerEntity;

	@Redirect(method = {"handlePlayerAction", "handleUseOrPlaceItem"},
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/player/gamemode/Gamemode;getBlockReachDistance()F"))
	private float potions$blockReach(Gamemode gamemode) {
		float reach = gamemode.getBlockReachDistance();
		if (ModEffects.has(playerEntity, ModEffects.REACH)) return reach * 2.0F;
		return reach;
	}

	@Redirect(method = "handleEntityInteract",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/player/gamemode/Gamemode;getEntityReachDistance()F"))
	private float potions$entityReach(Gamemode gamemode) {
		float reach = gamemode.getEntityReachDistance();
		if (ModEffects.has(playerEntity, ModEffects.REACH)) return reach * 2.0F;
		return reach;
	}
}
