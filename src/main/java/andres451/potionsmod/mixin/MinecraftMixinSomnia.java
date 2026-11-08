package andres451.potionsmod.mixin;

import andres451.potionsmod.ISomnia;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixinSomnia {

	@Unique private int potions$carryDim = -1;

	@Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
	private void potions$respawnHead(boolean multiplayer, int targetDimension, CallbackInfo ci) {
		Minecraft self = (Minecraft) (Object) this;
		potions$carryDim = -1;
		if (multiplayer || self.thePlayer == null || self.currentWorld == null) return;
		int dim = ((ISomnia) self.thePlayer).potions$getBedlessDim();
		if (dim < 0) return;
		if (targetDimension != dim || self.currentWorld.dimension.id != dim) {
			if (self.currentWorld.dimension.id != dim) self.usePortal(dim, null);
			self.respawn(false, dim);
			ci.cancel();
			return;
		}
		potions$carryDim = dim;
	}

	@Redirect(method = "respawn",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/entity/player/Player;getValidBedSpawnCoordinates(Lnet/minecraft/core/world/World;Lnet/minecraft/core/world/pos/TilePos;)Lnet/minecraft/core/world/pos/TilePos;"))
	private TilePos potions$validSpawn(World world, TilePos coords) {
		TilePos bed = Player.getValidBedSpawnCoordinates(world, coords); // carga los chunks
		Minecraft self = (Minecraft) (Object) this;
		if (self.thePlayer != null && ((ISomnia) self.thePlayer).potions$getBedlessDim() >= 0) return coords;
		return bed;
	}

	@Inject(method = "respawn", at = @At("TAIL"))
	private void potions$respawnTail(boolean multiplayer, int targetDimension, CallbackInfo ci) {
		Minecraft self = (Minecraft) (Object) this;
		if (self.thePlayer != null) ((ISomnia) self.thePlayer).potions$setBedlessDim(potions$carryDim);
	}
}
