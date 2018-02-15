package ant.data;

import java.util.ArrayList;

public class CompleteLog {

	ArrayList<GameLog> logs = new ArrayList<>();

	public CompleteLog() {
	}

	public void addGameLog(GameLog gameLog) {
		logs.add(gameLog);
	}

	public ArrayList<GameLog> getLogs() {
		return logs;
	}
}
