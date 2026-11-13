package andres451.potionsmod.mixin;

import andres451.potionsmod.PoppingClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.PortalHandler;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixinPopping {
	@Redirect(method = "usePortal",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/world/PortalHandler;teleportEntity(Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/util/helper/DyeColor;Lnet/minecraft/core/world/Dimension;Lnet/minecraft/core/world/Dimension;)V"))
	private void potions$hop(PortalHandler handler, World w, Entity e, DyeColor c, Dimension oldDim, Dimension newDim) {
		var h = PoppingClient.pending;
		if (h == null) handler.teleportEntity(w, e, c, oldDim, newDim); // portal normal
		else PoppingClient.place(w, e, h);
	}
}
