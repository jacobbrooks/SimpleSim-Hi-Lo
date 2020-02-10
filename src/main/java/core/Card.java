package core;

public class Card {

	private static final String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
	private final int ordinal;
	private int bjValue;
	private int countValue;

	public Card(int ordinal) {
		this.ordinal = ordinal;

		if (ordinal < 8) {
			bjValue = ordinal + 2;
		} else if (ordinal < 12) {
			bjValue = 10;
		} else {
			bjValue = 11;
		}

		if (ordinal < 5) {
			countValue = 1;
		} else if (ordinal > 7) {
			countValue = -1;
		} else {
			countValue = 0;
		}
	}

	public String getRank() {
		return RANKS[ordinal];
	}

	public int getOrdinal() {
		return ordinal;
	}

	public int getBJValue() {
		return bjValue;
	}

	public int getCountValue() {
		return countValue;
	}

	public void setAceEqualOne() {
		if (bjValue == 11)
			bjValue = 1;
	}

	public void setAceEqualEleven() {
		if (bjValue == 1)
			bjValue = 11;
	}

}
