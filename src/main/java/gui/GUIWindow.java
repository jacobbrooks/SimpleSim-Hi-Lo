package gui;

import core.SimSettings.SettingsBuilder;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class GUIWindow {

	protected GUIController controller;
	protected Stage stage;
	protected SettingsBuilder settingsBuilder;
	
	private String windowTitle;
	
	public GUIWindow(GUIController controller, String windowTitle) {
		this.controller = controller;
		this.windowTitle = windowTitle;
		settingsBuilder = controller.getSettingsBuilder();
	}

	protected abstract Scene getScene();

	protected abstract String invalidInputMessage();

	protected abstract void saveInput();
	
	protected String fieldVal(TextField field) {
		if(field != null) {
			if(field.getText() != null) {
				if(!field.getText().isEmpty()) {
					return field.getText();
				}
			}
		}
		return " ";
	}
	
	protected String comboBoxVal(ComboBox box) {
		if(box != null) {
			if(box.getValue() != null) {
				if(!box.getValue().toString().isEmpty()) {;
					return box.getValue().toString();
				}
			}
		}
		return " ";
	}

	public void open(boolean rebuild) {	
		boolean reinit = false;
		if(stage == null) {
			reinit = true;
		}else if(rebuild && !stage.isShowing()) {
			reinit = true;
		}
		if(reinit) {
			Scene scene = getScene();
			stage = new Stage();
			stage.setTitle(windowTitle);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(false);
			stage.setScene(scene);
		}
		stage.show();
	}

	public void close() {
		stage.hide();
	}
	
}
