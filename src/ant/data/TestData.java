package ant.data;

import java.io.FileNotFoundException;

public class TestData {

	private CompleteLog completeLog;
	private AntDataSet dataSet;

	public TestData() {
	}

	public TestData(CompleteLog completeLog, AntDataSet dataSet) {
		this.completeLog = completeLog;
		this.dataSet = dataSet;
	}

	public CompleteLog getCompleteLog() {
		return completeLog;
	}

	public AntDataSet getDataSet() {
		return dataSet;
	}

	public void addDataSetEntry(String direction, String... values) {
		values[values.length-1]=direction;
		dataSet.addEntry(values);
	}

	public void addGameLog(GameLog gameLog) {
		completeLog.addGameLog(gameLog);
	}

	public void generateArff() {
		try {
			dataSet.printToFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void removeLastDataSetEntry() {
		dataSet.removeLastEntry();
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
