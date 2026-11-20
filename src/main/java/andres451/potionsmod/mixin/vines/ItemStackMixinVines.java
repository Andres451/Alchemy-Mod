package andres451.potionsmod.mixin.vines;

import andres451.potionsmod.effect.VinesHelper;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixinVines {
	@Inject(method = "beforeBlockDestroyed", at = @At("HEAD"), cancellable = true)
	private void potions$vines(World world, Player player, Block<?> block, TilePosc pos, Side side,
							   CallbackInfoReturnable<Boolean> cir) {
		if (VinesHelper.run(world, player, block, pos)) cir.setReturnValue(false);
	}
}
