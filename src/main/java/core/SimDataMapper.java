package core;

/*
 * This class contains data mapping/conversion
 * functions that both the engine and GUI use
 * for various purposes
 */
public class SimDataMapper {
	
	/*
	 * returns the section coordinate of the decision table corresponding to the
	 * player's hand type
	 */
	public int mapHandTypeToSection(String handType) {
		switch (handType) {
		case "hard":
			return 0;
		case "soft":
			return 1;
		case "pair":
			return 2;
		default:
			return -1;
		}
	}

	/*
	 * Returns the string hand type of a given section
	 */
	public String mapSectionToHandType(int section) {
		String[] sections = { "hard", "soft", "pair" };
		return sections[section];
	}

	/*
	 * returns the row coordinate in the decision table corresponding to player's
	 * hand total / hand type
	 */
	public int mapHandTotalToRow(int handTotal, String handType) {
		if (handType.equals("hard")) {
			return handTotal - 4;
		} else if (handType.equals("soft")) {
			return handTotal - 13;
		}
		return (handTotal / 2) - 2;
	}

	/*
	 * returns the hand total for a given row
	 */
	public int mapRowToHandTotal(int row, int section) {
		int[] totals = { row + 4, row + 13, row + 2 };
		return totals[section];
	}

	/*
	 * returns the column coordinate in the decision table corresponding to the
	 * dealer's up card
	 */
	public int mapUpCardToColumn(int upCardValue) {
		return upCardValue - 2;
	}

	/*
	 * returns the up card value for a given column
	 */
	public int mapColumnToUpCard(int column) {
		return column + 2;
	}
	
    /*
     * Converts full word player decisions
     * the engine can understand to single 
     * characters, which is the data format
     * the chart parser uses/understands
     */
	public String mapDecisionToChar(String decision) {
		switch (decision) {
		case "hit":
			return "H";
		case "stand":
			return "S";
		case "double":
			return "D";
		case "split":
			return "P";
		case "surrender":
			return "R";
		default:
			return null;
		}
	}
	
	/*
	 * Takes in single character basic strategy/deviation and converts it to a
	 * decision String the player object can understand
	 */
	public String mapCharToDecision(String d) {
		switch (d.toUpperCase()) {
		case "H":
			return "hit";
		case "S":
			return "stand";
		case "D":
			return "double";
		case "P":
			return "split";
		case "R":
			return "surrender";
		default:
			return null;
		}
	}
}
