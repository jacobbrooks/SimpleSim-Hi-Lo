package core;

import java.util.Random;
import java.util.ArrayList;

public class Trial implements Runnable {
	
	private volatile boolean running;

	private SimSettings settings;

	private Random rand;

	private Card[] shoe;
	private int shoePos;
	private boolean ranOutOfCards;

	private Player player;
	private Player dealer;

	private Count count;

	private TrialStats stats;

	public Trial(SimSettings settings, DecisionSolver solver) {
		running = false;
		
		this.settings = settings;

		rand = new Random();

		shoe = generateShoe();
		shoe = shuffle();
		shoePos = 0;
		ranOutOfCards = false;

		count = new Count(settings);

		player = new Player(settings, solver, false);
		dealer = new Player(settings, null, true);

		player.setCount(count);

		stats = new TrialStats(settings);
	}

	public TrialStats getStats() {
		return stats;
	}

	private Card[] generateShoe() {
		Card[] shoe = new Card[settings.getDecks() * 52];
		int shoeIndex = 0;
		for (int i = 0; i < settings.getDecks(); i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 13; k++) {
					shoe[shoeIndex] = new Card(k);
					shoeIndex++;
				}
			}
		}
		return shoe;
	}

	/*
	 * Fisher - Yates
	 */
	private Card[] shuffle() {
		ArrayList<Card> oldShoe = new ArrayList<Card>();
		ArrayList<Card> newShoe = new ArrayList<Card>();
		Card[] retShoe = new Card[settings.getDecks() * 52];

		for (Card c : shoe) {
			c.setAceEqualEleven();
			oldShoe.add(c);
		}

		while (oldShoe.size() > 0) {
			int randShoePos = rand.nextInt(oldShoe.size());
			newShoe.add(oldShoe.get(randShoePos));
			oldShoe.remove(randShoePos);
		}

		return newShoe.toArray(retShoe);
	}

	/*
	 * Deals another card from the shoe to the player or dealer. We need to pass in
	 * the splitCount because player needs this information when updating hand
	 * totals for hands that contain aces in Player.hit(), (e.g. When forced to
	 * stand on a pair of aces, after already splitting a pair of aces, we want the
	 * hand total to count as 12 instead of 2 or 22)
	 * 
	 * If the shoe happens to run out of cards we need to shuffle the shoe and deal
	 * out the last round, but once that last round is over, we will need to
	 * re-shuffle for the start of the new shoe even though we just shuffled
	 * 
	 * We also need to know whether the dealt card is going to be the dealer's
	 * hole-card, because the dealer's hole card should not change the running count
	 * until it is revealed to the player later on
	 */
	private void dealCard(Player p, int splitCount, boolean holeCard) {
		if (shoePos == shoe.length) {
			reset();
			ranOutOfCards = true;
		}

		p.hit(shoe[shoePos], splitCount);

		if (!holeCard) {
			count.updateCount(shoe[shoePos]);
		}

		shoePos++;
		count.setShoePos(shoePos);
	}

	/*
	 * Handles logic associated with the insurance bet, and returns true if the
	 * round should end
	 */
	private boolean insuranceLogic(boolean playerBlackjack, boolean dealerBlackjack) {
		if (dealer.getUpCard().getRank().equals("A")) { // offer insurance

			if (player.getDecision("insurance", null, 0).equals("take")) {
				// player took insurance
				player.makeInsuranceBet();

				if (dealerBlackjack) {
					// dealer has blackjack

					player.winInsurance(); // Win insurance bet, 2:1 payoff

					if (!playerBlackjack) {
						player.loseBet(); // if player does not have blackjack then they lose original bet
					}

					return true;

				} else {
					/*
					 * Nobody home, player lost insurance bet
					 */
					player.loseInsurance();

					if (playerBlackjack) {
						/*
						 * player lost insurance bet but gets paid 3:2 on blackjack
						 */
						player.winBlackjack();
						return true;
					}

				}

			} else if (dealerBlackjack) {
				// player does not take insurance and dealer has blackjack

				if (!playerBlackjack) {
					/*
					 * player does not have blackjack and loses the hand
					 */
					player.loseBet();
				}

				return true;
			}

		}

		return false;
	}

	/*
	 * Handles logic associated with player and/or dealer getting a blackjack,
	 * returns true if the round should end
	 */
	private boolean blackjackLogic(boolean playerBlackjack, boolean dealerBlackjack) {
		if (dealerBlackjack) {
			/*
			 * Dealer has a ten up card, peeks for blackjack, and there's an ace in the hole
			 */
			if (!playerBlackjack) {
				/*
				 * player does not have blackjack and loses the hand
				 */
				player.loseBet();
			}

			return true;
		}

		if (playerBlackjack) {
			/*
			 * Player has blackjack and dealer does not. Player gets 3:2 payoff
			 */
			player.winBlackjack();
			return true;
		}

		return false;
	}

	private void playLogic(int splitCount) {

		// If no blackjacks then player plays the hand

		boolean playerBust = false;
		boolean playerStand = false;

		String decision = "";

		while (!(playerBust || playerStand)) {

			decision = player.getDecision("play", dealer.getUpCard(), splitCount);

			switch (decision) {
			case "hit":
				dealCard(player, splitCount, false);
				break;
			case "stand":
				playerStand = true;
				break;
			case "double":
				player.doubleBet();
				dealCard(player, splitCount, false);
				playerStand = true;
				break;
			case "split":
				player.split();
				splitCount++;
				dealCard(player, splitCount, false); // deal card to left hand
				break;
			case "surrender":
				player.surrenderBet();
				player.setHandNull(); // hand is dead after surrender
				playerStand = true;
				break;
			}

			/*
			 * A hand greater than 21 is a bust unless it is a pair of aces (A pair of aces
			 * is worth 22 when allowed to split)
			 */
			if (player.getHandTotal() > 21 && !player.hasAces()) {
				player.loseBet();
				player.setHandNull();
				playerBust = true;
			}

		}

		/*
		 * If player split, then there remains at least one hand yet to be played -
		 * advance to the next hand, deal that hand's second card, and recursively call
		 * this method to play out that next hand
		 */
		if (player.hasNextHand()) {
			player.nextHand();
			dealCard(player, splitCount, false);
			playLogic(splitCount);
		}

	}

	/*
	 * For each spot, player makes a bet and dealer deals the hand, then dealer
	 * deals himself, returns number of spots being played this round.
	 */
	private int dealTable() {
		int spots = getSpots();

		for (int i = 0; i < spots; i++) {
			player.makeBet();

			dealCard(player, 0, false);
			dealCard(player, 0, false);

			/*
			 * Only advance to the next spot if not already on the last spot
			 */
			if (i < spots - 1) {
				player.nextSpot(true);
			}
		}

		dealCard(dealer, 0, false);
		dealCard(dealer, 0, true); // This will be the dealer's hole-card

		player.resetCurrentSpot(); // set current spot back to first hand to play it out

		return spots;
	}

	/*
	 * Player's turn after being dealt initial hand(s)
	 */
	private void playerTurn(int currentNumSpots) {
		for (int i = 0; i < currentNumSpots; i++) {
			boolean endRound = false;

			boolean playerBlackjack = false;
			if (player.getHandTotal() == 21) {
				playerBlackjack = true;
			}

			boolean dealerBlackjack = false;
			if (dealer.getHandTotal() == 21) {
				dealerBlackjack = true;
			}

			endRound = insuranceLogic(playerBlackjack, dealerBlackjack); // first handle insurance logic

			if (!endRound) {
				endRound = blackjackLogic(playerBlackjack, dealerBlackjack); // then check for non-insurable blackjacks
			} else {
				player.setSpotNull(); // Dealer and/or player had blackjack so consider the hand dead
			}

			if (!endRound) {
				playLogic(0); // split count starts off at 0
			} else {
				player.setSpotNull(); // Player and/or dealer got blackjack so consider the hand dead
			}

			player.nextSpot(false);
		}
	}

	/*
	 * Dealer's turn when player still has live hands to draw against
	 */
	private void dealerTurn() {
		/*
		 * If dealer has a two aces as up-card and hole-card then we can now convert
		 * them to a 12
		 */
		if (dealer.getHandTotal() > 21) {
			dealer.convertTotalIfAce();
		}

		if (settings.isH17()) {
			while (dealer.getHandTotal() < 17 || (dealer.getHandTotal() == 17 && dealer.handType(0).equals("soft"))) {
				dealCard(dealer, 0, false);
			}
		} else {
			while (dealer.getHandTotal() < 17) {
				dealCard(dealer, 0, false);
			}
		}
	}

	/*
	 * House and/or player get payed off based on comparing player totals to dealer
	 * total
	 */
	private void settleBets(int currentNumSpots) {
		player.resetCurrentSpot();
		for (int i = 0; i < currentNumSpots; i++) {
			if (!player.deadSpot()) {
				while (true) {
					if (!player.deadHand()) {
						if (dealer.getHandTotal() > 21) {
							player.winBet(); // Dealer busted, player wins this hand
						} else {
							String winner = compareTotals();
							if (winner.equals("dealer")) {
								player.loseBet();
							} else if (winner.equals("player")) {
								player.winBet();
							}
						}
					}
					if (!player.hasNextHand()) {
						break;
					}
					player.nextHand();
				}
			}
			player.nextSpot(false);
		}
	}

	/*
	 * Compare the hand totals of dealer and player to determine winner. Return the
	 * winner of the hand as a string
	 */
	private String compareTotals() {
		if (dealer.getHandTotal() > player.getHandTotal()) {
			return "dealer";
		} else if (dealer.getHandTotal() < player.getHandTotal()) {
			return "player";
		}
		return "push";
	}

	/*
	 * Returns the number of spots player will be playing this round based on true
	 * count
	 */
	private int getSpots() {
		int rampIndex = 0;
		if (count.getTrueCount() > 0) {
			rampIndex = (int) Math.floor(count.getTrueCount());
			if (rampIndex >= settings.getSpotsRamp().length) {
				rampIndex = settings.getSpotsRamp().length - 1;
			}
		}
		return settings.getSpotsRamp()[rampIndex];
	}

	/*
	 * Returns true if cut card has been reached
	 */
	private boolean endShoe() {
		double decksRemaining = settings.getDecks() - (((double) shoePos) / 52);
		if (decksRemaining <= settings.getPenetration()) {
			return true;
		}
		return false;
	}

	/*
	 * Re-shuffles the shoe and resets all associated variables e.g shoePos,
	 * runningCount etc.
	 */
	private void reset() {
		this.shoe = shuffle();
		shoePos = 0;
		count = new Count(settings);
		player.setCount(count);
	}

	/*
	 * Advances through the shoe card by card until the count reaches player's
	 * wong-in entry point
	 */
	private void advanceToWongIn() {
		while (count.getTrueCount() < settings.getWongIn()) {
			count.updateCount(shoe[shoePos]);
			shoePos++;
			count.setShoePos(shoePos);

			if (endShoe()) {
				reset();
			}
		}
	}

	/*
	 * Logs statistics related to risk of ruin and bankroll size
	 */
	private void logRiskStatistics() {
		if (player.getBankroll() <= 0) {
			stats.setBankrollRuined(true);
		}
		if (player.getBankroll() <= ((1.0 / 2.0) * settings.getStartingBankroll())) {
			stats.setBankrollRuined50p(true);
		}
		if (player.getBankroll() <= ((3.0 / 4.0) * settings.getStartingBankroll())) {
			stats.setBankrollRuined25p(true);
		}
	}

	/*
	 * Simulate a single round of blackjack, will be called in a loop
	 */
	private void playRound() {
		/*
		 * Hands are dealt to the player and dealer
		 */
		int currentNumSpots = dealTable();

		/*
		 * Player hand(s) are played out
		 */

		playerTurn(currentNumSpots);

		/*
		 * Regardless of whether dealer ends up playing out his hand, dealer will still
		 * reveal hole-card, so update the count with it
		 */
		count.updateCount(dealer.getHoleCard());

		/*
		 * If a player still has at least one hand that survives, then dealer's turn
		 */
		if (player.hasLiveHand()) {
			dealerTurn();
			settleBets(currentNumSpots);
		}

	}

	/*
	 * Simulate X number of rounds by looping playRound() X times
	 * 
	 * Re-shuffle the shoe if need be
	 */
	private void runTrial() {

		boolean wongedOut = true;

		for (int i = 0; i < settings.getRoundsPerTrial(); i++) {
			/*
			 * Exit main loop if thread has 
			 * been shutdown
			 */
			if(!running) {
				break;
			}
			
			/*
			 * If backcounting advance deck to wong-in point
			 */
			if (settings.isBackcounting() && settings.isWonging() && wongedOut
					&& count.getTrueCount() < settings.getWongIn()) {
				advanceToWongIn();
			}

			wongedOut = false;

			/*
			 * play through round
			 */

			playRound();
			player.initNewRound();
			dealer.initNewRound();

			/*
			 * Set the wong-out flag if count drops too low, then we will know to advance to
			 * wong-in on the next iteration
			 */
			if ((settings.isWonging() && count.getTrueCount() < settings.getWongOut())) {
				wongedOut = true;
			}

			/*
			 * Reshuffle if it's the end of the shoe, or if we are wonging-out but not
			 * backcounting (meaning we leave the table, and start from the beginning of a
			 * newly shuffled shoe instead of advancing to wong-in)
			 */
			if (endShoe() || (wongedOut && !settings.isBackcounting()) || ranOutOfCards) {
				reset();
				ranOutOfCards = false;
			}

			/*
			 * Log statistics related to risk of ruin
			 */
			logRiskStatistics();
		}

		stats.setBankroll(player.getBankroll());
	}
	
	public void stopTrial() {
		running = false;
	}
	
	@Override
	public void run() {
		running = true;
		runTrial();
	}

}
