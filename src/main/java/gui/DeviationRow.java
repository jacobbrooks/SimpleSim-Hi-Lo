package gui;

import core.SimEngine;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class DeviationRow {

	private GUIController controller;

	private Text handType;
	private Text playerHand;
	private Text upCard;
	private Text index;
	private Text aboveIndex;
	private Text belowIndex;
	private CheckBox on;
	
	private int i, j, k;

	public DeviationRow(GUIController controller, int i, int j, int k) {
		this.controller = controller;

		handType = new Text();
		playerHand = new Text();
		upCard = new Text();
		index = new Text();
		aboveIndex = new Text();
		belowIndex = new Text();
		on = new CheckBox();
		
		buildRow();
	}
	
	private void buildRow() {
		handType.setFill(Paint.valueOf("white"));
		handType.setStyle("-fx-font: normal bold 11px 'sans-serif';");

		playerHand.setFill(Paint.valueOf("white"));
		playerHand.setStyle("-fx-font: normal bold 11px 'sans-serif';");

		upCard.setFill(Paint.valueOf("white"));
		upCard.setStyle("-fx-font: normal bold 11px 'sans-serif';");

		index.setFill(Paint.valueOf("white"));
		index.setStyle("-fx-font: normal bold 11px 'sans-serif';");

		aboveIndex.setFill(Paint.valueOf("white"));
		aboveIndex.setStyle("-fx-font: normal bold 11px 'sans-serif';");

		belowIndex.setFill(Paint.valueOf("white"));
		belowIndex.setStyle("-fx-font: normal bold 11px 'sans-serif';");

		on.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (!handType.getText().equals("insurance")) {
					if (!on.isSelected()) {
						if (aboveIndex.getText().equals("surrender")) {
							controller.setOffDeviationAt(i, j, k, true);
						} else {
							controller.setOffDeviationAt(i, j, k, false);
						}
					} else {
						if (aboveIndex.getText().equals("surrender")) {
							controller.setOnDeviationAt(i, j, k, true);
						} else {
							controller.setOnDeviationAt(i, j, k, false);
						}
					}
				} else {
					if (!on.isSelected()) {
						controller.setInsuranceDeviationOff();
					} else {
						controller.setInsuranceDeviationOn();
					}
				}

			}

		});
	}

	public Text getHandType() {
		return handType;
	}

	public void setHandType(String handType) {
		this.handType.setText(handType);
	}

	public Text getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(String playerHand) {
		this.playerHand.setText(playerHand);
	}

	public Text getUpCard() {
		return upCard;
	}

	public void setUpCard(String upCard) {
		this.upCard.setText(upCard);
	}

	public Text getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index.setText(index);
	}

	public Text getAboveIndex() {
		return aboveIndex;
	}

	public void setAboveIndex(String aboveIndex) {
		this.aboveIndex.setText(aboveIndex);
	}

	public Text getBelowIndex() {
		return belowIndex;
	}

	public void setBelowIndex(String belowIndex) {
		this.belowIndex.setText(belowIndex);
	}

	public CheckBox getCheckBoxOn() {
		return on;
	}

	public void setCheckBoxState(boolean state) {
		on.setSelected(state);
	}

}
