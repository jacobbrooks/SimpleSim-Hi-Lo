package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class ResultsWindow extends GUIWindow {

	public ResultsWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
	}

	@Override
	protected Scene getScene() {
		GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #3f5368;");
		grid.setMinSize(310, 120);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);

		ColumnConstraints c0 = new ColumnConstraints();
		c0.setPrefWidth(130);
		grid.getColumnConstraints().add(c0);
		
		Text roundsLabel = new Text("(Rounds)");
		roundsLabel.setFill(Paint.valueOf("white"));
		roundsLabel.setStyle("-fx-font: italic bold 12px 'sans-serif';");
		
		Text hoursLabel = new Text("(Hours)");
		hoursLabel.setFill(Paint.valueOf("white"));
		hoursLabel.setStyle("-fx-font: italic bold 12px 'sans-serif';");

		Text evLabel = new Text("EV:");
		evLabel.setFill(Paint.valueOf("orange"));
		evLabel.setStyle("-fx-font: normal bold 12px 'sans-serif';");

		Text stdDevLabel = new Text("STD DEV:");
		stdDevLabel.setFill(Paint.valueOf("orange"));
		stdDevLabel.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text n0Label = new Text("N0:");
		n0Label.setFill(Paint.valueOf("orange"));
		n0Label.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text evLabel2 = new Text("EV:");
		evLabel2.setFill(Paint.valueOf("orange"));
		evLabel2.setStyle("-fx-font: normal bold 12px 'sans-serif';");

		Text stdDevLabel2 = new Text("STD DEV:");
		stdDevLabel2.setFill(Paint.valueOf("orange"));
		stdDevLabel2.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text n0Label2 = new Text("N0:");
		n0Label2.setFill(Paint.valueOf("orange"));
		n0Label2.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text rorLabel = new Text("RISK OF RUIN:");
		rorLabel.setFill(Paint.valueOf("orange"));
		rorLabel.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text rorHalfLabel = new Text("RISK OF 1/2 RUIN:");
		rorHalfLabel.setFill(Paint.valueOf("orange"));
		rorHalfLabel.setStyle("-fx-font: normal bold 12px 'sans-serif';");

		Text rorQuarterLabel = new Text("RISK OF 1/4 RUIN:");
		rorQuarterLabel.setFill(Paint.valueOf("orange"));
		rorQuarterLabel.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text resultsLabel = new Text("SIMULATION RESULTS");
		resultsLabel.setFill(Paint.valueOf("gray"));
		resultsLabel.setStyle("-fx-font: normal bold 16px 'sans-serif';");

		Text evRounds = new Text("$" + controller.getSimResultAt(0) + " / Round Played");
		evRounds.setFill(Paint.valueOf("white"));
		evRounds.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text stdDevRounds = new Text("$" + controller.getSimResultAt(1) + " / Round Played");
		stdDevRounds.setFill(Paint.valueOf("white"));
		stdDevRounds.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text n0Rounds = new Text(controller.getSimResultAt(2) + " Rounds");
		n0Rounds.setFill(Paint.valueOf("white"));
		n0Rounds.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text evHours = new Text("$" + controller.getSimResultAt(3) + " / Hour");
		evHours.setFill(Paint.valueOf("white"));
		evHours.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text stdDevHours = new Text("$" + controller.getSimResultAt(4) + " / Hour");
		stdDevHours.setFill(Paint.valueOf("white"));
		stdDevHours.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text n0Hours = new Text(controller.getSimResultAt(5) + " Hours");
		n0Hours.setFill(Paint.valueOf("white"));
		n0Hours.setStyle("-fx-font: normal bold 12px 'sans-serif';");

		Text ror = new Text(controller.getSimResultAt(6) + " %");
		ror.setFill(Paint.valueOf("white"));
		ror.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text halfRor = new Text(controller.getSimResultAt(7) + " %");
		halfRor.setFill(Paint.valueOf("white"));
		halfRor.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		
		Text quartRor = new Text(controller.getSimResultAt(8) + " %");
		quartRor.setFill(Paint.valueOf("white"));
		quartRor.setStyle("-fx-font: normal bold 12px 'sans-serif';");
		

		Button save = new Button("Save Results");
		save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {	
				controller.saveResults();
				close();
			}

		});

		grid.add(resultsLabel, 0, 0);
		
		grid.add(roundsLabel, 0, 2);
		grid.add(hoursLabel, 4, 2);

		grid.add(evLabel, 0, 4);
		grid.add(evRounds, 1, 4);
		
		grid.add(stdDevLabel, 0, 5);
		grid.add(stdDevRounds, 1, 5);
		
		grid.add(n0Label, 0, 6);
		grid.add(n0Rounds, 1, 6);
		
		grid.add(evLabel2, 4, 4);
		grid.add(evHours, 5, 4);
		
		grid.add(stdDevLabel2, 4, 5);
		grid.add(stdDevHours, 5, 5);
		
		grid.add(n0Label2, 4, 6);
		grid.add(n0Hours, 5, 6);

		grid.add(rorLabel, 0, 8);
		grid.add(ror, 1, 8);	
		
		grid.add(rorHalfLabel, 0, 9);
		grid.add(halfRor, 1, 9);
		
		grid.add(rorQuarterLabel, 0, 10);
		grid.add(quartRor, 1, 10);

		grid.add(save, 5, 11);

		return new Scene(grid, 530, 225);
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
