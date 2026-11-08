package andres451.potionsmod.mixin;

import andres451.potionsmod.ISomniaSync;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.packet.PacketSleep;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.SERVER)
@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixinSomnia implements ISomniaSync {
	@Override
	public void potions$syncSleep() {
		PlayerServer self = (PlayerServer) (Object) this;
		TilePos b = self.bedTilePos;
		if (b == null) return;
		PacketSleep pk = new PacketSleep(self, 0, b.x, b.y, b.z);
		self.mcServer.getEntityTracker(self.dimension).sendPacketToTrackedPlayers(self, pk);
		self.playerNetServerHandler.teleportAndRotate(self.x, self.y, self.z, self.yRot, self.xRot);
		self.playerNetServerHandler.sendPacket(pk);
	}
}
