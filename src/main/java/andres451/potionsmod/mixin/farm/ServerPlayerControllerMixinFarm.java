package andres451.potionsmod.mixin.farm;

import andres451.potionsmod.effect.FarmHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.server.world.ServerPlayerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerController.class, remap = false)
public class ServerPlayerControllerMixinFarm {
	@Inject(method = "mineBlock", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/core/block/Blocks;getBlock(I)Lnet/minecraft/core/block/Block;"))
	private void potions$farm(int x, int y, int z, Side side, CallbackInfoReturnable<Boolean> cir,
							  @Local(name = "id") int id) {
		ServerPlayerController c = (ServerPlayerController) (Object) this;
		FarmHelper.onBreak(c.player.world, c.player, Blocks.getBlock(id), new TilePos(x, y, z));
	}
}
