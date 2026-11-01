package andres451.potionsmod.mixin;

import andres451.potionsmod.PotionsMod;
import andres451.potionsmod.TileEntityAlchemyTable;
import andres451.potionsmod.gui.MenuAlchemyTable;
import andres451.potionsmod.gui.PotionsScreens;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.packet.PacketContainerOpen;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.SERVER)
@Mixin(PlayerServer.class)
public abstract class PlayerServerMixinPotionsScreens implements PotionsScreens {
	@Shadow private int currentWindowId;
	@Shadow protected abstract void getNextWindowId();

	@Override
	public void potions$displayAlchemyTableScreen(TileEntityAlchemyTable tileEntity) {
		this.getNextWindowId();
		PlayerServer playerServer = (PlayerServer) (Object) this;
		playerServer.playerNetServerHandler.sendPacket(new PacketContainerOpen(
			this.currentWindowId, PotionsMod.ALCHEMY_TABLE_SCREEN_ID,
			tileEntity.getNameTranslationKey(), tileEntity.getContainerSize()));
		playerServer.containerMenu.onCraftGuiClosed(playerServer);
		playerServer.containerMenu = new MenuAlchemyTable(playerServer.inventory, tileEntity);
		playerServer.containerMenu.containerId = this.currentWindowId;
		playerServer.containerMenu.addSlotListener(playerServer);
	}
}
