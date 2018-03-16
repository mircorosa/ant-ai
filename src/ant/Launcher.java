package ant;

import ant.data.AntDataSet;
import ant.data.CompleteLog;
import ant.data.TestData;
import ant.game.Game;
import ant.game.PathsGUI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class Launcher extends Application {

	private Stage primaryStage;

	private Scene mainMenuScene;
	private BorderPane main;
	private Text title;
	private VBox mainMenuPane;
	private Text boardSizeText;
	private Slider sizeSlider;
	private Separator separator1;
	private Text antRangeText;
	private Slider rangeSlider ;
	private Separator separator2 ;
	private Text gameNumberText;
	private Slider gameNumberSlider;
	private Separator separator3;
	private Button trainingButton;

	private Separator separator4;
	private Button treeTestButton;

	private Scene trainingScene;
	private BorderPane training;
	private GridPane trainingData;
	private Text gamesProgr;      //Large on top
	private Text totalMoves;      //1st Row
	private Text totalScore;
	private Text scoreMoveRatio;
	private Text deaths;          //2nd Row
	private Text survivals;
	private Text survivalsDeathsRatio;
	private Button pathsButton;   //Last full row TODO Implement functionality


	private ProgressBar trainingProgress;

	private TestData testData;

	private static final String PATH = "/home/mirco/Desktop/";
	private static final String FILE_NAME = "AntTrain";
	private static final String DATA_SET_NAME = "AntTrain";

	public Launcher() {
		main = new BorderPane();
		title = new Text("The Ant Game");
		mainMenuPane = new VBox();
		boardSizeText = new Text("Board Size");
		sizeSlider = new Slider();
		separator1 = new Separator();
		antRangeText = new Text("Ant Range");
		rangeSlider = new Slider();
		separator2 = new Separator();
		gameNumberText = new Text("Game Number");
		gameNumberSlider = new Slider();
		separator3 = new Separator();
		trainingButton = new Button("Start Training");
		separator4 = new Separator();
		treeTestButton = new Button("Start Tree Test");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeGui();

		this.primaryStage=primaryStage;

		primaryStage.setTitle("Ant Game");
		mainMenuScene=new Scene(main,300,400);
		primaryStage.setScene(mainMenuScene);
		primaryStage.show();
	}

	void initializeGui() {
		title.setFont(Font.font("Verdana",24));
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.FIREBRICK);
		main.setTop(title);
		BorderPane.setAlignment(title, Pos.TOP_CENTER);
		BorderPane.setMargin(title,new Insets(15));


		mainMenuPane.setPadding(new Insets(10));
		mainMenuPane.setSpacing(7);
		mainMenuPane.setAlignment(Pos.CENTER);

		boardSizeText.setTextAlignment(TextAlignment.CENTER);
		mainMenuPane.getChildren().add(boardSizeText);

		sizeSlider.setMin(10);
		sizeSlider.setMax(50);
		sizeSlider.setValue(10);
		sizeSlider.setShowTickLabels(true);
		sizeSlider.setShowTickMarks(true);
		sizeSlider.valueProperty().addListener((obs,val,newVal)->sizeSlider.setValue(Math.round(newVal.doubleValue())));
		sizeSlider.setMinorTickCount(1);
		sizeSlider.setMajorTickUnit(10);
		sizeSlider.setBlockIncrement(1);
		sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> boardSizeText.setText("Board Size: "+newValue.intValue()));
		mainMenuPane.getChildren().add(sizeSlider);

		separator1.setOrientation(Orientation.HORIZONTAL);
		mainMenuPane.getChildren().add(separator1);

		antRangeText.setTextAlignment(TextAlignment.CENTER);
		mainMenuPane.getChildren().add(antRangeText);

		rangeSlider.setMin(1);
		rangeSlider.setMax(5);
		rangeSlider.setValue(1);
		rangeSlider.setShowTickLabels(true);
		rangeSlider.setShowTickMarks(true);
		rangeSlider.valueProperty().addListener((obs,val,newVal)->rangeSlider.setValue(Math.round(newVal.doubleValue())));
		rangeSlider.setMinorTickCount(0);
		rangeSlider.setMajorTickUnit(1);
		rangeSlider.setBlockIncrement(1);
		rangeSlider.valueProperty().addListener((observable, oldValue, newValue) -> antRangeText.setText("Ant Range: "+newValue.intValue()));
		mainMenuPane.getChildren().add(rangeSlider);

		separator2.setOrientation(Orientation.HORIZONTAL);
		mainMenuPane.getChildren().add(separator2);

		gameNumberText.setTextAlignment(TextAlignment.CENTER);
		mainMenuPane.getChildren().add(gameNumberText);

		gameNumberSlider.setMin(1);
		gameNumberSlider.setMax(15);
		gameNumberSlider.setValue(3);
		gameNumberSlider.setShowTickLabels(true);
		gameNumberSlider.setShowTickMarks(true);
		gameNumberSlider.valueProperty().addListener((obs,val,newVal)->gameNumberSlider.setValue(Math.round(newVal.doubleValue())));
		gameNumberSlider.setMinorTickCount(2);
		gameNumberSlider.setMajorTickUnit(3);
		gameNumberSlider.setBlockIncrement(1);
		gameNumberSlider.valueProperty().addListener((observable, oldValue, newValue) -> gameNumberText.setText("Game Number: "+newValue.intValue()));
		mainMenuPane.getChildren().add(gameNumberSlider);

		separator3.setOrientation(Orientation.HORIZONTAL);
		mainMenuPane.getChildren().add(separator3);

		trainingButton.setAlignment(Pos.CENTER);
		trainingButton.setMaxSize(Double.MAX_VALUE,100);
		trainingButton.setOnAction(new GameEventHandler() {
			int gameCount = 1;
			int boardSize, antRange,gamesNumber;

			@Override
			public void handle(ActionEvent event) {
				//Fires 1st game
				boardSize=(int)sizeSlider.getValue();
				antRange=(int)rangeSlider.getValue();
				gamesNumber=(int)gameNumberSlider.getValue();
				testData = new TestData(new CompleteLog(),new AntDataSet(DATA_SET_NAME,antRange),PATH,FILE_NAME);
				setupTrainingGUI();
				updateTrainingProgress(0,gamesNumber,0,0,0,0);
				new Game("Game "+gameCount, this, boardSize,antRange,testData);
			}

			@Override
			public void pushGameData(TestData partialTestData) {
				partialTestData.generateArff();
				updateTrainingProgress(gameCount,gamesNumber,partialTestData.countMoves(),partialTestData.countScore(),partialTestData.countDeaths(),partialTestData.countSurvivals());
				gameCount++;
				if(gameCount<=gamesNumber) {
					testData=partialTestData;
					new Game("Game "+gameCount, this, boardSize,antRange,partialTestData);
				}
			}
		});
		mainMenuPane.getChildren().add(trainingButton);

		separator4.setOrientation(Orientation.HORIZONTAL);
		separator4.setMinHeight(15);
		mainMenuPane.getChildren().add(separator4);

		treeTestButton.setAlignment(Pos.CENTER);
		treeTestButton.setMaxSize(Double.MAX_VALUE,100);


		mainMenuPane.getChildren().add(treeTestButton);


		main.setCenter(mainMenuPane);
	}


	public void setupTrainingGUI() {
		training = new BorderPane();

		trainingData = new GridPane();
		trainingData.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		//VBoxes containers for aesthetic
		VBox[] vBoxes = new VBox[7];
		for (int i = 0; i < 7; i++) {
			vBoxes[i] = new VBox();
			vBoxes[i].setPadding(new Insets(5));
			vBoxes[i].setAlignment(Pos.CENTER);
			vBoxes[i].maxWidth(Double.MAX_VALUE);
		}

		gamesProgr=new Text();
		gamesProgr.setTextAlignment(TextAlignment.CENTER);
		gamesProgr.setFont(Font.font("Verdana",18));
		vBoxes[0].getChildren().add(gamesProgr);
		trainingData.add(vBoxes[0],0,0,3,1);
		totalMoves=new Text();
		totalMoves.setTextAlignment(TextAlignment.CENTER);
		vBoxes[1].getChildren().add(totalMoves);
		trainingData.add(vBoxes[1],0,1);
		totalScore=new Text();
		totalScore.setTextAlignment(TextAlignment.CENTER);
		vBoxes[2].getChildren().add(totalScore);
		trainingData.add(vBoxes[2],1,1);
		scoreMoveRatio=new Text();
		scoreMoveRatio.setTextAlignment(TextAlignment.CENTER);
		vBoxes[3].getChildren().add(scoreMoveRatio);
		trainingData.add(vBoxes[3],2,1);
		deaths=new Text();
		deaths.setTextAlignment(TextAlignment.CENTER);
		vBoxes[4].getChildren().add(deaths);
		trainingData.add(vBoxes[4],0,2);
		survivals=new Text();
		survivals.setTextAlignment(TextAlignment.CENTER);
		vBoxes[5].getChildren().add(survivals);
		trainingData.add(vBoxes[5],1,2);
		survivalsDeathsRatio =new Text();
		survivalsDeathsRatio.setTextAlignment(TextAlignment.CENTER);
		vBoxes[6].getChildren().add(survivalsDeathsRatio);
		trainingData.add(vBoxes[6],2,2);
		pathsButton=new Button("Show Paths Map");
		pathsButton.setMaxSize(Double.MAX_VALUE,100);
		pathsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//				new PathsGUI(new TestData());

			}
		});
		//TODO Add behaviour
		trainingData.add(pathsButton,0,3,3,1);

		for (int i = 0; i < 3; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth((double)100/(double)3);
			trainingData.getColumnConstraints().add(columnConstraints);
		}

		for (int j = 0; j < 3; j++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight((double)100/(double)4);
			trainingData.getRowConstraints().add(rowConstraints);
		}

		training.setTop(trainingData);
		BorderPane.setAlignment(trainingData, Pos.TOP_CENTER);
		BorderPane.setMargin(trainingData,new Insets(10));

		trainingProgress = new ProgressBar(0.0);
		trainingProgress.setMaxWidth(Double.MAX_VALUE);
		training.setBottom(trainingProgress);
		BorderPane.setAlignment(trainingProgress, Pos.TOP_CENTER);
		BorderPane.setMargin(trainingProgress,new Insets(10));

		trainingScene=new Scene(training,500,250);
		primaryStage.setTitle("Manual Training");
		primaryStage.setScene(trainingScene);
	}

	private void updateTrainingProgress(int gameCount,int gamesNumber,int moves,int score,int deathsNumber,int survivalsNumber) {

		DecimalFormat df =new DecimalFormat("#.##");
		gamesProgr.setText("Game "+gameCount+"/"+gamesNumber);
		totalMoves.setText("Moves\n"+moves);
		totalScore.setText("Total Score\n"+score);
		scoreMoveRatio.setText("Score per Move\n"+df.format((moves==0?0:((double)score/(double)moves))));
		deaths.setText("Deaths\n"+deathsNumber);
		survivals.setText("Survivals\n"+survivalsNumber);
		survivalsDeathsRatio.setText("Survs/Deaths Ratio\n"+df.format((deathsNumber==0?0:((double)survivalsNumber/(double)deathsNumber))));
		trainingProgress.setProgress((double)gameCount/(double)gamesNumber);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
