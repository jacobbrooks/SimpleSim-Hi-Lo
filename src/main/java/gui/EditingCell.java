package gui;

import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditingCell extends TableCell<RampBucket, Integer> {

	private TextField textField;
	private BetRampWindow betRampWindow;
	private int cellID;

	public EditingCell(int cellID, BetRampWindow betRampWindow) {
		this.cellID = cellID; 
		this.betRampWindow = betRampWindow;
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null) {
			createTextField();
		}

		setGraphic(textField);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(String.valueOf(getItem()));
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@Override
	public void updateItem(Integer item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setGraphic(textField);
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			} else {
				setText(getString());
				setContentDisplay(ContentDisplay.TEXT_ONLY);
			}
		}
	}

	private void createTextField() {
		textField = new TextField(getString());
		textField.setMinWidth(100);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					String text = textField.getText();
					if (text.matches("^[1-9]\\d*$")) {
						commitEdit(Integer.parseInt(textField.getText()));
						betRampWindow.signalCellValid(cellID);
					}else {
						betRampWindow.signalCellInvalid(cellID);
					}
				} else if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}

		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
}
