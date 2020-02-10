package core;

public class Count {

	private SimSettings settings;

	private double runningCount;
	private int shoePos;

	public Count(SimSettings settings) {
		this.settings = settings;
		runningCount = 0;
		shoePos = 0;
	}

	public void setShoePos(int shoePos) {
		this.shoePos = shoePos;
	}

	public void updateCount(Card c) {
		runningCount += c.getCountValue();
	}

	public double getTrueCount() {
		double decksRemaining = settings.getDecks() - (((double) shoePos) / 52);
		return runningCount / decksRemaining;
	}

	public double getRunningCount() {
		return runningCount;
	}

	public double getDecksLeft() {
		return settings.getDecks() - (((double) shoePos) / 52);
	}

}
