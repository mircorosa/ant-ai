package ant.game;

import ant.GameEventHandler;
import ant.data.GameLog;
import ant.data.TestData;

import java.util.Random;

public class Game {

	private GameBoard gameBoard;
	private Ant ant;
	private GameGUI GUI;

	private String name;
	private int N, m;
	private int defaultValue, foodValue, outOfBoundValue, totalFood;
	private int startingX, startingY, foodScore, maxMoves, passingDrop, moves;
	private GameLog gameLog;

	private TestData testData;

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
	public Game(String name, GameEventHandler eventHandler, int N, int m, TestData testData) {
		this(name,N,m,1,2*N,-1,testData,eventHandler);
		setupGameBoard(0,1,-(N+2),N);
		setupAntRandomSP();
		startGame();
	}

	public Game(String name, GameEventHandler eventHandler, TestData testData) {
		this(name,10,2,1,2*10,-1,testData,eventHandler);
		setupGameBoard(0,1,-(N+2),N);
		setupAntRandomSP();
		startGame();
	}

	public Game(String name, int N, int m, int foodScore, int maxMoves, int passingDrop, TestData testData, GameEventHandler eventHandler) {
		this.N = N;
		this.m = m;
		this.name=name;
		this.eventHandler=eventHandler;
		this.foodScore=foodScore;
		this.maxMoves=maxMoves;
		this.passingDrop=passingDrop;
		this.testData=testData;

		moves=0;
		gameLog=new GameLog(name);
	}

	public void setupGameBoard(int defaultValue, int foodValue, int outOfBoundValue, int totalFood) {
		this.defaultValue = defaultValue;
		this.foodValue = foodValue;
		this.outOfBoundValue = outOfBoundValue;
		this.totalFood = totalFood;

		//TODO Prevent food on ant starting cell
		gameBoard = new GameBoard(N,m,defaultValue,foodValue,outOfBoundValue,totalFood);
	}

	public void setupAnt(int startingX, int startingY) {
		this.startingX=startingX;
		this.startingY=startingY;

		ant = new Ant(startingX,startingY,m,this);
	}

	public void setupAntRandomSP() {
		Random rand = new Random();
		int startingXCandidate, startingYCandidate;
		do {
			startingXCandidate = rand.nextInt(N) + m;
			startingYCandidate = rand.nextInt(N) + m;
		} while(gameBoard.getCellPoints(new Coordinates(startingXCandidate,startingYCandidate))!=0);

		setupAnt(startingXCandidate,startingYCandidate);
	}

	public void startGame() {
		GUI = new GameGUI(this);
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

	public void move(String direction) {
		//Update TestData
		testData.addDataSetRotationEntries(direction, getAntClockwiseRotatedViews());

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
				GUI.endGame();
			}
		} else { //Out of bound
			System.out.println("DEAD");
			gameLog.addMoveAndScore(direction,moveScore);
			testData.removeLastDataSetEntries(4);  //We do not want deadly moves in our DataSet
			GUI.endGame();
		}
	}

	public void finalizeGame() {
		testData.addGameLog(gameLog);
		eventHandler.pushGameData(testData);
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
}
