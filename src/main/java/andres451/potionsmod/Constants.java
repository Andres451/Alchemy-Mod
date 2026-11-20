package andres451.potionsmod;

public final class Constants {
	private Constants() {}

	public static final int TICKS_PER_SECOND = 20;

	// HEALING
	public static final int REGEN_INTERVAL = TICKS_PER_SECOND;

	// SIZE
	public static final float SMALL_WIDTH           = 0.6F;
	public static final float SMALL_HEIGHT          = 0.8F;
	public static final float SMALL_HEIGHT_SNEAKING = 0.5F;

	// FLIGHT
	public static final double FLIGHT_IDLE_SPEED = 0.0;

	// ASTRAL
	public static final double ASTRAL_HORIZONTAL_FRICTION = 0.85;
	public static final double ASTRAL_VERTICAL_FRICTION   = 0.6;

	// FARM
	public static final int FARM_CHAIN_STEP_TICKS  = 3;
	public static final int FARM_BONEMEAL_INTERVAL = 3;

	// MOD COMPAT
	public static final String STARDEW_BLOCKS = "luke.stardew.blocks.StardewBlocks";
	public static final String AETHER_BLOCKS  = "teamport.aether.block.AetherBlocks";
	public static final String AETHER_ITEMS   = "teamport.aether.item.AetherItems";
}
