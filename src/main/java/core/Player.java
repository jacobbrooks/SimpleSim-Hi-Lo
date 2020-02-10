package core;

import java.util.ArrayList;

public class Player {

	private SimSettings settings;

	/*
	 * An object that returns playing and betting decisions for the player
	 */
	private DecisionSolver solver;

	/*
	 * Contains the current Hi-Lo count, used by the decision solver
	 */
	private Count count;

	/*
	 * A list of spots the player is playing, containing a list of hands if the
	 * player split at a particular spot, containing a list of cards for each hand
	 */
	private ArrayList<ArrayList<ArrayList<Card>>> hands;

	/*
	 * A list of hand totals for each hand, for each spot being played
	 */
	private ArrayList<ArrayList<Integer>> handTotals;

	/*
	 * A list of bets for each hand, for each spot being played
	 */
	private ArrayList<ArrayList<Double>> bets;

	/*
	 * A list of insurance bets for each spot being played
	 */
	private ArrayList<Double> insuranceBets;

	/*
	 * Index of the current spot that is being played (Player can play multiple
	 * spots)
	 */
	private int currentSpot;

	/*
	 * Index of the current hand at the current spot (One spot could have multiple
	 * hands due to splits)
	 */
	private int currentHand;

	/*
	 * Indicates whether this Player object is dealer or player
	 */
	private boolean dealer;

	/*
	 * Player's current bankroll
	 */
	private double bankroll;

	public Player(SimSettings settings, DecisionSolver solver, boolean dealer) {
		this.settings = settings;
		this.dealer = dealer;

		if (!dealer) {
			this.solver = solver;
		}

		bankroll = settings.getStartingBankroll();

		initNewRound();
	}

	/*
	 * Returns the player's bankroll in its current state
	 */
	public double getBankroll() {
		return bankroll;
	}

	/*
	 * returns the current hand
	 */
	public ArrayList<Card> getHand() {
		return hands.get(currentSpot).get(currentHand);
	}

	/*
	 * returns the current hand's point score
	 */
	public int getHandTotal() {
		return handTotals.get(currentSpot).get(currentHand);
	}

	/*
	 * returns the index of the current spot in the hands list
	 */
	public int getCurrentSpotNum() {
		return currentSpot;
	}

	/*
	 * returns the index of the current hand in the hands list
	 */
	public int getCurrentHandNum() {
		return currentHand;
	}

	/*
	 * set the current hand as the next hand in the hands list
	 */
	public void nextHand() {
		currentHand++;
	}

	/*
	 * Sets the current spot in the spots list to null. (Needed to indicate when the
	 * round is over for the current spot if player got a blackjack)
	 */
	public void setSpotNull() {
		hands.set(currentSpot, null);
	}

	/*
	 * Sets the current hands to null if the hand busted, surrendered or was a
	 * blackjack
	 */
	public void setHandNull() {
		hands.get(currentSpot).set(currentHand, null);
	}

	/*
	 * Updates decision solver's count variable to the newly initialized, and
	 * current count object
	 */
	public void setCount(Count count) {
		this.count = count;
	}

	/*
	 * Resets the current spot back to the first spot
	 */
	public void resetCurrentSpot() {
		currentSpot = 0;
	}

	/*
	 * returns dealer's up card
	 */
	public Card getUpCard() {
		return getHand().get(0);
	}

	/*
	 * returns dealer's hole card
	 */
	public Card getHoleCard() {
		return getHand().get(1);
	}

	/*
	 * Consults the decision solver to make a proper sized bet
	 */
	public void makeBet() {
		bets.add(new ArrayList<Double>());
		bets.get(currentSpot).add(solver.getBettingDecision(count));
	}

	public void makeInsuranceBet() {
		insuranceBets.set(currentSpot, bets.get(currentSpot).get(currentHand) / 2);
	}

	public void doubleBet() {
		bets.get(currentSpot).set(currentHand, bets.get(currentSpot).get(currentHand) * 2);
	}

	public void surrenderBet() {
		bankroll -= bets.get(currentSpot).get(currentHand) / 2;
	}

	public void winBet() {
		bankroll += bets.get(currentSpot).get(currentHand);
	}

	public void winInsurance() {
		bankroll += insuranceBets.get(currentSpot) * 2; // Insurance pays 2:1
	}

	public void winBlackjack() {
		bankroll += 3 * bets.get(currentSpot).get(currentHand) / 2; // Blackjack pays 3:2
	}

	public void loseBet() {
		bankroll -= bets.get(currentSpot).get(currentHand);
	}

	public void loseInsurance() {
		bankroll -= insuranceBets.get(currentSpot);
	}

	public double getBet() {
		return bets.get(currentSpot).get(currentHand);
	}

	/*
	 * Initializes the current spot by:
	 * 
	 * Initializing a new list of hands for the current spot Initializing a new list
	 * of hand totals for current spot Initializing insurance bet at current spot to
	 * 0
	 */
	private void initCurrentSpot() {
		hands.add(new ArrayList<ArrayList<Card>>());
		hands.get(currentSpot).add(new ArrayList<Card>());

		handTotals.add(new ArrayList<Integer>());
		handTotals.get(currentSpot).add(0);

		insuranceBets.add(0.0);
	}

	/*
	 * Initializes/reinitializes all lists and variables that reset on a per round
	 * basis
	 */
	public void initNewRound() {
		hands = new ArrayList<ArrayList<ArrayList<Card>>>();
		handTotals = new ArrayList<ArrayList<Integer>>();
		bets = new ArrayList<ArrayList<Double>>();
		insuranceBets = new ArrayList<Double>();

		currentSpot = 0;
		currentHand = 0;

		initCurrentSpot();
	}

