package gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class BetRampWindow extends GUIWindow {

	private RampBucket betRamp;
	private RampBucket spotRamp;
	private TableView<RampBucket> betRampTable;
	private ObservableList<RampBucket> rampRows;
	
	private boolean[] validInputForCell;
	
	private int cellID;
	
	private BetRampWindow thisWindow;

	public BetRampWindow(GUIController controller, String windowTitle) {
		super(controller, windowTitle);
		validInputForCell = new boolean[14];
		thisWindow = this;
	}

	@Override
	protected Scene getScene() {
		
		int minCellWidth = 125;
		cellID = -1;
		
		for(int i = 0; i < validInputForCell.length; i++) {
			validInputForCell[i] = true;
		}

		betRampTable = new TableView<>();

		betRamp = new RampBucket("Bet");
		spotRamp = new RampBucket("Spots");

		if (!controller.getCurrentMinBet().isEmpty()) {
			betRamp.initDefaultBetValues(Integer.parseInt(controller.getCurrentMinBet()));
		} else {
			betRamp.initDefaultBetValues(0);
		}

		spotRamp.initDefaultSpotValues();

		rampRows = FXCollections.observableArrayList(betRamp, spotRamp);

		betRampTable.setEditable(true);
		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				cellID++;
				return new EditingCell(cellID, thisWindow);
			}
		};

		TableColumn row = new TableColumn(" ");
		row.setMinWidth(minCellWidth);
		row.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("row"));

		TableColumn tc0 = new TableColumn("True Count ≤ 0");
		tc0.setMinWidth(minCellWidth);
		tc0.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc0"));

		TableColumn tc1 = new TableColumn("True Count = 1");
		tc1.setMinWidth(minCellWidth);
		tc1.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc1"));

		TableColumn tc2 = new TableColumn("True Count = 2");
		tc2.setMinWidth(minCellWidth);
		tc2.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc2"));

		TableColumn tc3 = new TableColumn("True Count = 3");
		tc3.setMinWidth(minCellWidth);
		tc3.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc3"));

		TableColumn tc4 = new TableColumn("True Count = 4");
		tc4.setMinWidth(minCellWidth);
		tc4.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc4"));

		TableColumn tc5 = new TableColumn("True Count = 5");
		tc5.setMinWidth(minCellWidth);
		tc5.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc5"));

		TableColumn tc6 = new TableColumn("True Count ≥ 6");
		tc6.setMinWidth(minCellWidth);
		tc6.setCellValueFactory(new PropertyValueFactory<RampBucket, Integer>("tc6"));

		tc0.setCellFactory(cellFactory);
		tc0.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc0(t.getNewValue());
			}
		});

		tc1.setCellFactory(cellFactory);
		tc1.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc1(t.getNewValue());
			}
		});

		tc2.setCellFactory(cellFactory);
		tc2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc2(t.getNewValue());
			}
		});

		tc3.setCellFactory(cellFactory);
		tc3.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc3(t.getNewValue());
			}
		});

		tc4.setCellFactory(cellFactory);
		tc4.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc4(t.getNewValue());
			}
		});

		tc5.setCellFactory(cellFactory);
		tc5.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc5(t.getNewValue());
			}
		});

		tc6.setCellFactory(cellFactory);
		tc6.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RampBucket, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<RampBucket, Integer> t) {
				((RampBucket) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTc6(t.getNewValue());
			}
		});

		betRampTable.setItems(rampRows);
		betRampTable.getColumns().addAll(row, tc0, tc1, tc2, tc3, tc4, tc5, tc6);

		betRampTable.setFixedCellSize(25);
		betRampTable.prefHeightProperty()
				.bind(Bindings.size(betRampTable.getItems()).multiply(betRampTable.getFixedCellSize()).add(30));
		
		Button done = new Button();
		done.setText("Done");

		done.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String invalidInputMessage = invalidInputMessage();
				if(invalidInputMessage.isEmpty()) {
					saveInput();
					close();
				}else {
					controller.launchInvalidInputWindow(invalidInputMessage);
				}
			}

		});
		
		Text messageText = new Text("Hit ENTER/RETURN to commit value to cell.");
		messageText.setStyle("-fx-font: italic bold 12px 'sans-serif';");
		messageText.setFill(Paint.valueOf("white"));
		
		HBox messageBar = new HBox();
		messageBar.setStyle("-fx-background-color: #3f5368;");
		messageBar.setAlignment(Pos.CENTER);
		messageBar.setPadding(new Insets(2, 0, 2, 0));
		messageBar.getChildren().addAll(messageText);	
			
		HBox buttonBar = new HBox();
		buttonBar.setStyle("-fx-background-color: #3f5368;");
		buttonBar.setAlignment(Pos.CENTER);
		buttonBar.setPadding(new Insets(2, 0, 2, 0));
		buttonBar.getChildren().addAll(done);
		
		VBox vBox = new VBox();
		vBox.setStyle("-fx-background-color: #3f5368;");
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(0, 0, 2, 0));
		vBox.getChildren().addAll(messageBar, betRampTable, buttonBar);
		
		return new Scene(vBox);
	}

	@Override
	protected String invalidInputMessage() {
		String message = "";
		
		for(int i = 0; i < validInputForCell.length; i++) {
			if(!validInputForCell[i]) {
				message = "Invalid Input: Bet Ramp cell";
			}
		}
		
		return message;
	}

	@Override
	protected void saveInput() {
		settingsBuilder.setBetRamp(new int[] { betRamp.getTc0(), betRamp.getTc1(), betRamp.getTc2(), betRamp.getTc3(),
				betRamp.getTc4(), betRamp.getTc5(), betRamp.getTc6() });

		settingsBuilder.setSpotsRamp(new int[] { spotRamp.getTc0(), spotRamp.getTc1(), spotRamp.getTc2(),
				spotRamp.getTc3(), spotRamp.getTc4(), spotRamp.getTc5(), spotRamp.getTc6() });
	}
	
	public void signalCellInvalid(int cellID) {
		validInputForCell[cellID] = false;
	}

	public void signalCellValid(int cellID) {
		validInputForCell[cellID] = true;
	}

}
