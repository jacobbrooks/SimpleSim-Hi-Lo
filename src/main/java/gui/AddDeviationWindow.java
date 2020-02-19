package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class AddDeviationWindow extends GUIWindow {

	private ComboBox handType;
	private ComboBox handTotal;
	private ComboBox upCard;
	private TextField index;
	private ComboBox aboveIndex;
	private ComboBox belowIndex;

	public AddDeviationWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
	}

	@Override
	protected Scene getScene() {
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #3f5368;");
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(8);
		grid.setVgap(8);

		int[] columnWidths = { 100, 80, 80, 80, 120, 120 };
		ColumnConstraints[] constraints = new ColumnConstraints[6];
		for (int i = 0; i < constraints.length; i++) {
			constraints[i] = new ColumnConstraints();
			constraints[i].setPrefWidth(columnWidths[i]);
			grid.getColumnConstraints().add(constraints[i]);
		}

		Text windowTitle = new Text("Add Deviation");
		windowTitle.setStyle("-fx-font: italic bold 14px 'sans-serif';");
		windowTitle.setFill(Paint.valueOf("white"));

		handType = new ComboBox();
		handTotal = new ComboBox();
		upCard = new ComboBox();

		index = new TextField();
		index.setPrefWidth(40);

		aboveIndex = new ComboBox();
		belowIndex = new ComboBox();

		handTotal.setDisable(true);

		handType.getItems().addAll("hard", "soft", "pair", "insurance");

		handType.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				handTotal.setDisable(false);
				upCard.setDisable(false);
				aboveIndex.setDisable(false);
				belowIndex.setDisable(false);

				int minTotal = 0;
				int maxTotal = 0;

				handTotal.getItems().clear();

				if (handType.getValue().equals("hard")) {
					minTotal = 4;
					maxTotal = 20;
				} else if (handType.getValue().equals("soft")) {
					minTotal = 13;
					maxTotal = 20;
				} else if (handType.getValue().equals("pair")) {
					minTotal = 2;
					maxTotal = 11;
				} else {
					handTotal.setDisable(true);
					upCard.setDisable(true);
					aboveIndex.setDisable(true);
					belowIndex.setDisable(true);
				}

				for (int i = minTotal; i <= maxTotal; i++) {
					String s = i + "";
					if (handType.getValue().equals("pair")) {
						if (i == maxTotal) {
							s = "A";
						}
						s += "s";
					}
					handTotal.getItems().add(s);
				}

			}

		});

		aboveIndex.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (aboveIndex.getValue().toString().equals("surrender")) {
					belowIndex.setDisable(true);
					belowIndex.setValue("");
				} else {
					belowIndex.setDisable(false);
				}
			}

		});

		belowIndex.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (belowIndex.getValue().toString().equals("surrender")) {
					aboveIndex.setDisable(true);
					aboveIndex.setValue("");
				} else {
					aboveIndex.setDisable(false);
				}
			}

		});

		for (int i = 2; i <= 11; i++) {
			String s = i + "";
			if (i == 11) {
				s = "A";
			}
			upCard.getItems().add(s);
		}

		String[] decisions = { "hit", "stand", "double", "split", "surrender" };
		for (String d : decisions) {
			aboveIndex.getItems().add(d);
			belowIndex.getItems().add(d);
		}

		grid.add(windowTitle, 0, 0);

		String[] columnNames = { "Hand Type", "Player", "Dealer", "Index", "Above", "Below" };
		Text[] columnLabels = new Text[columnNames.length];
		for (int i = 0; i < columnLabels.length; i++) {
			columnLabels[i] = new Text();
			columnLabels[i].setText(columnNames[i]);
			columnLabels[i].setFill(Paint.valueOf("orange"));
			columnLabels[i].setStyle("-fx-font: italic bold 11px 'sans-serif';");
			grid.add(columnLabels[i], i, 1);
		}

		Button add = new Button("Add");
		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				String invalidInputMessage = invalidInputMessage();

				if (invalidInputMessage == null || invalidInputMessage.isEmpty()) {

					String handTypeVal = handType.getValue().toString().toLowerCase();

					int handTotalVal = 0;
					int upCardVal = 0;
					String aboveVal = "";
					String belowVal = "";

					if (!handTypeVal.equals("insurance")) {

						if (!handTypeVal.equals("pair")) {
							handTotalVal = Integer.parseInt(handTotal.getValue().toString());
						} else {
							handTotalVal = 22;
							if (!handTotal.getValue().toString().contains("A")) {
								String pair = handTotal.getValue().toString();
								pair = pair.substring(0, pair.length() - 1);
								handTotalVal = Integer.parseInt(pair) * 2;
							}
						}

						upCardVal = 11;
						if (!upCard.getValue().toString().equals("A")) {
							upCardVal = Integer.parseInt(upCard.getValue().toString());
						}

						aboveVal = controller.getDataMapper().mapDecisionToChar(aboveIndex.getValue().toString().toLowerCase());
						if (belowIndex.getValue() != null) {
							belowVal = controller.getDataMapper().mapDecisionToChar(belowIndex.getValue().toString().toLowerCase());
						}
					}

					int indexVal = Integer.parseInt(index.getText());

					controller.addCustomDeviation(handTypeVal, handTotalVal, upCardVal, indexVal, aboveVal, belowVal);

					controller.closeWindow(GUIController.DEVIATION_LIST_WINDOW);
					controller.launchWindow(GUIController.DEVIATION_LIST_WINDOW, true);
					stage.toFront();

				}else {
					controller.launchInvalidInputWindow(invalidInputMessage);
				}
			}
		});

		Button done = new Button("Done");
		done.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				close();
			}

		});

		Text space = new Text(" "); // Hacky way to get padding between buttons

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.TOP_CENTER);
		hBox.setStyle("-fx-background-color: #3f5368;");
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setPrefHeight(hBox.getPrefHeight() + 10);
		hBox.getChildren().addAll(add, space, done);

		grid.add(handType, 0, 2);
		grid.add(handTotal, 1, 2);
		grid.add(upCard, 2, 2);
		grid.add(index, 3, 2);
		grid.add(aboveIndex, 4, 2);
		grid.add(belowIndex, 5, 2);

		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: #3f5368;");
		pane.setTop(grid);
		pane.setBottom(hBox);

		return new Scene(pane, 580, 140);
	}

	@Override
	protected String invalidInputMessage() {

		String message = "";
	
		if(!comboBoxVal(handType).equals("pair") && (comboBoxVal(aboveIndex).equals("split") || comboBoxVal(belowIndex).equals("split"))) {
			message = "You cannot split a non-pair hand!";
		}

		if (comboBoxVal(belowIndex).equals(" ")) {
			if (!comboBoxVal(handType).equals("insurance")
					&& (comboBoxVal(aboveIndex).equals(" ") || !comboBoxVal(aboveIndex).equals("surrender"))) {
				message = "You must select a play below the index!";
			}
		}

		if (comboBoxVal(aboveIndex).equals(" ")) {
			if (!comboBoxVal(handType).equals("insurance")
					&& (comboBoxVal(belowIndex).equals(" ") || !comboBoxVal(belowIndex).equals("surrender"))) {
				message = "You must select a play above the index!";
			}
		}

		if (!fieldVal(index).matches("^[-]*[1-9]\\d*$")) {
			message = "You must enter a valid index number!";
		}

		if (comboBoxVal(upCard).equals(" ")) {
			if (!comboBoxVal(handType).equals("insurance")) {
				message = "You must select the dealer's up-card!";
			}
		}

		if (comboBoxVal(handTotal).equals(" ")) {
			if (!comboBoxVal(handType).equals("insurance")) {
				message = "You must select the player's hand!";
			}
		}

		if (comboBoxVal(handType).equals(" ")) {
			message = "You must select a hand type!";
		}
		
		return message;
	}

	@Override
	protected void saveInput() {
		// TODO Auto-generated method stub

	}

}
