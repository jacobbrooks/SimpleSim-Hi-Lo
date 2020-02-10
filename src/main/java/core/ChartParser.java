package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import core.SimSettings.SettingsBuilder;

/*
 * This class loads 
 * the correct 
 * basic strategy and deviation charts
 * from disc to ram
 */
public class ChartParser {

	private SettingsBuilder settings;

	private Scanner sc;
	private String[][][] bsChart; // basic strategy chart split into 3 sections (hard hands, soft hands, pairs)
	private String[][][] devChart; // Deviation chart split into 3 sections (hard hands, soft hands, pairs)

	private final int[] chartRows;

	public ChartParser(SettingsBuilder settings) {
		this.settings = settings;
		bsChart = new String[3][][];
		devChart = new String[3][][];
		chartRows = new int[] { 18, 9, 10 };
	}

	/*
	 * Loads either Basic Strategy or Deviation Chart into memory
	 */
	public void loadChart(boolean basicStrategy) {
		String[][][] chart = bsChart;
		String fileName = "charts/bscharts/" + getBSChartFileName() + ".txt";
		if (!basicStrategy) {
			chart = devChart;
			fileName = "charts/devcharts/" + getDevChartFileName() + ".txt";
		}
		
		String chartFile = "";
		
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classloader.getResourceAsStream(fileName);
		InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(streamReader);
		try {
			for (String line; (line = reader.readLine()) != null;) {
			   chartFile += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		sc = new Scanner(chartFile);
		
		for (int i = 0; i < chart.length; i++) {
			chart[i] = new String[chartRows[i]][10];
		}
		parseChart(basicStrategy);
	}

	/*
	 * Builds the file name for the correct basic strategy chart based on user
	 * settings
	 */
	private String getBSChartFileName() {
		String fileName = "";

		if (settings.getDecks() >= 4) {
			/*
			 * basic strategy does not change based on decks once the amount of decks is ≥ 4
			 */
			fileName += "4d";
		} else {
			fileName += Integer.toString(settings.getDecks()) + "d";
		}

		if (settings.isH17()) {
			fileName += "h17";
		} else {
			fileName += "s17";
		}

		if (settings.isDAS()) {
			fileName += "das";
		} else {
			fileName += "ndas";
		}

		if (settings.isLS()) {
			fileName += "lsr";
		} else {
			fileName += "nlsr";
		}

		return fileName;
	}

	/*
	 * Returns the file name for the correct deviation chart
	 */
	private String getDevChartFileName() {
		if (settings.isH17()) {
			return "h17";
		}
		return "s17";
	}

	/*
	 * loads the basic strategy or deviation chart into a 3d array of strings, where
	 * each string is a basic strategy or deviation decision
	 */
	private void parseChart(boolean basicStrategy) {
		int chartSection = 0;
		int sectionRow = 0;

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (!line.equals("=")) {
				String delim = "";
				int arrayLength = 0;
				if (basicStrategy) {
					delim = "[-]";
					arrayLength = bsChart[chartSection][sectionRow].length;
				} else {
					delim = "[|]";
					arrayLength = devChart[chartSection][sectionRow].length;
				}
				String[] row = line.split(delim);
				for (int i = 0; i < arrayLength; i++) {
					if (basicStrategy) {
						bsChart[chartSection][sectionRow][i] = row[i];
					} else {
						devChart[chartSection][sectionRow][i] = row[i];
					}
				}
				sectionRow++;
			} else {
				chartSection++;
				sectionRow = 0;
			}
		}
		sc.close();
	}

	public String getBSChartAt(int i, int j, int k) {
		return bsChart[i][j][k];
	}

	public String getDevChartAt(int i, int j, int k) {
		return devChart[i][j][k];
	}

}
