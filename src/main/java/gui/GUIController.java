package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import core.DecisionSolver;
import core.SimDataMapper;
import core.SimEngine;
import core.SimSettings;
import core.SimSettings.SettingsBuilder;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GUIController extends Application {
	
	public static final int MAIN_WINDOW = 0;
	public static final int BET_RAMP_WINDOW = 1;
	public static final int DEVIATION_LIST_WINDOW = 2;
	public static final int ADD_DEVIATION_WINDOW = 3;
	public static final int PROGRESS_BAR_WINDOW = 4;
	public static final int RESULTS_WINDOW = 5;
	public static final int INVALID_INPUT_WINDOW = 6;
	
	private SettingsBuilder settings;
	
	private SimDataMapper mapper;

	private Task simWorker;
	private SimEngine engine;

	private GUIWindow[] windows;

	public void doLaunch(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		settings = new SimSettings.SettingsBuilder();
		engine = new SimEngine(settings);
		mapper = new SimDataMapper();

		windows = new GUIWindow[7];

		GUIWindow mainWindow = new MainWindow(this, "SimpleSim Hi-Lo");
		GUIWindow deviationListWindow = new DeviationListWindow(this, "Deviations");
		GUIWindow addDeviationWindow = new AddDeviationWindow(this, "Add/Edit Deviations");
		GUIWindow betRampWindow = new BetRampWindow(this, "Edit Bet Ramp");
		GUIWindow resultsWindow = new ResultsWindow(this, "Sim Results");
		GUIWindow progressBarWindow = new ProgressBarWindow(this, "Sim Progress");
		GUIWindow invalidInputWindow = new InvalidInputWindow(this, "Invalid Input");

		windows[MAIN_WINDOW] = mainWindow;
		windows[BET_RAMP_WINDOW] = betRampWindow;
		windows[DEVIATION_LIST_WINDOW] = deviationListWindow;
		windows[ADD_DEVIATION_WINDOW] = addDeviationWindow;
		windows[PROGRESS_BAR_WINDOW] = progressBarWindow;
		windows[RESULTS_WINDOW] = resultsWindow;
		windows[INVALID_INPUT_WINDOW] = invalidInputWindow;

		mainWindow.open(true);
	}

	private void defineSimSettings() {
		for (GUIWindow window : windows) {
			window.saveInput();
		}
	}

	public Task createWorker() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				Thread t = new Thread(engine);
				t.start();
				while (engine.getPercentComplete() < 100 && !isCancelled()) {
					updateProgress(engine.getPercentComplete(), 100);
				}
				if(!isCancelled()) {
					updateProgress(engine.getPercentComplete(), 100);
				}
				t.join();
				return true;
			}
		};
	}

	public void runSimulation() {
		
		String invalidInputMessage = "";

		GUIWindow mainWindow = windows[MAIN_WINDOW];
			
		invalidInputMessage = mainWindow.invalidInputMessage();

		if (!invalidInputMessage.isEmpty()) {
			launchInvalidInputWindow(invalidInputMessage);
		} else {
			defineSimSettings();

			ProgressBarWindow progressBar = (ProgressBarWindow) windows[PROGRESS_BAR_WINDOW];
			simWorker = createWorker();

			progressBar.open(true);
			progressBar.initBinding();

			simWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					ProgressBarWindow progressBar = (ProgressBarWindow) windows[PROGRESS_BAR_WINDOW];
					progressBar.close();
					engine.resetPercentComplete();
					ResultsWindow resultsWindow = (ResultsWindow) windows[RESULTS_WINDOW];
					resultsWindow.open(true);
				}

			});
			new Thread(simWorker).start();
		}

	}
	
	public void cancelSimulation() {
	    simWorker.cancel();
		engine.stopEngine();
	}

	public void addCustomDeviation(String handType, int handTotal, int upCard, int index, String above, String below) {

		DecisionSolver solver = engine.getSolver();

		if (!handType.equals("insurance")) {
			int i = mapper.mapHandTypeToSection(handType);
			int j = mapper.mapHandTotalToRow(handTotal, handType);
			int k = mapper.mapUpCardToColumn(upCard);

			String deviation = "(" + index + ":" + above;

			if (below != null) {
				deviation += "/" + below + ")";
				solver.setDevitationAt(i, j, k, deviation, false);
			} else {
				deviation += ")";
				solver.setDevitationAt(i, j, k, deviation, true);
			}

		} else {
			solver.setInsuranceIndex(index);
		}

	}

	public ArrayList<DeviationRow> getDeviationListRows() {

		ArrayList<DeviationRow> deviationRows = new ArrayList<DeviationRow>();

		DecisionSolver solver = engine.getSolver();

		/*
		 * Insurance deviation is a different format than the rest of the deviations and
		 * always goes at the top of the list
		 */
		DeviationRow insurance = new DeviationRow(this, 0, 0, 0);
		insurance.setHandType("insurance");
		insurance.setPlayerHand("-");
		insurance.setUpCard("-");
		insurance.setIndex(Integer.toString(solver.getInsuranceIndex()));
		insurance.setAboveIndex("take");
		insurance.setBelowIndex("pass");
		insurance.setCheckBoxState(engine.getSolver().isInsuranceDeviationSetOn());
		deviationRows.add(insurance);

		/*
		 * On the first iteration of 'n' we do our nested loop to grab all the
		 * non-surrender deviations from the solver and add them to the Deviation List
		 * to be presented in the GUI. On the second iteration of 'n' we do the same but
		 * for all the surrender deviations
		 */
		boolean surrenderDeviation = false;
		for (int n = 0; n < 2; n++) {
			for (int i = 0; i < solver.sections(); i++) {
				for (int j = 0; j < solver.rowsAt(i); j++) {
					for (int k = 0; k < solver.columns(); k++) {
						if (solver.hasDeviationAt(i, j, k, surrenderDeviation)) {
							String handType = mapper.mapSectionToHandType(i);
							int playerHand = mapper.mapRowToHandTotal(j, i);
							int upCard = mapper.mapColumnToUpCard(k);
							String[] indexPlay = solver.getDeviationAt(i, j, k, surrenderDeviation);

							DeviationRow deviation = new DeviationRow(this, i, j, k);
							deviation.setHandType(handType);

							deviation.setPlayerHand(Integer.toString(playerHand));
							if (handType.equals("pair")) {
								deviation.setPlayerHand(Integer.toString(playerHand) + "s");
							}

							deviation.setUpCard(Integer.toString(upCard));
							if (upCard == 11) {
								deviation.setUpCard("A");
							}

							deviation.setIndex(indexPlay[0]);
							deviation.setAboveIndex(indexPlay[1]);
							deviation.setBelowIndex("-");
							if (!surrenderDeviation) {
								deviation.setBelowIndex(indexPlay[2]);
							}

							deviation.setCheckBoxState(
									engine.getSolver().isDeviationSetOnAt(i, j, k, surrenderDeviation));

							deviationRows.add(deviation);
						}
					}
				}
			}
			surrenderDeviation = true;
		}

		return deviationRows;
	}

	public void launchWindow(int windowID, boolean rebuild) {
		windows[windowID].open(rebuild);
	}
	
	public void closeWindow(int windowID) {
		windows[windowID].close();
	}

	public Task getSimWorker() {
		return simWorker;
	}

	public SettingsBuilder getSettingsBuilder() {
		return settings;
	}
	
	public SimDataMapper getDataMapper() {
		return mapper;
	}

	public void saveResults() {
		Stage fileChooserStage = new Stage();
		fileChooserStage.initModality(Modality.WINDOW_MODAL);
		
		SimpleDateFormat formatter= new SimpleDateFormat("MM-dd-yyyy_HHmmss");
		Date date = new Date(System.currentTimeMillis());
		String today = formatter.format(date);
		
		String defaultFileName = "SimResults_" + today + ".txt";
		
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.setTitle("Save Results");
        fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialFileName(defaultFileName);

        File file = fileChooser.showSaveDialog(fileChooserStage);

        if (file != null) {
        	engine.writeResultsToFile(file);
        }
	}

	public String getSimResultAt(int index) {
		String[] simResults = engine.getResults();
		return simResults[index];
	}

	public void loadEngineDeviations() {
		engine.loadDeviationsIntoSolver();
	}

	public void setOnDeviationAt(int i, int j, int k, boolean surrender) {
		engine.getSolver().setOnDeviationAt(i, j, k, surrender);
	}

	public void setOffDeviationAt(int i, int j, int k, boolean surrender) {
		engine.getSolver().setOffDeviationAt(i, j, k, surrender);
	}

	public void setInsuranceDeviationOn() {
		engine.getSolver().setInsuranceDeviationOn();
	}

	public void setInsuranceDeviationOff() {
		engine.getSolver().setInsuranceDeviationOff();
	}

	public String getCurrentMinBet() {
		MainWindow mainWindow = (MainWindow) windows[MAIN_WINDOW];
		String minBet = mainWindow.getMinBet();
		return minBet;
	}

	public void launchInvalidInputWindow(String message) {
		InvalidInputWindow invalidWindow = (InvalidInputWindow) windows[INVALID_INPUT_WINDOW];
		invalidWindow.setMessage(message);
		invalidWindow.open(true);
	}


}
