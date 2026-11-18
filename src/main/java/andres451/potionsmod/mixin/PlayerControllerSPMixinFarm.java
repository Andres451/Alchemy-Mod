package andres451.potionsmod.mixin;

import andres451.potionsmod.FarmHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerControllerSP;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerControllerSP.class, remap = false)
public abstract class PlayerControllerSPMixinFarm {
	@Inject(method = "destroyBlock", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/core/entity/player/Player;getCurrentEquippedItem()Lnet/minecraft/core/item/ItemStack;",
		shift = At.Shift.AFTER))
	private void potions$farm(TilePosc tilePos, Side side, CallbackInfoReturnable<Boolean> cir,
							  @Local Block<?> block) {
		Minecraft mc = Minecraft.getMinecraft();
		FarmHelper.onBreak(mc.currentWorld, mc.thePlayer, block, tilePos);
	}
}
