package gui;

import java.util.ArrayList;

import core.SimSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class DeviationListWindow extends GUIWindow {

	private ArrayList<DeviationRow> deviationRows;

	public DeviationListWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
	}

	@Override
	protected Scene getScene() {
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #3f5368;");
		grid.setMinSize(600, 600);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(50);

		deviationRows = controller.getDeviationListRows();

		String[] columnNames = { "Hand Type", "Player", "Dealer", "Index", "Above", "Below", "Set On" };
		Text[] columnLabels = new Text[columnNames.length];

		for (int i = 0; i < columnLabels.length; i++) {
			columnLabels[i] = new Text();
			columnLabels[i].setText(columnNames[i]);
			columnLabels[i].setFill(Paint.valueOf("orange"));
			columnLabels[i].setStyle("-fx-font: italic bold 11px 'sans-serif';");
			grid.add(columnLabels[i], i, 0);
		}

		for (int i = 0; i < deviationRows.size(); i++) {
			DeviationRow deviation = deviationRows.get(i);
			grid.add(deviation.getHandType(), 0, i + 1);
			grid.add(deviation.getPlayerHand(), 1, i + 1);
			grid.add(deviation.getUpCard(), 2, i + 1);
			grid.add(deviation.getIndex(), 3, i + 1);
			grid.add(deviation.getAboveIndex(), 4, i + 1);
			grid.add(deviation.getBelowIndex(), 5, i + 1);
			grid.add(deviation.getCheckBoxOn(), 6, i + 1);
		}

		ScrollPane scroll = new ScrollPane();
		scroll.setContent(grid);

		HBox hBox = new HBox();
		hBox.setStyle("-fx-background-color: #3f5368;");
		hBox.setPrefSize(620, 50);
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setAlignment(hBox.getAlignment().BOTTOM_RIGHT);

		Button done = new Button("Done");
		done.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				close();
			}

		});

		Button addOrEdit = new Button("Add/Edit");
		addOrEdit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.launchWindow(GUIController.ADD_DEVIATION_WINDOW, true);
			}

		});

		Text space = new Text(" "); // Hacky way to get some padding between each button

		hBox.getChildren().add(addOrEdit);
		hBox.getChildren().add(space);
		hBox.getChildren().add(done);

		String titleString = "S17 Deviations";
		if (settingsBuilder.isH17()) {
			titleString = "H17 Deviations";
		}

		HBox titleBox = new HBox();
		titleBox.setStyle("-fx-background-color: #3f5368;");
		titleBox.setPrefSize(620, 50);

		Text titleLabel = new Text(titleString);
		titleLabel.setFill(Paint.valueOf("orange"));
		titleLabel.setStyle("-fx-font: normal bold 22px 'sans-serif';");
		titleBox.setAlignment(titleBox.getAlignment().CENTER);
		titleBox.getChildren().add(titleLabel);

		BorderPane pane = new BorderPane();
		pane.setPrefSize(620, 500);
		pane.setTop(titleBox);
		pane.setCenter(scroll);
		pane.setBottom(hBox);

		return new Scene(pane, 660, 500);
	}

	@Override
	protected String invalidInputMessage() {
		return null;
	}

	@Override
	protected void saveInput() {
		// TODO Auto-generated method stub

	}

}
