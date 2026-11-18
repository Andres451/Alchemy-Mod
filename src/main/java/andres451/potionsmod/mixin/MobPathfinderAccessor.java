package andres451.potionsmod.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.MobPathfinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = MobPathfinder.class, remap = false)
public interface MobPathfinderAccessor {
	@Accessor("target") Entity potions$getTarget();
	@Accessor("target") void potions$setTarget(Entity e);
}
