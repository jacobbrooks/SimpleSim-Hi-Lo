package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class MainWindow extends GUIWindow {

	private CheckBox h17;
	private CheckBox das;
	private CheckBox rsa;
	private CheckBox ls;
	private ComboBox decks;
	private TextField penetration;
	private TextField minBet;
	private TextField roundsPerHour;
	private TextField bankroll;
	private CheckBox deviations;
	private CheckBox wong;
	private CheckBox backcount;
	private TextField wongIn;
	private Button editDeviations;
	private Button runSim;
	private TextField wongOut;
	private TextField splitLimit;
	private Button betRampButton;

	public MainWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
	}

	@Override
	protected Scene getScene() {
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #3f5368;");
		grid.setMinSize(330, 600);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);

		ColumnConstraints c0 = new ColumnConstraints();
		c0.setPrefWidth(120);

		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPrefWidth(60);
		c1.setFillWidth(false);

		ColumnConstraints c2 = new ColumnConstraints();
		c1.setPrefWidth(60);
		c1.setFillWidth(false);

		grid.getColumnConstraints().addAll(c0, c1, c2);

		Text gameRules = new Text("Game Rules");
		gameRules.setFill(Paint.valueOf("orange"));
		gameRules.setStyle("-fx-font: italic bold 12px 'sans-serif';");

		h17 = new CheckBox();
		h17.setText("H17");
		h17.setTextFill(Paint.valueOf("white"));
		h17.setStyle("-fx-font: normal bold 10px 'sans-serif';");
		h17.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (h17.isSelected()) {
					settingsBuilder.setH17(true);
				} else {
					settingsBuilder.setH17(false);
				}
				controller.loadEngineDeviations();
			}

		});

		das = new CheckBox();
		das.setText("DAS");
		das.setTextFill(Paint.valueOf("white"));
		das.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		rsa = new CheckBox();
		rsa.setText("RSA");
		rsa.setTextFill(Paint.valueOf("white"));
		rsa.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		ls = new CheckBox();
		ls.setText("LS");
		ls.setTextFill(Paint.valueOf("white"));
		ls.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		Text splitLimitLabel = new Text("SPLIT LIMIT");
		splitLimitLabel.setFill(Paint.valueOf("white"));
		splitLimitLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		splitLimit = new TextField();

		Text splitLimitTimesLabel = new Text("(TIMES)");
		splitLimitTimesLabel.setFill(Paint.valueOf("" + Color.LIGHTGRAY));
		splitLimitTimesLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		// Game Conditions
		Text gameConditions = new Text("Game Conditions");
		gameConditions.setFill(Paint.valueOf("orange"));
		gameConditions.setStyle("-fx-font: italic bold 12px 'sans-serif';");

		Text decksLabel = new Text("DECKS");
		decksLabel.setFill(Paint.valueOf("white"));
		decksLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		decks = new ComboBox();
		decks.getItems().addAll("1", "2", "4", "6", "8");

		Text penetrationLabel = new Text("PENETRATION");
		penetrationLabel.setFill(Paint.valueOf("white"));
		penetrationLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		penetration = new TextField();

		Text cutOffLabel = new Text("(DECKS CUT OFF)");
		cutOffLabel.setFill(Paint.valueOf("" + Color.LIGHTGRAY));
		cutOffLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		Text minBetLabel = new Text("TABLE MINIMUM");
		minBetLabel.setFill(Paint.valueOf("white"));
		minBetLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		minBet = new TextField();
		
		Text roundsPerHourLabel = new Text("ROUNDS PER HOUR");
		roundsPerHourLabel.setFill(Paint.valueOf("white"));
		roundsPerHourLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		roundsPerHour = new TextField();
		
		Text roundsPlayedLabel = new Text("(PLAYED)");
		roundsPlayedLabel.setFill(Paint.valueOf("" + Color.LIGHTGRAY));
		roundsPlayedLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		// Player Settings
		Text playerSettings = new Text("Player Settings");
		playerSettings.setFill(Paint.valueOf("orange"));
		playerSettings.setStyle("-fx-font: italic bold 12px 'sans-serif';");

		Text bankrollLabel = new Text("BANKROLL");
		bankrollLabel.setFill(Paint.valueOf("white"));
		bankrollLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		bankroll = new TextField();

		deviations = new CheckBox();
		deviations.setText("DEVIATIONS");
		deviations.setTextFill(Paint.valueOf("white"));
		deviations.setStyle("-fx-font: normal bold 10px 'sans-serif';");
		deviations.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (deviations.isSelected()) {
					editDeviations.setDisable(false);
					controller.loadEngineDeviations();
				} else {
					editDeviations.setDisable(true);
				}
			}

		});

		wong = new CheckBox();
		wong.setText("WONGING");
		wong.setTextFill(Paint.valueOf("white"));
		wong.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		backcount = new CheckBox();
		backcount.setText("BACKCOUNTING");
		backcount.setTextFill(Paint.valueOf("white"));
		backcount.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		Text wongInLabel = new Text("WONG-IN");
		wongInLabel.setFill(Paint.valueOf("white"));
		wongInLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		wongIn = new TextField();

		Text wongOutLabel = new Text("WONG-OUT");
		wongOutLabel.setFill(Paint.valueOf("white"));
		wongOutLabel.setStyle("-fx-font: normal bold 10px 'sans-serif';");

		betRampButton = new Button();
		betRampButton.setText("Edit Bet Ramp");

		betRampButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if (fieldVal(minBet).matches("^\\d+$")) {
					String prevMinBet = Integer.toString((int) settingsBuilder.getMinBet());
					if (!fieldVal(minBet).equals(prevMinBet)) {
						Double minBetAsDouble = Double.parseDouble(fieldVal(minBet));
						settingsBuilder.setMinBet(minBetAsDouble);
						controller.launchWindow(GUIController.BET_RAMP_WINDOW, true);
					} else {
						controller.launchWindow(GUIController.BET_RAMP_WINDOW, false);
					}
				} else {
					controller.launchInvalidInputWindow("Table Minimum: Invalid Input");
				}

			}

		});

		runSim = new Button();
		runSim.setText("Run Simulation");
		runSim.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				controller.runSimulation();
			}

		});

		wongOut = new TextField();

		editDeviations = new Button("Edit Deviations");
		editDeviations.setDisable(true);
		editDeviations.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.launchWindow(GUIController.DEVIATION_LIST_WINDOW, true);
			}

		});

		grid.add(gameRules, 0, 0);
		grid.add(h17, 0, 2);
		grid.add(das, 0, 3);
		grid.add(rsa, 0, 4);
		grid.add(ls, 0, 5);
		grid.add(splitLimitLabel, 0, 6);
		grid.add(splitLimit, 1, 6);
		grid.add(splitLimitTimesLabel, 2, 6);

		grid.add(gameConditions, 0, 10);
		grid.add(decksLabel, 0, 12);
		grid.add(decks, 1, 12);
		grid.add(penetrationLabel, 0, 13);
		grid.add(penetration, 1, 13);
		grid.add(cutOffLabel, 2, 13);
		grid.add(minBetLabel, 0, 14);
		grid.add(minBet, 1, 14);
		grid.add(roundsPerHourLabel, 0, 15);
		grid.add(roundsPerHour, 1, 15);
		grid.add(roundsPlayedLabel, 2, 15);

		grid.add(playerSettings, 0, 19);
		grid.add(bankrollLabel, 0, 21);
		grid.add(bankroll, 1, 21);
		grid.add(deviations, 0, 23);
		grid.add(wong, 0, 24);
		grid.add(backcount, 0, 25);
		grid.add(wongInLabel, 0, 27);
		grid.add(wongIn, 1, 27);
		grid.add(wongOutLabel, 0, 28);
		grid.add(wongOut, 1, 28);

		grid.add(betRampButton, 0, 31);
		grid.add(editDeviations, 0, 32);

		grid.add(runSim, 2, 35);

		return new Scene(grid, 330, 630);
	}

	@Override
	protected void saveInput() {
		settingsBuilder.setH17(h17.isSelected()).setDAS(das.isSelected()).setRSA(rsa.isSelected())
				.setLS(ls.isSelected()).setSplitLimit(Integer.parseInt(splitLimit.getText()))
				.setDecks(Integer.parseInt(decks.getValue().toString()))
				.setPenetration(Double.parseDouble(penetration.getText()))
				.setRoundsPerHour(Integer.parseInt(roundsPerHour.getText()))
				.setStartingBankroll(Double.parseDouble(bankroll.getText()))
				.setUseDeviations(deviations.isSelected())
				.setWong(wong.isSelected()).setBackcount(backcount.isSelected());

		if (!wongIn.getText().isEmpty()) {
			settingsBuilder.setWongIn(Double.parseDouble(wongIn.getText()));
		}

		if (!wongOut.getText().isEmpty()) {
			settingsBuilder.setWongOut(Double.parseDouble(wongOut.getText()));
		}
	}

	@Override
	protected String invalidInputMessage() {
		String message = "";

		if (!fieldVal(splitLimit).matches("^[1-9]\\d*$")) {
			message = "Split Limit: invalid input";
		}

		if (decks.getValue() == null) {
			message = "You must select the number of decks!";
		}

		if (!fieldVal(penetration).matches("^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$")) {
			message = "Penetration: invalid input";
		} else if (decks.getValue() != null
				&& Double.parseDouble(fieldVal(penetration)) >= Integer.parseInt(decks.getValue().toString())) {
			message = "Penetration: out of range";
		}

		if (!fieldVal(bankroll).matches("^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$")) {
			message = "Bankroll: invalid input";
		}

		if (!fieldVal(minBet).matches("^\\d+$")) {
			message = "Table Minimum: invalid input";
		}
		
		if(!fieldVal(roundsPerHour).matches("^[1-9]\\d*$")) {
			message = "Rounds Per Hour: invalid input";
		}

		if (settingsBuilder.getBetRamp() == null) {
			message = "You must define a bet ramp!";
		}

		if (wong.isSelected()
				&& !fieldVal(wongOut).matches("^(?:[-]*[1-9]\\d*|0)?(?:\\.\\d+)?$")) {
			message = "You must define a Wong-Out point!";
		}

		if (backcount.isSelected()
				&& !fieldVal(wongIn).matches("^(?:[-]*[1-9]\\d*|0)?(?:\\.\\d+)?$")) {
			message = "You must define a Wong-In point!";
		}
		
		if (message.isEmpty() && (wong.isSelected() && backcount.isSelected())) {
			if(Double.parseDouble(fieldVal(wongIn)) < Double.parseDouble(fieldVal(wongOut))) {
				message = "Wong-In point cannot be less than Wong-Out point";
			}
		}

		return message;
	}

	public String getMinBet() {
		return minBet.getText();
	}

}
