package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InvalidInputWindow extends GUIWindow {

	private String message;

	public InvalidInputWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
	}

	@Override
	protected Scene getScene() {
		VBox vBox = new VBox();
		vBox.setStyle("-fx-background-color: #3f5368;");
		vBox.setPrefSize(300, 100);
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.setAlignment(vBox.getAlignment().CENTER);

		Text msgLabel = new Text(message);
		msgLabel.setFill(Paint.valueOf("white"));
		msgLabel.setStyle("-fx-font: normal bold 15px 'sans-serif';");

		Button okay = new Button("Okay");
		okay.setTranslateY(10);
		okay.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				close();
			}

		});

		vBox.getChildren().addAll(msgLabel, okay);

		return new Scene(vBox, 450, 100);
	}

	public void setMessage(String message) {
		this.message = message;
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
