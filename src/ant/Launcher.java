package ant;

import ant.data.AntDataSet;
import ant.data.BoardsHistory;
import ant.data.FullLog;
import ant.data.GamesData;
import ant.game.Game;
import ant.paths.Paths;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

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
	private Button mlPerceptronTestButton;

	private Scene gamesScene;
	private BorderPane gamesPane;
	private GridPane gamesDataGrid;
	private Text gamesProgr;      //Large on top
	private Text totalMoves;      //1st Row
	private Text totalScore;
	private Text scoreMoveRatio;
	private Text deaths;          //2nd Row
	private Text survivals;
	private Text survivalsDeathsRatio;
	private Button pathsButton;   //Last full row
	private Separator separator5;
	private Button backToMainMenuButton;


	private ProgressBar gamesProgress;

	private GamesData trainingData, testData;

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
		treeTestButton = new Button("Start Decision Tree Test");
		mlPerceptronTestButton = new Button("Start Multilayer Perceptron Test");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage=primaryStage;
		setupMainMenuGUI();
		showMainMenu();

		primaryStage.show();
	}

	private void setupMainMenuGUI() {
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
			int boardSize, antRange, gamesNumber;

			@Override
			public void handle(ActionEvent event) {
				//Fires 1st game
				gameCount = 1;
				boardSize=(int)sizeSlider.getValue();
				antRange=(int)rangeSlider.getValue();
				gamesNumber=(int)gameNumberSlider.getValue();
				trainingData = new GamesData(new FullLog(), new BoardsHistory(),new AntDataSet(DATA_SET_NAME,antRange),PATH,FILE_NAME);
				if(gamesPane==null)
					setupGamesGUI();
				updateGamesProgress(0,gamesNumber,0,0,0,0);
				showGamesGUI(true);
				new Game("Game "+gameCount, this, boardSize,antRange, trainingData,SessionType.TRAINING,null);
			}

			@Override
			public void pushGameData(GamesData partialTrainingData) {
				partialTrainingData.generateArff();
				updateGamesProgress(gameCount,gamesNumber, partialTrainingData.countMoves(), partialTrainingData.countScore(), partialTrainingData.countDeaths(), partialTrainingData.countSurvivals());
				gameCount++;
				if(gameCount<=gamesNumber) {
					trainingData = partialTrainingData;
					new Game("Game "+gameCount, this, boardSize,antRange, partialTrainingData,SessionType.TRAINING,null);
				}
			}
		});
		mainMenuPane.getChildren().add(trainingButton);

		separator4.setOrientation(Orientation.HORIZONTAL);
		separator4.setMinHeight(15);
		mainMenuPane.getChildren().add(separator4);

		treeTestButton.setAlignment(Pos.CENTER);
		treeTestButton.setMaxSize(Double.MAX_VALUE,100);
		treeTestButton.setOnAction(new GameEventHandler() {
			int gameCount;
			int boardSize, antRange, gamesNumber;
			AbstractClassifier classifier;


			@Override
			public void handle(ActionEvent event) {
				//Fires 1st game
				gameCount = 1;
				boardSize=(int)sizeSlider.getValue();
				antRange=(int)rangeSlider.getValue();
				gamesNumber=(int)gameNumberSlider.getValue();
				try {
					classifier = getTrainedDecisionTree();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(testData==null)
					testData = new GamesData(new FullLog(),new BoardsHistory(),new AntDataSet(DATA_SET_NAME,antRange),PATH,FILE_NAME);
				testData.cleanDataForNewTest();
				if(gamesPane==null)
					setupGamesGUI();
				updateGamesProgress(0,gamesNumber,0,0,0,0);
				showGamesGUI(false);
				new Game("Game "+gameCount, this, boardSize,antRange, testData,SessionType.DECISION_TREE,classifier);
			}

			@Override
			public void pushGameData(GamesData partialTestData) {
//				partialTestData.generateArff();
				updateGamesProgress(gameCount,gamesNumber, partialTestData.countMoves(), partialTestData.countScore(), partialTestData.countDeaths(), partialTestData.countSurvivals());
				gameCount++;
				if(gameCount<=gamesNumber) {
					testData = partialTestData;
					new Game("Game "+gameCount, this, boardSize,antRange, testData,SessionType.DECISION_TREE,classifier);
				}
			}
		});
		mainMenuPane.getChildren().add(treeTestButton);

		mlPerceptronTestButton.setAlignment(Pos.CENTER);
		mlPerceptronTestButton.setMaxSize(Double.MAX_VALUE,100);
		mlPerceptronTestButton.setOnAction(new GameEventHandler() {
			int gameCount;
			int boardSize, antRange, gamesNumber;
			AbstractClassifier classifier;

			@Override
			public void handle(ActionEvent event) {
				//Fires 1st game
				gameCount = 1;
				boardSize=(int)sizeSlider.getValue();
				antRange=(int)rangeSlider.getValue();
				gamesNumber=(int)gameNumberSlider.getValue();
				try {
					classifier = getTrainedMultilayerPerceptron();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(testData==null)
					testData = new GamesData(new FullLog(),new BoardsHistory(),new AntDataSet(DATA_SET_NAME,antRange),PATH,FILE_NAME);
				testData.cleanDataForNewTest();
				if(gamesPane==null)
					setupGamesGUI();
				updateGamesProgress(0,gamesNumber,0,0,0,0);
				showGamesGUI(false);
				new Game("Game "+gameCount, this, boardSize,antRange, testData,SessionType.NEURAL_NETWORK,classifier);
			}

			@Override
			public void pushGameData(GamesData partialTestData) {
//				partialTestData.generateArff();
				updateGamesProgress(gameCount,gamesNumber, partialTestData.countMoves(), partialTestData.countScore(), partialTestData.countDeaths(), partialTestData.countSurvivals());
				gameCount++;
				if(gameCount<=gamesNumber) {
					testData = partialTestData;
					new Game("Game "+gameCount, this, boardSize,antRange, testData,SessionType.NEURAL_NETWORK,classifier);
				}
			}
		});
		mainMenuPane.getChildren().add(mlPerceptronTestButton);

		main.setCenter(mainMenuPane);
		mainMenuScene=new Scene(main,300,430);
	}

	public void showMainMenu() {
		primaryStage.setTitle("Ant Game");
		primaryStage.setScene(mainMenuScene);
	}


	public void setupGamesGUI() {
		gamesPane = new BorderPane();

		gamesDataGrid = new GridPane();
		gamesDataGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

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
		gamesDataGrid.add(vBoxes[0],0,0,3,1);
		totalMoves=new Text();
		totalMoves.setTextAlignment(TextAlignment.CENTER);
		vBoxes[1].getChildren().add(totalMoves);
		gamesDataGrid.add(vBoxes[1],0,1);
		totalScore=new Text();
		totalScore.setTextAlignment(TextAlignment.CENTER);
		vBoxes[2].getChildren().add(totalScore);
		gamesDataGrid.add(vBoxes[2],1,1);
		scoreMoveRatio=new Text();
		scoreMoveRatio.setTextAlignment(TextAlignment.CENTER);
		vBoxes[3].getChildren().add(scoreMoveRatio);
		gamesDataGrid.add(vBoxes[3],2,1);
		deaths=new Text();
		deaths.setTextAlignment(TextAlignment.CENTER);
		vBoxes[4].getChildren().add(deaths);
		gamesDataGrid.add(vBoxes[4],0,2);
		survivals=new Text();
		survivals.setTextAlignment(TextAlignment.CENTER);
		vBoxes[5].getChildren().add(survivals);
		gamesDataGrid.add(vBoxes[5],1,2);
		survivalsDeathsRatio =new Text();
		survivalsDeathsRatio.setTextAlignment(TextAlignment.CENTER);
		vBoxes[6].getChildren().add(survivalsDeathsRatio);
		gamesDataGrid.add(vBoxes[6],2,2);
		pathsButton=new Button("Show Paths Map");
		pathsButton.setMaxSize(Double.MAX_VALUE,100);
		gamesDataGrid.add(pathsButton,0,3,3,1);
		separator5 = new Separator(Orientation.HORIZONTAL);
		separator5.setValignment(VPos.CENTER);
		separator5.setMinHeight(10);
		gamesDataGrid.add(separator5,0,4,3,1);


		backToMainMenuButton = new Button("Main Menu");
		backToMainMenuButton.setMaxSize(Double.MAX_VALUE,100);
		backToMainMenuButton.setOnAction(event ->
				showMainMenu()
				 );
		gamesDataGrid.add(backToMainMenuButton,0,5,3,1);

		for (int i = 0; i < 3; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth((double)100/(double)3);
			gamesDataGrid.getColumnConstraints().add(columnConstraints);
		}

		for (int j = 0; j < 3; j++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight((double)100/(double)4);
			gamesDataGrid.getRowConstraints().add(rowConstraints);
		}

		gamesPane.setTop(gamesDataGrid);
		BorderPane.setAlignment(gamesDataGrid, Pos.TOP_CENTER);
		BorderPane.setMargin(gamesDataGrid,new Insets(10));

		gamesProgress = new ProgressBar(0.0);
		gamesProgress.setMaxWidth(Double.MAX_VALUE);
		gamesPane.setBottom(gamesProgress);
		BorderPane.setAlignment(gamesProgress, Pos.TOP_CENTER);
		BorderPane.setMargin(gamesProgress,new Insets(10));
		gamesScene =new Scene(gamesPane,500,300);
	}

	public void showGamesGUI(boolean training) {
		pathsButton.setOnAction(event -> new Paths(training ? trainingData : testData,(int)sizeSlider.getValue(),(int)rangeSlider.getValue()));
		primaryStage.setTitle("Games Stats");
		primaryStage.setScene(gamesScene);
	}

	private J48 getTrainedDecisionTree() throws Exception{
		//Loading .arff
		ConverterUtils.DataSource source = new ConverterUtils.DataSource("/home/mirco/Desktop/AntTrain.arff");
		Instances data = source.getDataSet();
		// setting class attribute if the data format does not provide this information
		// For example, the XRFF format saves the class attribute information as well
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);


		String[] options = weka.core.Utils.splitOptions("-C 0.25 -M 2");
		J48 tree = new J48();         // new instance of tree
		tree.setOptions(options);     // set the options
		tree.buildClassifier(data);   // build classifier

		return tree;
	}

	private MultilayerPerceptron getTrainedMultilayerPerceptron() throws Exception {
		//Loading .arff
		ConverterUtils.DataSource source = new ConverterUtils.DataSource("/home/mirco/Desktop/AntTrain.arff");
		Instances data = source.getDataSet();
		// setting class attribute if the data format does not provide this information
		// For example, the XRFF format saves the class attribute information as well
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		String[] options = weka.core.Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
		MultilayerPerceptron mlPerceptron = new MultilayerPerceptron();         // new instance of mlPerceptron
		mlPerceptron.setOptions(options);     // set the options
		mlPerceptron.buildClassifier(data);   // build classifier

		return mlPerceptron;
	}



	private void updateGamesProgress(int gameCount, int gamesNumber, int moves, int score, int deathsNumber, int survivalsNumber) {

		DecimalFormat df =new DecimalFormat("#.##");
		gamesProgr.setText("Game "+gameCount+"/"+gamesNumber);
		totalMoves.setText("Moves\n"+moves);
		totalScore.setText("Total Score\n"+score);
		scoreMoveRatio.setText("Score per Move\n"+df.format((moves==0?0:((double)score/(double)moves))));
		deaths.setText("Deaths\n"+deathsNumber);
		survivals.setText("Survivals\n"+survivalsNumber);
		survivalsDeathsRatio.setText("Survs/Deaths Ratio\n"+df.format((deathsNumber==0?0:((double)survivalsNumber/(double)deathsNumber))));
		gamesProgress.setProgress((double)gameCount/(double)gamesNumber);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
