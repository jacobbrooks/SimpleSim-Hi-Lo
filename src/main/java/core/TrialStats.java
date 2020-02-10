package core;

public class TrialStats {

	private SimSettings settings;

	private boolean bankrollRuined;
	private boolean bankrollRuined50p;
	private boolean bankrollRuined25p;

	private double bankroll;

	public TrialStats(SimSettings settings) {
		this.settings = settings;
		bankrollRuined = false;
		bankrollRuined50p = false;
		bankrollRuined25p = false;
	}

	public boolean isBankrollRuined() {
		return bankrollRuined;
	}

	public void setBankrollRuined(boolean bankrollRuined) {
		this.bankrollRuined = bankrollRuined;
	}
	
	public boolean isBankrollRuined50p() {
		return bankrollRuined50p;
	}

	public void setBankrollRuined50p(boolean bakrollRuined50p) {
		this.bankrollRuined50p = bakrollRuined50p;
	}

	public boolean isBankrollRuined25p() {
		return bankrollRuined25p;
	}

	public void setBankrollRuined25p(boolean bankrollRuined25p) {
		this.bankrollRuined25p = bankrollRuined25p;
	}

	public void setBankroll(double bankroll) {
		this.bankroll = bankroll;
	}

	public double getNetProfit() {
		return bankroll - settings.getStartingBankroll();
	}

}
