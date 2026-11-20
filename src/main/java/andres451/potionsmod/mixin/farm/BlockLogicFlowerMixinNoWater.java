package andres451.potionsmod.mixin.farm;

import andres451.potionsmod.registry.ModEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockLogicFlower.class)
public class BlockLogicFlowerMixinNoWater {
	@WrapOperation(method = "onBonemealUsed", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/core/world/World;setBlockTypeNotify(Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/block/Block;)Z"))
	private boolean potions$noWaterSpread(World world, TilePosc pos, Block<?> block, Operation<Boolean> original,
										  @Local(argsOnly = true) Player player) {
		if (player != null && ModEffects.has(player, ModEffects.FARM)
			&& world.getBlockType(pos).hasTag(BlockTags.IS_WATER)) return false;
		return original.call(world, pos, block);
	}
}
