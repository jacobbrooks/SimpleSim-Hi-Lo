package gui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class ProgressBarWindow extends GUIWindow {

	private ProgressBar progress;

	public ProgressBarWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
	}

	@Override
	protected Scene getScene() {
		Label progressLabel = new Label("Simulation Progress: ");
		progressLabel.setTextFill(Paint.valueOf("WHITE"));

		progress = new ProgressBar(0);

		HBox hb = new HBox();
		hb.setSpacing(5);
		hb.setPadding(new Insets(0, 0, 10, 0));
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(progressLabel, progress);

		Label percentLabel = new Label();
		DoubleProperty rawDoubleValue = progress.progressProperty();
		DoubleBinding doubleAsPercentage = rawDoubleValue.multiply(100);
		StringBinding sb = doubleAsPercentage.asString("%.0f");

		percentLabel.textProperty().bind(sb);
		percentLabel.setTextFill(Paint.valueOf("WHITE"));

		Label percentSign = new Label();
		percentSign.setText("%");
		percentSign.setTextFill(Paint.valueOf("WHITE"));
		
		Button cancel = new Button("Cancel");
		cancel.setAlignment(Pos.BOTTOM_CENTER);
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.cancelSimulation();
				close();
			}
		});

		hb.getChildren().add(percentLabel);
		hb.getChildren().add(percentSign);
		
		VBox vb = new VBox();
		vb.setSpacing(10);
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().add(hb);
		vb.getChildren().add(cancel);

		StackPane progressPane = new StackPane();
		progressPane.getChildren().add(vb);
		progressPane.setStyle("-fx-background-color: #3f5368;");

		progress.setProgress(0);

		return new Scene(progressPane, 300, 100);
	}

	@Override
	protected String invalidInputMessage() {
		return null;
	}

	public void initBinding() {
		progress.progressProperty().unbind();
		progress.progressProperty().bind(controller.getSimWorker().progressProperty());
	}

	@Override
	protected void saveInput() {
		// TODO Auto-generated method stub
	}

}
