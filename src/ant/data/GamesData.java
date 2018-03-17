package ant.data;

import ant.game.Coordinates;
import ant.game.GameBoard;

import java.io.FileNotFoundException;

public class GamesData {

	private FullLog fullLog;
	private AntDataSet dataSet;
	private String path, fileName;
	private BoardsHistory history;

	public GamesData() {
	}

	public GamesData(FullLog fullLog, BoardsHistory history, AntDataSet dataSet, String path, String fileName) {
		this.fullLog = fullLog;
		this.dataSet = dataSet;
		this.path=path;
		this.fileName=fileName;
		this.history = history;
	}

	public FullLog getFullLog() {
		return fullLog;
	}

	public AntDataSet getDataSet() {
		return dataSet;
	}

	public void addDataSetEntry(String direction, String... values) {
		values[values.length-1]=direction;
		dataSet.addEntry((Object) values);
	}

	public void addDataSetRotationEntries(String direction, String[]... values) {
		String directions = "NWSE";

		for (int i = 0; i < values.length; i++) {
			values[i][values[i].length-1]= String.valueOf(directions.charAt((directions.indexOf(direction)+i)%directions.length()));
			dataSet.addEntry((Object[]) values[i]);
		}
	}

	public void addGameLog(GameLog gameLog) {
		fullLog.addGameLog(gameLog);
	}

	public void generateArff() {
		try {
			dataSet.printToFile(path,fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void removeLastDataSetEntries(int number) {
		for (int i = 0; i < number; i++) {
			dataSet.removeLastEntry();
		}
	}

	public int countMoves() {
		int count=0;
		for (GameLog log : fullLog.getLogs())
			count+=log.getMoveCount();
		return count;
	}

	public int countScore() {
		int totalScore=0;
		for(GameLog log : fullLog.getLogs()) {
			for(int score : log.getScores()) {
				if(score>0) totalScore+=score;
			}
		}
		return totalScore;
	}

	public int countDeaths() {
		int deaths=0;
		for(GameLog log : fullLog.getLogs()) {
			for(int score : log.getScores()) {
				if(score<0) deaths++;
			}
		}
		return deaths;
	}

	public int countSurvivals() {
		return getFullLog().getLogs().size()-countDeaths();
	}

	public BoardsHistory getBoardHistory() {
		return history;
	}

	public boolean isBoardHistoryComplete(int gameNumber) {
		return getBoardHistory().getBoards().size() >= gameNumber ;
	}

	public void addBoardToHistory(GameBoard gameBoard) {
		getBoardHistory().addGameBoard(gameBoard);
	}

	public void addStartingPositionToHistory(Coordinates startingPosition) {
		getBoardHistory().addStartingPosition(startingPosition);
	}

	public void cleanDataForNewTest() {
		fullLog = new FullLog();
		dataSet = new AntDataSet(dataSet.getName(),dataSet.getM());
	}
}
