package andres451.potionsmod.mixin;

import andres451.potionsmod.ITeleportSync;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.SERVER)
@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixinTeleport implements ITeleportSync {
	@Override
	public void potions$syncTeleport() {
		PlayerServer s = (PlayerServer) (Object) this;
		s.playerNetServerHandler.teleportAndRotate(s.x, s.y, s.z, s.yRot, s.xRot);
	}
}
