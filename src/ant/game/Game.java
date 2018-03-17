package ant.game;

import ant.GameEventHandler;
import ant.SessionType;
import ant.data.AntDataSet;
import ant.data.GameLog;
import ant.data.GamesData;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

	private GameBoard gameBoard;
	private Ant ant;
	private GameGUI GUI;

	private String name;
	private int N, m;
	private int defaultValue, foodValue, outOfBoundValue, totalFood;
	private int startingX, startingY, foodScore, maxMoves, passingDrop, moves;
	private GameLog gameLog;
	private SessionType sessionType;

	private Timer testTimer;

	private GamesData gamesData;

	private GameEventHandler eventHandler;

	/*
	 ## Default ant.GameGame Board
	 Size(NxN): 10x10
	 ant.game.Ant.Ant Range (m): 2
	 Food units: N
	 Empty cells value: 0
	 Food cells value: +1
	 Outside cells value: -(N-2)

	 ## Default ant.gameAnt
	 For each step cell value is decreased by 1
	 Food adds 1 to score
	 Going out of bound = death
	 Max n° of moves: 2N
	*/
	public Game(String name, GameEventHandler eventHandler, int N, int m, GamesData gamesData, SessionType sessionType) {
		this(name,N,m,1,2*N,-1, gamesData,eventHandler, sessionType);
		int gameNumber = Integer.valueOf(name.substring(name.length()-1));
		if(sessionType.equals(SessionType.DECISION_TREE) && !gamesData.isBoardHistoryComplete(gameNumber)) {
			setupGameBoard(0,1,-(N+2),N);
			gamesData.addBoardToHistory(gameBoard);
			setupAntRandomSP();
			gamesData.addStartingPositionToHistory(new Coordinates(startingX,startingY));
		} else {
			this.gameBoard = new GameBoard(gamesData.getBoardHistory().getGameBoard(gameNumber-1));
			setupAnt(gamesData.getBoardHistory().getStartingPosition(gameNumber-1));
		}
		startGame();
	}

	public Game(String name, GameEventHandler eventHandler, GamesData gamesData, SessionType sessionType) {
		this(name,10,2,1,2*10,-1, gamesData,eventHandler, sessionType);
		int gameNumber = Integer.valueOf(name.substring(name.length()-1));
		if(sessionType.equals(SessionType.DECISION_TREE) && !gamesData.isBoardHistoryComplete(gameNumber)) {
			setupGameBoard(0,1,-(N+2),N);
			gamesData.addBoardToHistory(gameBoard);
			setupAntRandomSP();
			gamesData.addStartingPositionToHistory(new Coordinates(startingX,startingY));
		} else {
			this.gameBoard = new GameBoard(gamesData.getBoardHistory().getGameBoard(gameNumber-1));
			setupAnt(gamesData.getBoardHistory().getStartingPosition(gameNumber-1));
		}
		startGame();
	}

	public Game(String name, int N, int m, int foodScore, int maxMoves, int passingDrop, GamesData gamesData, GameEventHandler eventHandler, SessionType sessionType) {
		this.N = N;
		this.m = m;
		this.name=name;
		this.eventHandler=eventHandler;
		this.foodScore=foodScore;
		this.maxMoves=maxMoves;
		this.passingDrop=passingDrop;
		this.gamesData = gamesData;
		this.sessionType=sessionType;

		moves=0;
		gameLog=new GameLog(name);
	}

	public void setupGameBoard(int defaultValue, int foodValue, int outOfBoundValue, int totalFood) {
		this.defaultValue = defaultValue;
		this.foodValue = foodValue;
		this.outOfBoundValue = outOfBoundValue;
		this.totalFood = totalFood;
		
		gameBoard = new GameBoard(N,m,defaultValue,foodValue,outOfBoundValue,totalFood);
	}

	public void setupAnt(int startingX, int startingY) {
		this.startingX=startingX;
		this.startingY=startingY;

		ant = new Ant(startingX,startingY,m,this);
		gameLog.setStartingPosition(new Coordinates(startingX,startingY));
	}

	public void setupAnt(Coordinates startingPosition) {
		this.startingX=startingPosition.x();
		this.startingY=startingPosition.y();

		ant = new Ant(startingX,startingY,m,this);
		gameLog.setStartingPosition(new Coordinates(startingX,startingY));
	}

	public void setupAntRandomSP() {
		Random rand = new Random();
		int startingXCandidate, startingYCandidate;
		do {
			startingXCandidate = rand.nextInt(N) + m;
			startingYCandidate = rand.nextInt(N) + m;
		} while(gameBoard.getCellPoints(new Coordinates(startingXCandidate,startingYCandidate))!=0); //Avoid food

		setupAnt(startingXCandidate,startingYCandidate);
	}

	public void startGame() {
		//Training is built in GUI (arrow keys)
		GUI = new GameGUI(this);

		//Test modes
		switch(sessionType) {
			case TRAINING:
				System.out.println("Manual training mode");
				break;
			case DECISION_TREE:
				try {
					runDecisionTreeTest(getTrainedDecisionTree());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case NEURAL_NETWORK:
				break;
			case GENETIC_ALGORITHMS:
				break;
		}
	}

	int[][] getBoardData() {
		return gameBoard.getBoard();
	}

	public String getGameName() {
		return name;
	}

	////////////////
	//
	// Game Logic
	//

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

	private void runDecisionTreeTest(J48 tree) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				switch(classify()) {
					case 0:
						move("N");
						break;
					case 1:
						move("S");
						break;
					case 2:
						move("E");
						break;
					case 3:
						move("W");
						break;
					default:
						System.out.println("Classification error");
						break;
				}
			}

			private int classify() {
				try {
					AntDataSet viewArff = new AntDataSet("Move",m);
					String[] view = getAntView();
					view[view.length-1]="N";   //Placeholder class
					viewArff.addEntry((Object[]) view);

					Instances antView = new Instances(new BufferedReader(new StringReader(viewArff.printToString())));
					antView.setClassIndex(antView.numAttributes()-1);
					int direction = (int) tree.classifyInstance(antView.instance(0));
					System.out.println(direction);
					return direction;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return -1;
			}
		};

		testTimer = new Timer();
		testTimer.schedule(task,500,200);
	}

	public void move(String direction) {
		//Update GamesData
		if(sessionType.equals(SessionType.TRAINING))
			gamesData.addDataSetRotationEntries(direction, getAntClockwiseRotatedViews());

		//Move
		gameBoard.modifyCellOf(ant.getPosition(), passingDrop);
		if(ant.move(direction)==null) {
			System.err.println("Illegal move");
			return;
		}

		int moveScore = ant.updateScore(gameBoard.getCellPoints(ant.getPosition()));

		moves++;
		GUI.updateUI();

		if(isAntInbound()) {
			System.out.println("Still Alive");
			gameLog.addMoveAndScore(direction,moveScore);
			if(moves>=maxMoves) { //Game finished, moves limit reached
				System.out.println("NO MORE MOVES");
				if(sessionType.equals(SessionType.DECISION_TREE))
					testTimer.cancel();
				GUI.endGame();
			}
		} else { //Out of bound
			System.out.println("DEAD");
			gameLog.addMoveAndScore(direction,moveScore);
			if(sessionType.equals(SessionType.TRAINING))
				gamesData.removeLastDataSetEntries(4);  //We do not want deadly moves in our DataSet
			if(sessionType.equals(SessionType.DECISION_TREE))
				testTimer.cancel();
			GUI.endGame();
		}
	}

	//TODO Handle user window closing
	public void finalizeGame() {
		gamesData.addGameLog(gameLog);
		eventHandler.pushGameData(gamesData);
	}

	public Coordinates getAntPosition() {
		return ant.getPosition();
	}

	public int getM() {
		return m;
	}

	public boolean isAntInbound() {
		return getAntPosition().x()>=m && getAntPosition().x()<m+N && getAntPosition().y()>=m && getAntPosition().y()<m+N;
	}

	private String[] getAntView() {
		return gameBoard.getDataRatio(getAntPosition());
	}

	private String[][] getAntClockwiseRotatedViews() {
		return gameBoard.getDataRatioClockwiseRotations(getAntPosition());
	}

	public int getMaxMoves() {
		return maxMoves;
	}

	public int getMoves() {
		return moves;
	}

	public String getAntStatus() {
		return ant.getStatus();
	}

	public SessionType getSessionType() {
		return sessionType;
	}
}
