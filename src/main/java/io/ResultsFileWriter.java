package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import core.SimEngine;
import core.SimSettings;

public class ResultsFileWriter {

	private SimSettings settings;
	private SimEngine engine;

	public ResultsFileWriter(SimSettings settings, SimEngine engine) {
		this.settings = settings;
		this.engine = engine;
	}

	public void writeResults(File file) {
		try {
			
			PrintWriter p = new PrintWriter(file);

			p.println(" ______________________________ ");
			p.println("|                              |");
			p.println("| S i m p l e S i m  H i - L o |");
			p.println("|                              |");
			p.println("|      Simulation Results      |");
			p.println("|______________________________|");

			p.println();
			p.println();

			p.println("PLAYER SETTINGS ");

			p.println();

			p.println("   Bankroll: " + settings.getStartingBankroll());
			p.println("   Bet Ramp:");

			p.println();

			for (int i = 0; i < settings.getBetRamp().length; i++) {
				String tc = "";
				if (i == 0) {
					tc = "True Count <= ";
				} else if (i < 6) {
					tc = "True Count = ";
				} else {
					tc = "True Count >= ";
				}
				p.println("      " + tc + i + ": " + settings.getSpotsRamp()[i] + " x $" + settings.getBetRamp()[i]);
			}

			p.println();

			p.println("   Deviations: " + boolToStr(settings.isUseDeviations()));
			p.println("   Wong: " + boolToStr(settings.isWonging()));
			p.println("   Backcount: " + boolToStr(settings.isBackcounting()));
			if(settings.isBackcounting()) {
				p.println("   Wong-In: " + settings.getWongIn());
			}
			if(settings.isWonging()) {
				p.println("   Wong-Out: " + settings.getWongOut());
			}
			p.println();

			p.println("GAME CONDITIONS ");

			p.println();

			p.println("   Table Min: " + settings.getMinBet());
			p.println("   Decks: " + settings.getDecks());
			p.println("   Penetration: " + settings.getPenetration());
			p.println("   Rounds Per Hour: " + settings.getRoundsPerHour());

			p.println();

			p.println("GAME RULES ");

			p.println();

			p.println("   H17: " + boolToStr(settings.isH17()));
			p.println("   DAS: " + boolToStr(settings.isDAS()));
			p.println("   RSA: " + boolToStr(settings.isRSA()));
			p.println("   LS: " + boolToStr(settings.isLS()));
			p.println("   Split Limit: " + settings.getSplitLimit() + " times");

			p.println();

			p.println("RESULTS: ");

			p.println();

			String[] results = engine.getResults();
			
			p.println("   (Rounds)");
			p.println();
			p.println("      EV: $" + results[0] + " / Round Played");
			p.println("      Std Dev: $" + results[1] + " / Round Played");
			p.println("      N0: " + results[2] + " Rounds");
			p.println();
			p.println("   (Hours)");
			p.println();
			p.println("      EV: $" + results[3] + " / Hour");
			p.println("      Std Dev: $" + results[4] + " / Hour");
			p.println("      N0: " + results[5] + " Hours");
			p.println();
			p.println("   Risk of Ruin: " + results[6] + " %");
			p.println("   Risk of 1/2 Ruin: " + results[7] + " %");
			p.println("   Risk of 1/4 Ruin: " + results[8] + " %");
			
			p.println();
			p.println();

			p.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String boolToStr(boolean b) {
		if (b)
			return "Yes";
		return "No";
	}

}
