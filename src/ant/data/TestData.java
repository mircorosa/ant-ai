package ant.data;

import java.io.FileNotFoundException;

public class TestData {

	private CompleteLog completeLog;
	private AntDataSet dataSet;
	private String path, fileName;

	public TestData() {
	}

	public TestData(CompleteLog completeLog, AntDataSet dataSet, String path, String fileName) {
		this.completeLog = completeLog;
		this.dataSet = dataSet;
		this.path=path;
		this.fileName=fileName;
	}

	public CompleteLog getCompleteLog() {
		return completeLog;
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
		completeLog.addGameLog(gameLog);
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
		for (GameLog log : completeLog.getLogs())
			count+=log.getMoveCount();
		return count;
	}

	public int countScore() {
		int totalScore=0;
		for(GameLog log : completeLog.getLogs()) {
			for(int score : log.getScores()) {
				if(score>0) totalScore+=score;
			}
		}
		return totalScore;
	}

	public int countDeaths() {
		int deaths=0;
		for(GameLog log : completeLog.getLogs()) {
			for(int score : log.getScores()) {
				if(score<0) deaths++;
			}
		}
		return deaths;
	}

	public int countSurvivals() {
		return getCompleteLog().getLogs().size()-countDeaths();
	}
}
