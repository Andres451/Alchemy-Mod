package andres451.potionsmod;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import static andres451.potionsmod.Constants.TICKS_PER_SECOND;

public final class Config {
	private Config() {}

	public static TomlConfigHandler CFG;

	// ALCHEMY TABLE
	public static int COOKING_DURATION;

	// DAMAGE
	public static int DAMAGE_BOOST_DURATION;
	public static double DAMAGE_BOOST_MULTIPLIER;

	// HEALING
	public static int REGEN_DURATION, REGEN_AMOUNT;

	// SPEED
	public static int SPEED_DURATION;
	public static float SPEED_MULTIPLIER;

	// TOOLS
	public static int DIG_SPEED_DURATION;
	public static float DIG_SPEED_MULTIPLIER;

	// DEFENSE
	public static int RESISTANCE_DURATION;
	public static double INCOMING_DAMAGE_MULTIPLIER;

	// VISION
	public static int NIGHT_VISION_DURATION;
	public static float NIGHT_VISION_GAMMA;

	// SIZE
	public static int SMALL_DURATION;

	// LAPSUS
	public static int INVISIBLE_DURATION;

	// FLIGHT
	public static int FLIGHT_DURATION;
	public static float FLIGHT_SPEED;
	public static double FLIGHT_UP_SPEED, FLIGHT_DOWN_SPEED;

	// INVENTORY
	public static int INVENTORY_DURATION;

	// SOMNIA
	public static int SOMNIA_DURATION;

	// REACH
	public static int REACH_DURATION;
	public static float REACH_MULTIPLIER;

	// WARP
	public static int REPOSITION_DURATION;
	public static double REPOSITION_RANGE;

	// ASTRAL
	public static int ASTRAL_DURATION;
	public static double ASTRAL_ACCEL;

	// RECALL
	public static int POPPING_DURATION;

	// VEINS
	public static int VINES_DURATION, VINES_MAX_BLOCKS;

	// FARM
	public static int FARM_DURATION, FARM_MAX_BLOCKS, FARM_BONEMEAL_RADIUS;

	// VARIANTS
	public static double DILUTED_DURATION_FACTOR, DILUTED_STRENGTH_FACTOR;
	public static double DENSE_DURATION_FACTOR, DENSE_STRENGTH_FACTOR;


	public static void load() {
		Toml t = new Toml("Alchemy configuration - Durations are in SECONDS");

		t.addCategory("Alchemy table")
			.addEntry("cooking_duration", 30);

		t.addCategory("Damage")
			.addEntry("duration", 60)
			.addEntry("multiplier", 2.0);

		t.addCategory("Healing")
			.addEntry("duration", 15)
			.addEntry("amount", 2);

		t.addCategory("Speed")
			.addEntry("duration", 15)
			.addEntry("multiplier", 3.0);

		t.addCategory("Tools")
			.addEntry("duration", 60)
			.addEntry("multiplier", 2.5);

		t.addCategory("Defense")
			.addEntry("duration", 60)
			.addEntry("incoming_damage_multiplier", 0.6);

		t.addCategory("Vision")
			.addEntry("duration", 180)
			.addEntry("gamma", 3.0);

		t.addCategory("Size")
			.addEntry("duration", 120);

		t.addCategory("Lapsus")
			.addEntry("duration", 15);

		t.addCategory("Flight")
			.addEntry("duration", 60)
			.addEntry("speed", 0.035)
			.addEntry("up_speed", 0.3)
			.addEntry("down_speed", -0.3);

		t.addCategory("Inventory")
			.addEntry("duration", 3);

		t.addCategory("Somnia")
			.addEntry("duration", 3);

		t.addCategory("Reach")
			.addEntry("duration", 60)
			.addEntry("multiplier", 5.0);

		t.addCategory("Warp")
			.addEntry("duration", 2)
			.addEntry("range", 200.0);

		t.addCategory("Astral")
			.addEntry("duration", 30)
			.addEntry("accel", 0.08);

		t.addCategory("Recall")
			.addEntry("duration", 10);

		t.addCategory("Veins")
			.addEntry("duration", 60)
			.addEntry("max_blocks", 64);

		t.addCategory("Farm")
			.addEntry("duration", 60)
			.addEntry("max_blocks", 128)
			.addEntry("bonemeal_radius", 1);

		t.addCategory("Variants")
			.addEntry("diluted_duration_factor", 2.0)
			.addEntry("diluted_strength_factor", 0.5)
			.addEntry("dense_duration_factor", 0.5)
			.addEntry("dense_strength_factor", 1.5);

		CFG = new TomlConfigHandler("alchemy", t);

		COOKING_DURATION = secs("Alchemy table.cooking_duration");

		DAMAGE_BOOST_DURATION = secs("Damage.duration");
		DAMAGE_BOOST_MULTIPLIER = CFG.getDouble("Damage.multiplier");

		REGEN_DURATION = secs("Healing.duration");
		REGEN_AMOUNT = CFG.getInt("Healing.amount");

		SPEED_DURATION = secs("Speed.duration");
		SPEED_MULTIPLIER = flt("Speed.multiplier");

		DIG_SPEED_DURATION = secs("Tools.duration");
		DIG_SPEED_MULTIPLIER = flt("Tools.multiplier");

		RESISTANCE_DURATION = secs("Defense.duration");
		INCOMING_DAMAGE_MULTIPLIER = CFG.getDouble("Defense.incoming_damage_multiplier");

		NIGHT_VISION_DURATION = secs("Vision.duration");
		NIGHT_VISION_GAMMA = flt("Vision.gamma");

		SMALL_DURATION = secs("Size.duration");

		INVISIBLE_DURATION = secs("Lapsus.duration");

		FLIGHT_DURATION = secs("Flight.duration");
		FLIGHT_SPEED = flt("Flight.speed");
		FLIGHT_UP_SPEED = CFG.getDouble("Flight.up_speed");
		FLIGHT_DOWN_SPEED = CFG.getDouble("Flight.down_speed");

		INVENTORY_DURATION = secs("Inventory.duration");
		SOMNIA_DURATION = secs("Somnia.duration");

		REACH_DURATION = secs("Reach.duration");
		REACH_MULTIPLIER = flt("Reach.multiplier");

		REPOSITION_DURATION = secs("Warp.duration");
		REPOSITION_RANGE = CFG.getDouble("Warp.range");

		ASTRAL_DURATION = secs("Astral.duration");
		ASTRAL_ACCEL = CFG.getDouble("Astral.accel");

		POPPING_DURATION = secs("Recall.duration");

		VINES_DURATION = secs("Veins.duration");
		VINES_MAX_BLOCKS = CFG.getInt("Veins.max_blocks");

		FARM_DURATION = secs("Farm.duration");
		FARM_MAX_BLOCKS = CFG.getInt("Farm.max_blocks");
		FARM_BONEMEAL_RADIUS = CFG.getInt("Farm.bonemeal_radius");

		DILUTED_DURATION_FACTOR = CFG.getDouble("Variants.diluted_duration_factor");
		DILUTED_STRENGTH_FACTOR = CFG.getDouble("Variants.diluted_strength_factor");
		DENSE_DURATION_FACTOR = CFG.getDouble("Variants.dense_duration_factor");
		DENSE_STRENGTH_FACTOR = CFG.getDouble("Variants.dense_strength_factor");
	}

	private static int secs(String path) {
		return CFG.getInt(path) * TICKS_PER_SECOND;
	}

	private static float flt(String path) {
		return (float) CFG.getDouble(path);
	}
}
