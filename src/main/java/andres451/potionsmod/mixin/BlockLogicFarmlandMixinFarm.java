package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.minecraft.core.block.BlockLogicFarmland;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLogicFarmland.class)
public class BlockLogicFarmlandMixinFarm {
	@Inject(method = "onEntityWalkedOn", at = @At("HEAD"), cancellable = true)
	private void potions$noTrample(World world, TilePosc tilePos, Entity walker, CallbackInfo ci) {
		if (walker instanceof Player p && ModEffects.has(p, ModEffects.FARM)) ci.cancel();
	}
}
