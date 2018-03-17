package ant.data;

import java.util.ArrayList;

public class FullLog {

	private ArrayList<GameLog> logs = new ArrayList<>();

	public FullLog() {
	}

	public void addGameLog(GameLog gameLog) {
		logs.add(gameLog);
	}

	public ArrayList<GameLog> getLogs() {
		return logs;
	}
}
