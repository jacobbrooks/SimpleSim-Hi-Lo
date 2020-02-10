package core;

public class SimSettings {

	// engine settings
	private final int roundsPerTrial;
	private final int numTrials;

	// player settings
	private final double startingBankroll;
	private final boolean useDeviations;
	private final int[] betRamp; // index 0 corresponds to True Count of 0 or less
	private final int[] spotsRamp; // index 0 corresponds to True Count of 0 or less
	private final boolean wong;
	private final boolean backcount;
	private final double wongIn;
	private final double wongOut;

	// game conditions
	private final double minBet;
	private final int decks;
	private final double penetration; // in terms of decks cut off
	private final int roundsPerHour; // rounds played

	// game rules, assumes BJ pays 3:2
	private final boolean h17;
	private final boolean das;
	private final boolean rsa;
	private final boolean ls;
	private final int splitLimit;

	private SimSettings(int roundsPerTrial, int numTrials, double startingBankroll, boolean useDeviations,
			int[] betRamp, int[] spotsRamp, boolean wong, boolean backcount, double wongIn, double wongOut,
			double minBet, int decks, double penetration, int roundsPerHour, boolean h17, boolean das, boolean rsa, boolean ls,
			int splitLimit) {
		this.roundsPerTrial = roundsPerTrial;
		this.numTrials = numTrials;
		this.startingBankroll = startingBankroll;
		this.useDeviations = useDeviations;
		this.betRamp = betRamp;
		this.spotsRamp = spotsRamp;
		this.wong = wong;
		this.backcount = backcount;
		this.wongIn = wongIn;
		this.wongOut = wongOut;
		this.minBet = minBet;
		this.decks = decks;
		this.penetration = penetration;
		this.roundsPerHour = roundsPerHour;
		this.h17 = h17;
		this.das = das;
		this.rsa = rsa;
		this.ls = ls;
		this.splitLimit = splitLimit;
	}

	public static class SettingsBuilder {
		private int roundsPerTrial;
		private int numTrials;
		private double startingBankroll;
		private boolean useDeviations;
		private int[] betRamp;
		private int[] spotsRamp;
		private boolean wong;
		private boolean backcount;
		private double wongIn;
		private double wongOut;
		private double minBet;
		private int decks;
		private double penetration;
		private int roundsPerHour;
		private boolean h17;
		private boolean das;
		private boolean rsa;
		private boolean ls;
		private int splitLimit;

		public SettingsBuilder setRoundsPerTrial(int roundsPerTrial) {
			this.roundsPerTrial = roundsPerTrial;
			return this;
		}

		public SettingsBuilder setNumTrials(int numTrials) {
			this.numTrials = numTrials;
			return this;
		}

		public SettingsBuilder setStartingBankroll(double startingBankroll) {
			this.startingBankroll = startingBankroll;
			return this;
		}

		public SettingsBuilder setUseDeviations(boolean useDeviations) {
			this.useDeviations = useDeviations;
			return this;
		}

		public SettingsBuilder setBetRamp(int[] betRamp) {
			this.betRamp = betRamp;
			return this;
		}

		public SettingsBuilder setSpotsRamp(int[] spotsRamp) {
			this.spotsRamp = spotsRamp;
			return this;
		}

		public SettingsBuilder setWong(boolean wong) {
			this.wong = wong;
			return this;
		}

		public SettingsBuilder setBackcount(boolean backcount) {
			this.backcount = backcount;
			return this;
		}

		public SettingsBuilder setWongIn(double wongIn) {
			this.wongIn = wongIn;
			return this;
		}

		public SettingsBuilder setWongOut(double wongOut) {
			this.wongOut = wongOut;
			return this;
		}

		public SettingsBuilder setMinBet(double minBet) {
			this.minBet = minBet;
			return this;
		}

		public SettingsBuilder setDecks(int decks) {
			this.decks = decks;
			return this;
		}

		public SettingsBuilder setPenetration(double penetration) {
			this.penetration = penetration;
			return this;
		}
		
		public SettingsBuilder setRoundsPerHour(int roundsPerHour) {
			this.roundsPerHour = roundsPerHour;
			return this;
		}

		public SettingsBuilder setH17(boolean h17) {
			this.h17 = h17;
			return this;
		}

		public SettingsBuilder setDAS(boolean das) {
			this.das = das;
			return this;
		}

		public SettingsBuilder setRSA(boolean rsa) {
			this.rsa = rsa;
			return this;
		}

		public SettingsBuilder setLS(boolean ls) {
			this.ls = ls;
			return this;
		}

		public SettingsBuilder setSplitLimit(int splitLimit) {
			this.splitLimit = splitLimit;
			return this;
		}

		public SimSettings build() {
			SimSettings settings = new SimSettings(roundsPerTrial, numTrials, startingBankroll, useDeviations, betRamp,
					spotsRamp, wong, backcount, wongIn, wongOut, minBet, decks, penetration, roundsPerHour, h17, das, rsa, ls,
					splitLimit
			);
			return settings;
		}

		public int getRoundsPerTrial() {
			return roundsPerTrial;
		}

		public int getNumTrials() {
			return numTrials;
		}

		public double getStartingBankroll() {
			return startingBankroll;
		}

		public boolean isUseDeviations() {
			return useDeviations;
		}

		public int[] getBetRamp() {
			return betRamp;
		}

		public int[] getSpotsRamp() {
			return spotsRamp;
		}

		public boolean isWonging() {
			return wong;
		}

		public boolean isBackcounting() {
			return backcount;
		}

		public double getWongIn() {
			return wongIn;
		}

		public double getWongOut() {
			return wongOut;
		}

		public double getMinBet() {
			return minBet;
		}

		public int getDecks() {
			return decks;
		}

		public double getPenetration() {
			return penetration;
		}
		
		public int getRoundsPerHour() {
			return roundsPerHour;
		}

		public boolean isH17() {
			return h17;
		}

		public boolean isDAS() {
			return das;
		}

		public boolean isRSA() {
			return rsa;
		}

		public boolean isLS() {
			return ls;
		}

		public int getSplitLimit() {
			return splitLimit;
		}

	}

	public int getRoundsPerTrial() {
		return roundsPerTrial;
	}

	public int getNumTrials() {
		return numTrials;
	}

	public double getStartingBankroll() {
		return startingBankroll;
	}

	public boolean isUseDeviations() {
		return useDeviations;
	}

	public int[] getBetRamp() {
		return betRamp;
	}

	public int[] getSpotsRamp() {
		return spotsRamp;
	}

	public boolean isWonging() {
		return wong;
	}

	public boolean isBackcounting() {
		return backcount;
	}

	public double getWongIn() {
		return wongIn;
	}

	public double getWongOut() {
		return wongOut;
	}

	public double getMinBet() {
		return minBet;
	}

	public int getDecks() {
		return decks;
	}

	public double getPenetration() {
		return penetration;
	}
	
	public double getRoundsPerHour() {
		return roundsPerHour;
	}
	
	public boolean isH17() {
		return h17;
	}

	public boolean isDAS() {
		return das;
	}

	public boolean isRSA() {
		return rsa;
	}

	public boolean isLS() {
		return ls;
	}

	public int getSplitLimit() {
		return splitLimit;
	}

}
