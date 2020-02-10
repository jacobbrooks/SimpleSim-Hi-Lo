package core;

public class Decision {

	private SimSettings settings;

	private String basicStrategy;
	private String deviation;
	private String surrenderDeviation;

	private boolean deviationOn;
	private boolean surrenderDeviationOn;
	
	private SimDataMapper mapper;

	public Decision() {
		deviationOn = true;
		surrenderDeviationOn = true;
		mapper = new SimDataMapper();
	}

	public void setSettings(SimSettings settings) {
		this.settings = settings;
	}

	public void setBasicStrategy(String basicStrategy) {
		this.basicStrategy = basicStrategy;
	}

	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}

	public void setSurrenderDeviation(String surrenderDeviation) {
		this.surrenderDeviation = surrenderDeviation;
	}

	public String getDeviation() {
		return deviation;
	}

	public String getSurrenderDeviation() {
		return surrenderDeviation;
	}

	/*
	 * Sets a the deviation (or surrender deviation) for this decision object on or
	 * off
	 */
	public void setDeviationState(boolean state, boolean surrender) {
		if (!surrender) {
			deviationOn = state;
		} else {
			surrenderDeviationOn = state;
		}
	}

	public boolean isDeviationOn(boolean surrender) {
		if (!surrender && deviationOn) {
			return true;
		}

		if (surrender && surrenderDeviationOn) {
			return true;
		}

		return false;
	}

	/*
	 * Returns true if player can no longer double or surrender, (If player could no
	 * longer split, it would have accessed the decisionTable as a hard hand, and
	 * would not have gotten to this point)
	 */
	private boolean pastInitialHand(int handSize, int splitCount) {
		/*
		 * Hand size is greater than 2 so player already hit. OR Split count is greater
		 * than 0 so player has already split.
		 * 
		 * Therefore:
		 * -----------------------------------------------------------------------------
		 * -- For Deviations:
		 * 
		 * Player can no longer elect to double or surrender as a part of an index play
		 * 
		 * For Basic Strategy:
		 * 
		 * 1.If player has already hit, then he will always choose the otherwise option,
		 * e.g. If 'Dh' (double otherwise hit) then player will just hit
		 * 
		 * 2. If player already split, then he cannot surrender, and will choose
		 * otherwise option, e.g. If 'Rh' (surrender otherwise hit) then player will
		 * just hit
		 * 
		 * 3. If player already split, and double after split is not allowed, then
		 * player will choose otherwise option, e.g If 'Ds' (double otherwise stand)
		 * then player will just stand
		 * 
		 */
		if (handSize > 2 || (splitCount > 0
				&& (bsInitial().equalsIgnoreCase("R") || (!settings.isDAS() && bsInitial().equalsIgnoreCase("D"))))) {
			return true;
		}
		return false;
	}

	private boolean noSurrenderDeviation() {
		if (surrenderDeviation == null || surrenderDeviation.equals("N")) {
			return true;
		}
		return false;
	}

	public String getDecision(int handSize, int splitCount, Count count) {

		boolean chooseOtherwise = false;

		if (settings.isUseDeviations()) {

			if (settings.isLS()) {

				if (noSurrenderDeviation() || !surrenderDeviationOn) {
					if (bsInitial().equals("R") && !pastInitialHand(handSize, splitCount)) {
						/*
						 * If there is no surrender index but basic strategy says surrender, then just
						 * surrender. We don't want to resort to a hit/stand deviation when we're
						 * allowed to still surrender
						 */
						return mapper.mapCharToDecision(bsInitial());
					}
				} else {
					// There is a surrender deviation
					if (!pastInitialHand(handSize, splitCount)) {

						// We are allowed to surrender
						if (count.getTrueCount() >= devIndex(surrenderDeviation)) {
							return mapper.mapCharToDecision(aboveIndex(surrenderDeviation));
						} else {
							/*
							 * We just chose not to surrender because True Count is not high enough. If
							 * there is no other deviation for this hand, then we will end up resorting to
							 * basic strategy. Basic Strategy may tell us to surrender, but we know better
							 * than that now, so we set this flag to indicate that player should choose
							 * otherwise-option instead of surrendering.
							 */
							chooseOtherwise = true;
						}

					}

				}

			}

			// We did not make a surrender deviation, but there might be other index plays
			// for this hand

			if (!deviation.equals("N") && deviationOn) {
				// There is a deviation for this hand
				if (count.getTrueCount() >= devIndex(deviation)) {
					if (!pastInitialHand(handSize, splitCount)
							|| (pastInitialHand(handSize, splitCount) && !aboveIndex(deviation).equals("D"))) {
						/*
						 * The true count is at the proper index to deviate, and we are either still on
						 * the initial hand (So all playing options are on the table), or we are
						 * mid-hand, but deviation doesn't call for player to double
						 */
						return mapper.mapCharToDecision(aboveIndex(deviation));
					}
				}

				// True count did not meet index, so at this point we would take the belowIndex
				// decision
				return mapper.mapCharToDecision(belowIndex(deviation));
			}

		}

		// At this point there were no index plays made so just play basic strategy

		if (!pastInitialHand(handSize, splitCount)) {

			// Hand is still the initial 2 cards

			if (chooseOtherwise && bsInitial().equals("R")) {
				/*
				 * This is a special case where we elected to not surrender according to a
				 * surrender deviation, and there is no hit/stand deviation to fall back on. If
				 * Basic Strategy says to surrender, then we will instead choose the
				 * otherwise-option because we already determined that we didn't want to
				 * surrender
				 */
				return mapper.mapCharToDecision(bsOtherwise());
			}

			return mapper.mapCharToDecision(bsInitial());

		} else if (basicStrategy.length() > 1) {
			/*
			 * Hand is past initial 2 cards, so we choose Basic Strategy otherwise-option
			 */
			return mapper.mapCharToDecision(bsOtherwise());
		}

		// Hand is past initial 2 cards but Basic Strategy only has one option
		return mapper.mapCharToDecision(bsInitial());

	}

	/*
	 * Returns the true count at which the deviation is made
	 */
	public int devIndex(String deviation) {
		int colon = deviation.indexOf(':');
		String index = deviation.substring(1, colon);
		int ret = Integer.parseInt(index);
		return ret;
	}

	/*
	 * Returns decision to take above index
	 */
	public String aboveIndex(String deviation) {
		int colon = deviation.indexOf(':');
		String ret = deviation.charAt(colon + 1) + "";
		return ret;
	}

	/*
	 * Returns decision to take below index. Cannot take surrenderDeviation as an
	 * argument (surrender deviations don't have a below index, and therefore no '/'
	 * character)
	 */
	public String belowIndex(String deviation) {
		int slash = deviation.indexOf('/');
		String ret = deviation.charAt(slash + 1) + "";
		return ret;
	}

	/*
	 * Returns the first basic strategy option, e.g. when 'Ds' (double otherwise
	 * stand), return double
	 */
	private String bsInitial() {
		return Character.toString(basicStrategy.charAt(0));
	}

	/*
	 * Returns the second basic strategy option, e.g. when 'Ds' (double otherwise
	 * stand), return stand
	 */
	private String bsOtherwise() {
		return Character.toString(basicStrategy.charAt(1));
	}

}
