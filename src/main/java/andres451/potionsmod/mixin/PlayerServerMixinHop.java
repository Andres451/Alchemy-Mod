package andres451.potionsmod.mixin;

import andres451.potionsmod.IDimensionHop;
import andres451.potionsmod.PotionEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.PlayerList;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.SERVER)
@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixinHop implements IDimensionHop {
	@Override
	public void potions$hop(PotionEffects.Hop h) {
		PlayerServer s = (PlayerServer) (Object) this;
		PlayerList list = s.mcServer.playerList;
		list.sendPlayerToOtherDimension(s, h.dim(), null, false);
		World w = s.mcServer.getDimensionWorld(h.dim());
		double[] d = PotionEffects.destination(w, h);
		if (d == null) return;
		s.moveTo(d[0], d[1], d[2], s.yRot, s.xRot);
		s.fallDistance = 0.0F;
		s.playerNetServerHandler.teleportAndRotate(s.x, s.y, s.z, s.yRot, s.xRot);
		list.resendChunksAfterTeleport(s);
	}
}
