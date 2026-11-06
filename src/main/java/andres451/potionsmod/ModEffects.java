package andres451.potionsmod;

import static andres451.potionsmod.PotionsMod.MOD_ID;
import static andres451.potionsmod.PotionEffects.*;

import java.util.Collections;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.catalyst.effects.api.attribute.Attributes;
import sunsetsatellite.catalyst.effects.api.effect.Effect;
import sunsetsatellite.catalyst.effects.api.effect.EffectContainer;
import sunsetsatellite.catalyst.effects.api.effect.EffectStack;
import sunsetsatellite.catalyst.effects.api.effect.EffectTimeType;
import sunsetsatellite.catalyst.effects.api.effect.Effects;
import sunsetsatellite.catalyst.effects.api.effect.IHasEffects;
import sunsetsatellite.catalyst.effects.api.modifier.ModifierType;
import sunsetsatellite.catalyst.effects.api.modifier.type.IntModifier;

public class ModEffects {

	private static Effect simple(String name, int ticks) {
		return new Effect("effect." + MOD_ID + "." + name, MOD_ID + ":" + name,
			Collections.emptyList(), EffectTimeType.RESET, 1).setDefaultDuration(ticks);
	}

	public static final Effect DAMAGE_BOOST = simple("damage_boost", DAMAGE_BOOST_DURATION);

	public static final Effect REGEN = new Effect(
		"effect." + MOD_ID + ".regen", MOD_ID + ":regen",
		Collections.emptyList(), EffectTimeType.RESET, 1
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player && stack.getTickCount() % 20 == 0) {
				((Player) container.getParent()).heal(2);
			}
		}
	}.setDefaultDuration(REGEN_DURATION);

	public static final Effect SPEED = new Effect(
		"effect." + MOD_ID + ".speed", MOD_ID + ":speed",
		Collections.emptyList(), EffectTimeType.RESET, 1
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player) {
				Player p = (Player) container.getParent();
				p.speed *= 3.0F;
				p.flySpeed *= 3.0F;
			}
		}
	}.setDefaultDuration(SPEED_DURATION);

	public static final Effect DIG_SPEED = simple("dig_speed", DIG_SPEED_DURATION);

	public static final Effect RESISTANCE = simple("resistance", RESISTANCE_DURATION);

	public static final Effect SMALL = simple("small", SMALL_DURATION);

	public static final Effect NIGHT_VISION = new Effect(
		"effect." + MOD_ID + ".night_vision", MOD_ID + ":night_vision",
		Collections.emptyList(), EffectTimeType.RESET, 1
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player) {
				Runnable r = nightVisionApply;
				if (r != null) r.run();
			}
		}

		@Override
		public <T> void expired(EffectStack stack, EffectContainer<T> container) {
			Runnable r = nightVisionRestore;
			if (r != null) r.run();
		}

		@Override
		public <T> void removed(EffectStack stack, EffectContainer<T> container) {
			Runnable r = nightVisionRestore;
			if (r != null) r.run();
		}
	}.setDefaultDuration(NIGHT_VISION_DURATION);

	public static volatile Runnable nightVisionApply = null;
	public static volatile Runnable nightVisionRestore = null;

	public static final Effect INVISIBLE = simple("invisible", INVISIBLE_DURATION);

	public static final Effect FLIGHT = new Effect(
		"effect." + MOD_ID + ".flight", MOD_ID + ":flight",
		Collections.emptyList(), EffectTimeType.RESET, 1
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (!(container.getParent() instanceof Player)) return;
			Player p = (Player) container.getParent();
			p.fallDistance = 0.0F;
			if (p.vehicle != null || p.isPlayerSleeping()) return;
			if (p.isJumping()) {
				p.yd = 0.3;        // UP
			} else if (p.isSneaking()) {
				p.yd = -0.3;       // DOWN
			} else {
				p.yd = 0.0;        // IDLE
			}
			p.flySpeed = 0.035F;   // HORIZONTAL SPEED
		}
	}.setDefaultDuration(FLIGHT_DURATION);

	public static final Effect MORTALITY = simple("mortality", MORTALITY_DURATION);

	public static final Effect[] ALL = { REGEN, SPEED, DAMAGE_BOOST, DIG_SPEED, RESISTANCE, SMALL, FLIGHT, NIGHT_VISION, INVISIBLE };

	public static void init() {
		for (Effect e : ALL) {
			Effects.getInstance().register(e.id, e);
		}
	}

	public static void give(Player player, Effect effect, int ticks, int amount) {
		IHasEffects<?> target = (IHasEffects<?>) player;
		EffectStack stack = new EffectStack(target, effect, ticks, amount);
		target.getContainer().add(stack);
		stack.start(target.getContainer());
	}

	public static void clear(Player player) {
		EffectContainer<?> container = ((IHasEffects<?>) player).getContainer();
		for (Effect e : ALL) {
			container.remove(e);
		}
	}

	public static boolean has(Player player, Effect effect) {
		EffectContainer<?> c = ((IHasEffects<?>) player).getContainer();
		return c != null && c.hasEffect(effect);
	}

	public static int scale(int value, double factor) {
		double d = value * factor;
		int floor = (int) Math.floor(d);
		return Math.random() < d - floor ? floor + 1 : floor;
	}
}