	/*
	 * returns true if player has a hand yet to be played after splitting
	 */
	public boolean hasNextHand() {
		if (currentHand < hands.get(currentSpot).size() - 1) {
			return true;
		}
		return false;
	}

	/*
	 * set the current spot as the next spot in the list and reset current hand to 0
	 * 
	 * If this method is being called while dealer is dealing then initialize the
	 * current spot
	 * 
	 */
	public void nextSpot(boolean dealing) {
		currentSpot++;
		currentHand = 0;

		/*
		 * Only initialize a new spot when dealer is dealing the table
		 */
		if (dealing) {
			initCurrentSpot();
		}
	}

	/*
	 * returns true if player still has at least one live hand that dealer must draw
	 * against
	 */
	public boolean hasLiveHand() {
		for (int i = 0; i < hands.size(); i++) {
			if (hands.get(i) != null) {
				for (int j = 0; j < hands.get(i).size(); j++) {
					if (hands.get(i).get(j) != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * Returns true if the current hand is a pair of aces
	 */
	public boolean hasAces() {
		String firstRank = getHand().get(0).getRank();
		String secondRank = getHand().get(1).getRank();
		if (getHand().size() == 2 && firstRank.equals("A") && firstRank.equals(secondRank)) {
			return true;
		}
		return false;
	}

	/*
	 * Returns true if there are no live hands at the current spot
	 */
	public boolean deadSpot() {
		if (hands.get(currentSpot) == null) {
			return true;
		}
		return false;
	}

	/*
	 * Returns true if the current hand at this spot is dead
	 */
	public boolean deadHand() {
		if (getHand() == null) {
			return true;
		}
		return false;
	}

	/*
	 * returns player's decision based on the decision type (e.g. insurance or hand
	 * play)
	 */
	public String getDecision(String decisionType, Card upCard, int splitCount) {
		if (decisionType.equals("insurance")) {
			return solver.getInsuranceDecision(count);
		}

		return solver.getPlayingDecision(getHandTotal(), getHand(), upCard, handType(splitCount), getHand().size(),
				splitCount, count);
	}

	public void hit(Card c, int splitCount) {
		getHand().add(c);
		int total = getHandTotal() + c.getBJValue();
		handTotals.get(currentSpot).set(currentHand, total);

		/*
		 * If total is greater than 21 check to see if there is an ace that is being
		 * interpreted as an eleven, then convert it to a one
		 * 
		 * EXCEPT, when hand is a pair of aces (22), then we should NOT convert the total for
		 * two reasons:
		 * 
		 * 1. For the dealer, we want the dealer's ace up-card to be worth 11 so we
		 * access the up-card column of the decision table properly
		 * (DecisionSolver.mapUpCardToColumn()), we will convert the aces to a 12 once
		 * it's the dealers turn to draw
		 * 
		 * 2. For the player, we want the pair of aces to have a total of 22 so that we
		 * access the hand-total row in decision table properly,
		 * (DecisionSolver.mapHandTotalToRow()), and then split the aces into two 11's
		 * 
		 * The only times we don't want a pair of aces to count as 22, and instead count
		 * as a 12, is when player is forced to stand on the pair of aces because
		 * they've already split a previous pair of aces, and either cannot re-split
		 * aces, or can re-split aces but have already reached the split limit.
		 */
		if (total > 21 && (getHand().size() > 2
				|| (hasAces() && ((!settings.isRSA() && splitCount > 0) 
						|| splitCount == settings.getSplitLimit())))) {
			convertTotalIfAce();
		}
	}

	/*
	 * If there is an ace in the hand equal to eleven, then set that ace equal to
	 * one and subtract 10 from hand total
	 */
	public void convertTotalIfAce() {
		for (int i = 0; i < getHand().size(); i++) {
			if (getHand().get(i).getBJValue() == 11) {
				getHand().get(i).setAceEqualOne();
				handTotals.get(currentSpot).set(currentHand, getHandTotal() - 10);
				break; // if hand has more than one ace, we only want to change the value of one of them
			}
		}
	}

	/*
	 * split the current hand into 2, and put up 2nd bet
	 */
	public void split() {
		Card c = getHand().get(1); // player's 2nd card

		getHand().remove(1);

		ArrayList<Card> newHand = new ArrayList<Card>();
		newHand.add(c);
		hands.get(currentSpot).add(newHand);

		handTotals.get(currentSpot).add(c.getBJValue());
		handTotals.get(currentSpot).set(currentHand, getHand().get(0).getBJValue()); // Reset hand total of first card

		bets.get(currentSpot).add(bets.get(currentSpot).get(currentHand)); // create new bet equal to original
	}

	/*
	 * Determines the hand type of the current hand (hard, soft, pair)
	 */
	public String handType(int splitCount) {
		/*
		 * If hand consists of only original two cards that are equal in value, and
		 * player hasn't reached the split limit yet, then hand is considered a pair
		 * 
		 * Also the dealer's hand is never considered a pair.
		 */
		if (!dealer && getHand().size() == 2
				&& (getHand().get(0).getBJValue() == getHand().get(1).getBJValue()
						|| getHand().get(0).getRank().equals(getHand().get(1).getRank()))
				&& splitCount < settings.getSplitLimit()) {

			return "pair";
		}

		/*
		 * If there is any Ace in the hand that still retains its 11 point value then it
		 * must be a soft hand
		 */
		for (Card c : getHand()) {
			if (c.getBJValue() == 11) {
				return "soft";
			}
		}

		return "hard";
	}

}
