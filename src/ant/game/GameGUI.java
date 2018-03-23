package ant.game;

import ant.SessionType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class GameGUI extends GridPane {

	private Game game;
	private BorderPane main;
	private VBox statusBar;
	private Text title;
	private Separator separator1;
	private Text movesRemaining;
	private Separator separator5;
	private Text score;
	private Separator separator2;
	private Text antStatus;
	private Separator separator3;
	private CheckBox blindfolded;
	private Separator separator4;
	private Button endGameButton;

	private Stage stage;

	public GameGUI(Game game) {
		this.game = game;

		main = new BorderPane();

		setMinSize(500,500);
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		main.setCenter(this);
		BorderPane.setAlignment(this,Pos.CENTER);
		BorderPane.setMargin(this,new Insets(10));

		statusBar = new VBox();
		statusBar.setMinWidth(150);
		statusBar.setPrefWidth(150);
		BorderPane.setAlignment(statusBar,Pos.CENTER);


		statusBar.setPadding(new Insets(5));
		statusBar.setSpacing(7);
		statusBar.setAlignment(Pos.CENTER);
		main.setRight(statusBar);

		title = new Text(game.getGameName());
		title.setFont(Font.font("Verdana",20));
		title.setTextAlignment(TextAlignment.CENTER);
		statusBar.getChildren().add(title);

		separator1=new Separator(Orientation.HORIZONTAL);
		statusBar.getChildren().add(separator1);

		movesRemaining = new Text("Moves\n"+game.getMoves()+"/"+game.getMaxMoves());
		movesRemaining.setTextAlignment(TextAlignment.CENTER);
		statusBar.getChildren().add(movesRemaining);

		separator5=new Separator(Orientation.HORIZONTAL);
		statusBar.getChildren().add(separator5);

		score = new Text("Score\n"+game.getScore());
		score.setTextAlignment(TextAlignment.CENTER);
		statusBar.getChildren().add(score);

		separator2=new Separator(Orientation.HORIZONTAL);
		statusBar.getChildren().add(separator2);

		antStatus = new Text("Status\n"+game.getAntStatus());
		antStatus.setTextAlignment(TextAlignment.CENTER);
		statusBar.getChildren().add(antStatus);

		separator3=new Separator(Orientation.HORIZONTAL);
		statusBar.getChildren().add(separator3);

		blindfolded=new CheckBox("Blindfolded");
		blindfolded.setSelected(true);
		blindfolded.selectedProperty().addListener((observable, oldValue, newValue) -> updateUI());
		statusBar.getChildren().add(blindfolded);

		separator4=new Separator(Orientation.HORIZONTAL);
		statusBar.getChildren().add(separator4);

		endGameButton = new Button("End Game");
		endGameButton.setVisible(false);
		endGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				game.finalizeGame();
				stage.close();
			}
		});
		statusBar.getChildren().add(endGameButton);

		initializeBoard();
		updateUI();

		stage = new Stage();
		stage.setTitle(game.getGameName());
		Scene scene = new Scene(main, 670, 520);
		if(game.getSessionType().equals(SessionType.TRAINING)) {
			scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
				if(!endGameButton.isVisible()) {
					String direction;
					switch (key.getCode()) {
						case UP:
							direction="N";
							break;
						case DOWN:
							direction="S";
							break;
						case RIGHT:
							direction="E";
							break;
						case LEFT:
							direction="W";
							break;
						default:
							direction="";
					}

//		   	System.out.println(key.getCode().getName()+" pressed ("+direction+")");
					game.move(direction);
				}
			});
		}

		stage.setScene(scene);
		stage.show();

