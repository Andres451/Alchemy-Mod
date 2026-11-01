package andres451.potionsmod.mixin;

import andres451.potionsmod.PotionsMod;
import andres451.potionsmod.TileEntityAlchemyTable;
import andres451.potionsmod.gui.PotionsScreens;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.net.packet.PacketContainerOpen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PacketHandlerClient.class)
public abstract class PacketHandlerClientMixinPotions {
	@Final @Shadow private Minecraft mc;

	@Inject(method = "handleContainerOpen(Lnet/minecraft/core/net/packet/PacketContainerOpen;)V", at = @At("TAIL"))
	private void handlePotionsMachines(PacketContainerOpen packet, CallbackInfo ci) {
		PlayerLocal player = this.mc.thePlayer;
		if (packet.inventoryType == PotionsMod.ALCHEMY_TABLE_SCREEN_ID) {
			((PotionsScreens) player).potions$displayAlchemyTableScreen(new TileEntityAlchemyTable());
			player.containerMenu.containerId = packet.windowId;
		}
	}
}
