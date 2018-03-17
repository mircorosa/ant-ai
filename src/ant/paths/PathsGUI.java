package ant.paths;

import ant.data.GamesData;
import ant.game.Coordinates;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PathsGUI extends GridPane {

	private BorderPane main;
	private GamesData data;
	private Button endAnimationButton;
	private int N, m;
	private Paths paths;

	private Stage stage;

	public PathsGUI(Paths paths) {
		this.paths=paths;
		this.data = paths.getGamesData();
		this.N = paths.getN();
		this.m = paths.getM();

		main = new BorderPane();
		setMinSize(300,300);
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		main.setCenter(this);
		BorderPane.setAlignment(this, Pos.CENTER);
		BorderPane.setMargin(this,new Insets(10));

		initializeBoard();
		updateUI();

		endAnimationButton = new Button(">>> Speed Up Animation >>>");
		endAnimationButton.setOnAction(event -> {
			paths.speedUpAnimations();
		});
		main.setBottom(endAnimationButton);
		BorderPane.setAlignment(endAnimationButton, Pos.CENTER);




		stage = new Stage();
		stage.setTitle("Moves Distribution");
		Scene scene = new Scene(main, 500, 530);
		stage.setScene(scene);
		stage.show();
	}

	private void initializeBoard() {
		int[][] board_data=paths.getBoard();
		Coordinates antPosition=paths.getAntPosition();
		for (int i = 0; i < board_data.length; i++) {
			for (int j = 0; j < board_data[i].length; j++) {
				VBox cell = new VBox();
				cell.setAlignment(Pos.CENTER);
				cell.setPrefSize(500/board_data[i].length,500/board_data.length);
				setCellStyle(board_data, antPosition, m, i, j, cell,false);

				String cellText;
				if (i<m || j<m || j>=board_data[0].length-m || i>=board_data.length-m)
					cellText = "";
				else if (i==antPosition.x() && j==antPosition.y())
					cellText = "A";
				else
					cellText = String.valueOf(-board_data[j][i]);
				Text value = new Text(cellText);
				setTextStyle(board_data, antPosition, m, i, j, value);
				cell.getChildren().add(value);
				add(cell,j,i);
			}
		}

		for (int i = 0; i < board_data[0].length; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth((double)100/(double)board_data[0].length);
			getColumnConstraints().add(columnConstraints);
		}
		for (int j = 0; j < board_data.length; j++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight((double)100/(double)board_data.length);
			getRowConstraints().add(rowConstraints);
		}
	}

	public void updateUI() {
		int[][] board_data=paths.getBoard();
		Coordinates antPosition=paths.getAntPosition();
		ObservableList<VBox> children = (ObservableList)getChildren();
		for(VBox cell : children) {
			Text value = (Text)cell.getChildren().get(0);
			int i = GridPane.getColumnIndex(cell);
			int j = GridPane.getRowIndex(cell);

			setCellStyle(board_data, antPosition, m, i, j, cell,true);

			if (i<m || j<m || j>=board_data[0].length-m || i>=board_data.length-m)
				value.setText("");
			else if (i==antPosition.x() && j==antPosition.y())
				value.setText("A");
			else
				value.setText(String.valueOf(-board_data[j][i]));

			setTextStyle(board_data, antPosition, m, i, j, value);

		}
	}

	private void setCellStyle(int[][] board_data, Coordinates antPosition, int m, int i, int j, VBox cell, boolean reversed) {
		//Reversed is due to columns-rows parsing of GridPane (instead of rows-columns in our game board) when updating
		if (i==antPosition.x() && j==antPosition.y() && !(i<m || j<m || j>=board_data[0].length-m || i>=board_data.length-m) )    cell.setStyle("-fx-border-color: red;-fx-background-color: orange;");
		else if ((i<m || j<m || j>=board_data[0].length-m || i>=board_data.length-m) && board_data[reversed?j:i][reversed?i:j]<1)    cell.setStyle("-fx-border-color: black;-fx-background-color: red;");
		else if ((i<m || j<m || j>=board_data[0].length-m || i>=board_data.length-m) && board_data[reversed?j:i][reversed?i:j]==1)  cell.setStyle("-fx-border-color: saddlebrown;-fx-background-color: darkslategrey;");
		else cell.setStyle("-fx-border-color: black;-fx-background-color: rgb("+
					(255+board_data[reversed?j:i][reversed?i:j]*(255/(board_data.length)))+","+
					(255+board_data[reversed?j:i][reversed?i:j]*(255/(board_data.length)))+","+
					(255+board_data[reversed?j:i][reversed?i:j]*(255/(board_data.length)))+");");
	}

	private void setTextStyle(int[][] board_data, Coordinates antPosition, int m, int i, int j, Text value) {
		if (i==antPosition.x() && j==antPosition.y()) value.setFill(Color.SADDLEBROWN);
		else if (i>=antPosition.x()-m && i<=antPosition.x()+m && j>=antPosition.y()-m && j<=antPosition.y()+m) value.setFill(Color.BLACK);
		else if(board_data[i][j]>=(-255/(2*board_data.length))) value.setFill(Color.BLACK);
		else value.setFill(Color.WHITE);
	}

}
