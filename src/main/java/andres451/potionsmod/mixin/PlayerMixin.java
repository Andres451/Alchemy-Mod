package andres451.potionsmod.mixin;

import andres451.potionsmod.ModEffects;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.gamerule.GameRule;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin {

	@ModifyArg(
		method = "attackTargetEntityWithCurrentItem",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/entity/Entity;hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z"),
		index = 1)
	private int boostDamage(int damage) {
		Player self = (Player) (Object) this;
		return ModEffects.has(self, ModEffects.DAMAGE_BOOST) ? ModEffects.scale(damage, 2) : damage;
	}

	@ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private int reduceDamage(int damage) {
		if (damage > 0 && ModEffects.has((Player) (Object) this, ModEffects.RESISTANCE)) {
			return ModEffects.scale(damage, 0.6);
		}
		return damage;
	}

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void fireImmunity(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if (type == DamageType.FIRE && ModEffects.has((Player) (Object) this, ModEffects.RESISTANCE)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void blastImmunity(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if (type == DamageType.BLAST && ModEffects.has((Player) (Object) this, ModEffects.RESISTANCE)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "acidTick", at = @At("HEAD"), cancellable = true)
	private void acidImmunity(CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (ModEffects.has(self, ModEffects.RESISTANCE)) {
			self.acidMeltTicks = 0;
			self.acidExitDelayLeft = 0;
			self.acidPostExitPeak = 0;
			self.acidPostExitDrainTicksLeft = 0;
			ci.cancel();
		}
	}

	@Inject(method = "canBreatheUnderwater", at = @At("HEAD"), cancellable = true)
	private void waterBreathing(CallbackInfoReturnable<Boolean> cir) {
		if (ModEffects.has((Player) (Object) this, ModEffects.RESISTANCE)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "getCurrentPlayerStrVsBlock", at = @At("RETURN"), cancellable = true)
	private void digSpeed(Block<?> block, CallbackInfoReturnable<Float> cir) {
		if (ModEffects.has((Player) (Object) this, ModEffects.DIG_SPEED)) {
			cir.setReturnValue(cir.getReturnValue() * 2.5F);
		}
	}

	@Shadow protected boolean isDwarf;
	@Inject(method = "setupScale", at = @At("TAIL"))
	private void shrink(CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (self.isPlayerSleeping()) return;
		if (ModEffects.has(self, ModEffects.SMALL)) {
			this.isDwarf = true;
			self.bbWidth = 0.6F;
			self.bbHeight = self.isSneaking() ? 0.5F : 0.8F;
			self.setBounds();
		}
	}

	@Redirect(method = "onDeath",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/world/World;getGameRuleValue(Lnet/minecraft/core/data/gamerule/GameRule;)Ljava/lang/Object;"))
	private Object keepInventory(World world, GameRule<?> rule) {
		if (ModEffects.has((Player) (Object) this, ModEffects.MORTALITY)) return Boolean.TRUE;
		return world.getGameRuleValue(rule);
	}
}