//		game.notifyMainGui();
	}



	private void initializeBoard() {
		int[][] board_data=game.getBoardData();
		Coordinates antPosition=game.getAntPosition();
		int m = game.getM();
		for (int i = 0; i < board_data.length; i++) {
			for (int j = 0; j < board_data[i].length; j++) {
				VBox cell = new VBox();
				cell.setAlignment(Pos.CENTER);
				cell.setPrefSize(500/board_data[i].length,500/board_data.length);
				setCellStyle(board_data, antPosition, m, i, j, cell,false,blindfolded.isSelected());

				Text value = new Text(i==antPosition.x() && j==antPosition.y() ? "A" : String.valueOf(board_data[i][j]));
				setTextStyle(board_data, antPosition, m, i, j, value,blindfolded.isSelected());
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
		int[][] board_data=game.getBoardData();
		Coordinates antPosition=game.getAntPosition();
		int m = game.getM();
		ObservableList<VBox> children = (ObservableList)getChildren();
		for(VBox cell : children) {
			Text value = (Text)cell.getChildren().get(0);
			int i = GridPane.getColumnIndex(cell);
			int j = GridPane.getRowIndex(cell);

			setCellStyle(board_data, antPosition, m, i, j, cell,true,blindfolded.isSelected());

			value.setText(i==antPosition.x() && j==antPosition.y() ? "A" : String.valueOf(board_data[j][i]));
			setTextStyle(board_data, antPosition, m, i, j, value,blindfolded.isSelected());

		}

		//Scoreboard update
		movesRemaining.setText("Moves\n"+game.getMoves()+"/"+game.getMaxMoves());
		antStatus.setText("Status\n"+game.getAntStatus());
		score.setText("Score\n"+game.getScore());

	}

	private void setCellStyle(int[][] board_data, Coordinates antPosition, int m, int i, int j, VBox cell, boolean reversed, boolean blindfolded) {
		//Reversed is due to columns-rows parsing of GridPane (instead of rows-columns in our game board) when updating
		if(blindfolded && !(i>=antPosition.x()-m && i<=antPosition.x()+m && j>=antPosition.y()-m && j<=antPosition.y()+m)) cell.setStyle("-fx-border-color: gray;-fx-background-color: gray;");
		else if (i==antPosition.x() && j==antPosition.y())    cell.setStyle("-fx-border-color: red;-fx-background-color: orange;");
		else if (board_data[reversed?j:i][reversed?i:j]>0)   cell.setStyle("-fx-border-color: black;-fx-background-color: red;");
		else if (i<m || j<m || j>=board_data[0].length-m || i>=board_data.length-m)  cell.setStyle("-fx-border-color: saddlebrown;-fx-background-color: darkslategrey;");
		else if (i>=antPosition.x()-m && i<=antPosition.x()+m && j>=antPosition.y()-m && j<=antPosition.y()+m && board_data[reversed?j:i][reversed?i:j]<=0)	cell.setStyle("-fx-border-color: red;-fx-background-color: yellow;");
		else cell.setStyle("-fx-border-color: black;-fx-background-color: rgb("+
																	(255+board_data[reversed?j:i][reversed?i:j]*(255/(board_data.length)))+","+
																	(255+board_data[reversed?j:i][reversed?i:j]*(255/(board_data.length)))+","+
																	(255+board_data[reversed?j:i][reversed?i:j]*(255/(board_data.length)))+");");
	}

	private void setTextStyle(int[][] board_data, Coordinates antPosition, int m, int i, int j, Text value, boolean blindfolded) {
		if(blindfolded && !(i>=antPosition.x()-m && i<=antPosition.x()+m && j>=antPosition.y()-m && j<=antPosition.y()+m)) value.setFill(Color.GRAY);
		else if (i==antPosition.x() && j==antPosition.y()) value.setFill(Color.SADDLEBROWN);
		else if (i>=antPosition.x()-m && i<=antPosition.x()+m && j>=antPosition.y()-m && j<=antPosition.y()+m) value.setFill(Color.BLACK);
		else if(board_data[i][j]>=(-255/(2*board_data.length))) value.setFill(Color.BLACK);
		else value.setFill(Color.WHITE);
	}

	public void endGame() {
		endGameButton.setVisible(true);
		blindfolded.setSelected(false);
	}
}
