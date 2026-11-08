package andres451.potionsmod.mixin;

import andres451.potionsmod.ISomnia;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixinSomnia implements ISomnia {

	@Unique
	private boolean potions$potionSleep;

	@Invoker("setPlayerSleeping")
	abstract void potions$invokeSleep(int x, int y, int z);

	@Override
	public void potions$somnia() {
		Player self = (Player) (Object) this;
		if (self.isPlayerSleeping() || !self.isAlive()) return;
		potions$potionSleep = true;
		potions$invokeSleep(MathHelper.floor(self.x), MathHelper.floor(self.bb.minY), MathHelper.floor(self.z));
	}

	@Inject(method = "isInBed", at = @At("HEAD"), cancellable = true)
	private void potions$inBed(CallbackInfoReturnable<Boolean> cir) {
		if (potions$potionSleep) cir.setReturnValue(true);
	}

	@Redirect(method = "tick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;isDaytime()Z"))
	private boolean potions$day(World world) {
		return !potions$potionSleep && world.isDaytime();
	}

	@Inject(method = "wakeUpPlayer", at = @At("HEAD"))
	private void potions$wake(boolean a, boolean b, CallbackInfo ci) {
		potions$potionSleep = false;
	}
}
