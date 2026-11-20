package andres451.potionsmod;

public final class Constants {
	private Constants() {}

	public static final int TICKS_PER_SECOND = 20;
	private static int seconds(int s) { return s * TICKS_PER_SECOND; }

	// ALCHEMY TABLE
	public static final int COOKING_DURATION = seconds(15);

	// DAMAGE
	public static final int    DAMAGE_BOOST_DURATION = seconds(15);
	public static final double DAMAGE_BOOST_MULTIPLIER = 2.0;

	// HEALING
	public static final int REGEN_DURATION = seconds(15);
	public static final int REGEN_INTERVAL = TICKS_PER_SECOND;
	public static final int REGEN_AMOUNT   = 2;

	// SPEED
	public static final int   SPEED_DURATION   = seconds(15);
	public static final float SPEED_MULTIPLIER = 3.0F;

	// TOOLS
	public static final int   DIG_SPEED_DURATION   = seconds(60);
	public static final float DIG_SPEED_MULTIPLIER = 2.5F;

	// DEFENSE
	public static final int    RESISTANCE_DURATION = seconds(60);
	public static final double INCOMING_DAMAGE_MULTIPLIER = 0.6;

	// VISION
	public static final int   NIGHT_VISION_DURATION = seconds(60);
	public static final float NIGHT_VISION_GAMMA    = 3.0F;

	// SIZE
	public static final int   SMALL_DURATION = seconds(15);
	public static final float SMALL_WIDTH           = 0.6F;
	public static final float SMALL_HEIGHT          = 0.8F;
	public static final float SMALL_HEIGHT_SNEAKING = 0.5F;

	// LAPSUS
	public static final int INVISIBLE_DURATION = seconds(15);

	// FLIGHT
	public static final int    FLIGHT_DURATION = seconds(15);
	public static final float  FLIGHT_SPEED    = 0.035F;
	public static final double FLIGHT_UP_SPEED   =  0.3;
	public static final double FLIGHT_DOWN_SPEED = -0.3;
	public static final double FLIGHT_IDLE_SPEED =  0.0;

	// INVENTORY
	public static final int INVENTORY_DURATION = seconds(3);

	// SOMNIA
	public static final int SOMNIA_DURATION    = seconds(3);

	// REACH
	public static final int   REACH_DURATION   = seconds(60);
	public static final float REACH_MULTIPLIER = 5.0F;

	// WARP
	public static final int    REPOSITION_DURATION     = seconds(2);
	public static final double REPOSITION_RANGE        = 200;

	// ASTRAL
	public static final int    ASTRAL_DURATION = seconds(20);
	public static final double ASTRAL_ACCEL = 0.08;
	public static final double ASTRAL_HORIZONTAL_FRICTION = 0.85;
	public static final double ASTRAL_VERTICAL_FRICTION   = 0.6;

	// RECALL
	public static final int POPPING_DURATION = seconds(10);

	// VEINS
	public static final int VINES_DURATION        = seconds(60);
	public static final int VINES_MAX_BLOCKS      = 64;

	// FARM
	public static final int FARM_DURATION            = seconds(60);
	public static final int FARM_MAX_BLOCKS          = 128;
	public static final int FARM_CHAIN_STEP_TICKS    = 3;
	public static final int FARM_BONEMEAL_INTERVAL   = 3;
	public static final int FARM_BONEMEAL_RADIUS     = 1;

	// MOD COMPAT
	public static final String STARDEW_BLOCKS = "luke.stardew.blocks.StardewBlocks";
	public static final String AETHER_BLOCKS  = "teamport.aether.block.AetherBlocks";
	public static final String AETHER_ITEMS   = "teamport.aether.item.AetherItems";
}
