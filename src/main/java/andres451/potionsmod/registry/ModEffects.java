package andres451.potionsmod.registry;

import static andres451.potionsmod.Constants.*;
import static andres451.potionsmod.Config.*;
import static andres451.potionsmod.PotionsMod.MOD_ID;

import java.util.Collections;

import andres451.potionsmod.PotionVariant;
import andres451.potionsmod.effect.FarmHelper;
import andres451.potionsmod.d_interface.ISomnia;
import andres451.potionsmod.d_interface.ISomniaSync;
import andres451.potionsmod.effect.PotionEffects;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.catalyst.effects.api.effect.Effect;
import sunsetsatellite.catalyst.effects.api.effect.EffectContainer;
import sunsetsatellite.catalyst.effects.api.effect.EffectStack;
import sunsetsatellite.catalyst.effects.api.effect.EffectTimeType;
import sunsetsatellite.catalyst.effects.api.effect.Effects;
import sunsetsatellite.catalyst.effects.api.effect.IHasEffects;

public class ModEffects {

	private static final int MAX_STACK = PotionVariant.values().length; // 3

	private static Effect simple(String name, int ticks) {
		return new Effect("effect." + MOD_ID + "." + name, MOD_ID + ":" + name,
			Collections.emptyList(), EffectTimeType.RESET, MAX_STACK).setDefaultDuration(ticks);
	}

	public static final Effect DAMAGE_BOOST = simple("damage_boost", DAMAGE_BOOST_DURATION);

	public static final Effect REGEN = new Effect(
		"effect." + MOD_ID + ".regen", MOD_ID + ":regen",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p && stack.getTickCount() % REGEN_INTERVAL == 0) {
				p.heal(scale(REGEN_AMOUNT, strength(p, REGEN)));
			}
		}
	}.setDefaultDuration(REGEN_DURATION);

	public static final Effect SPEED = new Effect(
		"effect." + MOD_ID + ".speed", MOD_ID + ":speed",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p) {
				float f = (float) bonus(p, SPEED, SPEED_MULTIPLIER);
				p.speed *= f;
				p.flySpeed *= f;
			}
		}
	}.setDefaultDuration(SPEED_DURATION);

	public static final Effect DIG_SPEED = simple("dig_speed", DIG_SPEED_DURATION);
	public static final Effect RESISTANCE = simple("resistance", RESISTANCE_DURATION);
	public static final Effect SMALL = simple("small", SMALL_DURATION);
	public static final Effect NIGHT_VISION = simple("night_vision", NIGHT_VISION_DURATION);
	public static final Effect INVISIBLE = simple("invisible", INVISIBLE_DURATION);

	public static final Effect FLIGHT = new Effect(
		"effect." + MOD_ID + ".flight", MOD_ID + ":flight",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (!(container.getParent() instanceof Player p)) return;
			p.fallDistance = 0.0F;
			if (p.vehicle != null || p.isPlayerSleeping()) return;
			double s = strength(p, FLIGHT);
			if (p.isJumping()) {
				p.yd = FLIGHT_UP_SPEED * s;
			} else if (p.isSneaking()) {
				p.yd = FLIGHT_DOWN_SPEED * s;
			} else {
				p.yd = FLIGHT_IDLE_SPEED;
			}
			p.flySpeed = (float) (FLIGHT_SPEED * s);
		}
	}.setDefaultDuration(FLIGHT_DURATION);

	public static final Effect INVENTORY = simple("inventory", INVENTORY_DURATION);

	public static final Effect SOMNIA = new Effect(
		"effect." + MOD_ID + ".somnia", MOD_ID + ":somnia",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void expired(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p) {
				if (!p.world.isClientSide) ((ISomnia) p).potions$somnia();
				if (p.isPlayerSleeping() && p instanceof ISomniaSync s) s.potions$syncSleep();
			}
		}
	}.setDefaultDuration(SOMNIA_DURATION);

	public static final Effect REACH = simple("reach", REACH_DURATION);

	public static final Effect REPOSITION = new Effect(
		"effect." + MOD_ID + ".reposition", MOD_ID + ":reposition",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void expired(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p && !p.world.isClientSide) {
				PotionEffects.finishReposition(p);
			}
		}
	}.setDefaultDuration(REPOSITION_DURATION);

	public static final Effect ASTRAL = simple("astral", ASTRAL_DURATION);

	public static final Effect POPPING = new Effect(
		"effect." + MOD_ID + ".popping", MOD_ID + ":popping",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void expired(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p && !p.world.isClientSide) PotionEffects.finishPopping(p);
		}

		@Override
		public <T> void removed(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p && !p.world.isClientSide) PotionEffects.finishPopping(p);
		}
	}.setDefaultDuration(POPPING_DURATION);

	public static final Effect VINES = simple("vines", VINES_DURATION);

	public static final Effect FARM = new Effect(
		"effect." + MOD_ID + ".farm", MOD_ID + ":farm",
		Collections.emptyList(), EffectTimeType.RESET, 3
	) {
		@Override
		public <T> void tick(EffectStack stack, EffectContainer<T> container) {
			if (container.getParent() instanceof Player p) FarmHelper.tick(p, stack.getTickCount());
		}
	}.setDefaultDuration(FARM_DURATION);

	public static final Effect[] ALL = {
		REGEN, SPEED, DAMAGE_BOOST, DIG_SPEED,
		RESISTANCE, SMALL, FLIGHT, NIGHT_VISION,
		INVISIBLE, INVENTORY, SOMNIA, REACH,
		REPOSITION, ASTRAL, POPPING, VINES, FARM };

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

	public static void give(Player player, Effect effect, int ticks, int amount, PotionVariant v) {
		((IHasEffects<?>) player).getContainer().remove(effect);
		int scaledTicks = Math.max(1, (int) Math.round(ticks * v.duration()));
		give(player, effect, scaledTicks, v.stackAmount());
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

	public static PotionVariant variantOf(Player player, Effect effect) {
		if (player == null) return PotionVariant.NORMAL;
		EffectContainer<?> c = ((IHasEffects<?>) player).getContainer();
		if (c == null) return PotionVariant.NORMAL;
		for (EffectStack s : c.getEffects()) {
			if (s.getEffect() == effect) return PotionVariant.fromStackAmount(s.getAmount());
		}
		return PotionVariant.NORMAL;
	}

	public static double strength(Player player, Effect effect) {
		return variantOf(player, effect).strength();
	}

	public static double bonus(Player player, Effect effect, double base) {
		return Math.max(0.0, 1.0 + (base - 1.0) * strength(player, effect));
	}

	public static int scaleCount(int value, double factor) {
		return Math.max(1, (int) Math.round(value * factor));
	}

	public static int scale(int value, double factor) {
		double d = value * factor;
		int floor = (int) Math.floor(d);
		return Math.random() < d - floor ? floor + 1 : floor;
	}
}
