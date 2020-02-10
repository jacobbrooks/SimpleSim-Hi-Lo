package core;

import java.util.ArrayList;

public class DecisionSolver {

	private SimSettings settings;

	/*
	 * Table of decisions based on basic strategy but with filters for various
	 * conditions and index plays
	 */
	private Decision[][][] decisionTable;

	private ChartParser chartParser;

	private final int[] tableRows;
	private final int sections;
	private final int columns;

	private boolean tableLoaded;

	private int insuranceIndex;
	private boolean insuranceDeviationOn;
	
	private SimDataMapper mapper;

	public DecisionSolver(ChartParser chartParser) {
		this.chartParser = chartParser;
		mapper = new SimDataMapper();

		tableRows = new int[] { 18, 9, 10 };
		sections = 3;
		columns = 10;

		insuranceIndex = 3;
		insuranceDeviationOn = true;

		initTables();
		tableLoaded = false;
	}

	public void setSettings(SimSettings settings) {
		this.settings = settings;
	}

	private void initTables() {
		/*
		 * 18 possible hard hands 9 possible soft hands 10 possible pair hands
		 */
		decisionTable = new Decision[sections][][];

		for (int i = 0; i < sections; i++) {
			/*
			 * Tables have x rows of possible player hands, but always 10 columns of dealer
			 * up cards
			 */
			decisionTable[i] = new Decision[tableRows[i]][columns];
		}
	}

	/*
	 * loads basic strategy and deviations into the decision table
	 */
	public void loadDecisionTable(boolean basicStrategy) {
		for (int i = 0; i < decisionTable.length; i++) {
			for (int j = 0; j < decisionTable[i].length; j++) {
				for (int k = 0; k < decisionTable[i][j].length; k++) {

					if (!tableLoaded) {
						decisionTable[i][j][k] = new Decision();
					}

					decisionTable[i][j][k].setSettings(settings);

					if (basicStrategy) {
						// Load the correct basic strategy for user settings
						String basicStrategyDecision = chartParser.getBSChartAt(i, j, k);
						decisionTable[i][j][k].setBasicStrategy(basicStrategyDecision);
					} else {
						// Load the correct deviations for settings
						String deviationDecision = chartParser.getDevChartAt(i, j, k);
						if (deviationDecision.indexOf('/') != -1) {

							if (deviationDecision.indexOf(',') != -1) {
								String[] twoDevs = deviationDecision.split("[,]");
								decisionTable[i][j][k].setDeviation(twoDevs[0]);
								decisionTable[i][j][k].setSurrenderDeviation(twoDevs[1]);
							} else {
								decisionTable[i][j][k].setSurrenderDeviation("N");
								decisionTable[i][j][k].setDeviation(deviationDecision);
							}

						} else {
							decisionTable[i][j][k].setDeviation(deviationDecision);
							decisionTable[i][j][k].setSurrenderDeviation(deviationDecision);

							if (!deviationDecision.equals("N")) {
								decisionTable[i][j][k].setDeviation("N");
							}
						}
					}
				}
			}
		}
		tableLoaded = true;
	}

	/*
	 * Returns a player's bet based on true count
	 */
	public double getBettingDecision(Count count) {
		int rampIndex = 0;
		if (count.getTrueCount() > 0) {
			rampIndex = (int) Math.floor(count.getTrueCount());
			if (rampIndex >= settings.getBetRamp().length) {
				rampIndex = settings.getBetRamp().length - 1;
			}
		}
		return settings.getBetRamp()[rampIndex];
	}

