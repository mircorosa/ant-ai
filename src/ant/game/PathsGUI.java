package ant.game;

import ant.data.TestData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PathsGUI extends GridPane {

//	private BorderPane main;
//	private TestData data;
//	private Button endAnimationButton;
//
//	private Stage stage;
//
//	public PathsGUI(TestData data, int boardSize) {
//		this.data = data;
//
//		main = new BorderPane();
//		setMinSize(300,300);
//		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		main.setCenter(this);
//		BorderPane.setAlignment(this, Pos.CENTER);
//		BorderPane.setMargin(this,new Insets(10));
//
//		endAnimationButton = new Button("End Animation");
//		endAnimationButton.setOnAction(event -> {
//
//		});
//		main.setBottom(endAnimationButton);
//		BorderPane.setAlignment(endAnimationButton, Pos.CENTER);
//
//
//
//
//		stage = new Stage();
//		stage.setTitle("Moves Distribution");
//		Scene scene = new Scene(main, 670, 520);
//		stage.setScene(scene);
//		stage.show();
//	}
//
//	private void initializeBoard() {
//		int[][] board_data=game.getBoardData();
//		Coordinates antPosition=game.getAntPosition();
//		int m = game.getM();
//		for (int i = 0; i < board_data.length; i++) {
//			for (int j = 0; j < board_data[i].length; j++) {
//				VBox cell = new VBox();
//				cell.setAlignment(Pos.CENTER);
//				cell.setPrefSize(500/board_data[i].length,500/board_data.length);
//				setCellStyle(board_data, antPosition, m, i, j, cell,false,blindfolded.isSelected());
//
//				Text value = new Text(i==antPosition.x() && j==antPosition.y() ? "A" : String.valueOf(board_data[i][j]));
//				setTextStyle(board_data, antPosition, m, i, j, value,blindfolded.isSelected());
//				cell.getChildren().add(value);
//				add(cell,j,i);
//			}
//		}
//
//		for (int i = 0; i < board_data[0].length; i++) {
//			ColumnConstraints columnConstraints = new ColumnConstraints();
//			columnConstraints.setPercentWidth((double)100/(double)board_data[0].length);
//			getColumnConstraints().add(columnConstraints);
//		}
//		for (int j = 0; j < board_data.length; j++) {
//			RowConstraints rowConstraints = new RowConstraints();
//			rowConstraints.setPercentHeight((double)100/(double)board_data.length);
//			getRowConstraints().add(rowConstraints);
//		}
//	}
}
