package andres451.potionsmod;

public enum PotionVariant {
	NORMAL(1), DILUTED(2), DENSE(3);

	private final int stackAmount;

	PotionVariant(int stackAmount) { this.stackAmount = stackAmount; }

	public int stackAmount() { return stackAmount; }

	public double duration() {
		return switch (this) {
			case DILUTED -> Config.DILUTED_DURATION_FACTOR;
			case DENSE -> Config.DENSE_DURATION_FACTOR;
			default -> 1.0;
		};
	}

	public double strength() {
		return switch (this) {
			case DILUTED -> Config.DILUTED_STRENGTH_FACTOR;
			case DENSE -> Config.DENSE_STRENGTH_FACTOR;
			default -> 1.0;
		};
	}

	public static PotionVariant fromStackAmount(int amount) {
		for (PotionVariant v : values()) if (v.stackAmount == amount) return v;
		return NORMAL;
	}
}