	/*
	 * returns final playing decision from the decision table
	 */
	public String getPlayingDecision(int handTotal, ArrayList<Card> playerHand, Card upCard, String handType,
			int handSize, int splitCount, Count count) {

		/*
		 * SPECIAL CASE AFTER SPLITTING ACES 
		 * --------------------------------- 
		 * player is forced to stand after splitting aces, 
		 * unless re-splitting aces is allowed, in
		 * which case player will always re-split until 
		 * split limit, so no need to consult decision table
		 */
		if (playerHand.get(0).getRank().equals("A") && splitCount > 0) {

			// If first card is an ace after a split, then we must have just split aces

			if (settings.isRSA() && splitCount < settings.getSplitLimit() && playerHand.get(1).getRank().equals("A")) {
				return "split";
			}

			return "stand";
		}

		int i = mapper.mapHandTypeToSection(handType);
		int j = mapper.mapHandTotalToRow(handTotal, handType);
		int k = mapper.mapUpCardToColumn(upCard.getBJValue());

		return decisionTable[i][j][k].getDecision(handSize, splitCount, count);
	}

	/*
	 * Returns insurance decision as a String, (take or skip) based on true count if
	 * counting and deviations, but always skip if not counting/playing deviations
	 */
	public String getInsuranceDecision(Count count) {
		if (settings.isUseDeviations() && insuranceDeviationOn && count.getTrueCount() >= insuranceIndex) {
			return "take";
		}
		return "pass";
	}

	

	/*
	 * Returns an array containing index, aboveIndex and belowIndex
	 */
	public String[] getDeviationAt(int i, int j, int k, boolean surrender) {
		Decision d = decisionTable[i][j][k];
		String deviation = d.getDeviation();
		if (surrender) {
			deviation = d.getSurrenderDeviation();
		}
		String index = Integer.toString(d.devIndex(deviation));
		String aboveIndex = mapper.mapCharToDecision(d.aboveIndex(deviation));
		String belowIndex = "";
		if (!surrender) {
			belowIndex = mapper.mapCharToDecision(d.belowIndex(deviation));
		}
		String[] ret = { index, aboveIndex, belowIndex };
		return ret;
	}

	/*
	 * Inject a custom deviation into the decision object at i, j, k
	 */
	public void setDevitationAt(int i, int j, int k, String deviation, boolean surrender) {
		if (surrender) {
			decisionTable[i][j][k].setSurrenderDeviation(deviation);
		} else {
			decisionTable[i][j][k].setDeviation(deviation);
		}
	}

	/*
	 * Disables deviation in the decision table at location i, j, k
	 */
	public void setOffDeviationAt(int i, int j, int k, boolean surrender) {
		decisionTable[i][j][k].setDeviationState(false, surrender);
	}

	/*
	 * Re-enables deviation in the decision table at location i, j, k
	 */
	public void setOnDeviationAt(int i, int j, int k, boolean surrender) {
		decisionTable[i][j][k].setDeviationState(true, surrender);
	}

	public boolean isDeviationSetOnAt(int i, int j, int k, boolean surrender) {
		if (decisionTable[i][j][k].isDeviationOn(surrender)) {
			return true;
		}
		return false;
	}

	public boolean isInsuranceDeviationSetOn() {
		return insuranceDeviationOn;
	}

	public void setInsuranceDeviationOn() {
		insuranceDeviationOn = true;
	}

	public void setInsuranceDeviationOff() {
		insuranceDeviationOn = false;
	}

	public void setInsuranceIndex(int insuranceIndex) {
		this.insuranceIndex = insuranceIndex;
	}

	public int getInsuranceIndex() {
		return insuranceIndex;
	}

	/*
	 * Checks if a deviation exists in decision table at location i, j, k
	 */
	public boolean hasDeviationAt(int i, int j, int k, boolean surrender) {
		String deviation = decisionTable[i][j][k].getDeviation();
		if (surrender) {
			deviation = decisionTable[i][j][k].getSurrenderDeviation();
		}
		if (deviation == null || deviation.equals("N")) {
			return false;
		}
		return true;
	}

	public int sections() {
		return sections;
	}

	public int rowsAt(int section) {
		return tableRows[section];
	}

	public int columns() {
		return columns;
	}

}
