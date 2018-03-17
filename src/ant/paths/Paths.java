package ant.paths;

import ant.data.GameLog;
import ant.data.GamesData;
import ant.game.Coordinates;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Paths {

	private GamesData gamesData;
	private int N, m;
	private PathsBoard board;
	ArrayList<Coordinates> movesList;
	private PathsGUI GUI;
	private Timer animationTimer;
	private TimerTask animationTask;
	private Coordinates antPosition;

	private static final int ANIMATION_PERIOD = 100;


	//TODO Handle user window closing
	public Paths(GamesData gamesData, int N, int m) {
		this.gamesData = gamesData;
		this.N = N;
		this.m = m;
		antPosition = new Coordinates(gamesData.getFullLog().getLogs().isEmpty() ? new Coordinates() : gamesData.getFullLog().getLogs().get(0).getStartingPosition());

		initialize();
		runExecution();

	}

	public void initialize() {
		board = new PathsBoard(this,0,1);
		GUI = new PathsGUI(this);
	}

	private void runExecution() {
		movesList = new ArrayList<>();
		for (GameLog gameLog : gamesData.getFullLog().getLogs()) {
			movesList.add(gameLog.getStartingPosition());
			for(String move : gameLog.getMoves()) {
				movesList.add(new Coordinates(movesList.get(movesList.size()-1)));
				switch (move) {
					case "N":
						movesList.get(movesList.size()-1).moveYOf(-1);
						break;
					case "E":
						movesList.get(movesList.size()-1).moveXOf(1);
						break;
					case "S":
						movesList.get(movesList.size()-1).moveYOf(1);
						break;
					case "W":
						movesList.get(movesList.size()-1).moveXOf(-1);
						break;
				}
			}
		}

		animationTimer = new Timer();
		animationTask = new PathsTask(animationTimer);
		animationTimer.schedule(animationTask,500,ANIMATION_PERIOD);
	}

	public int[][] getBoard() {
		return board.getBoard();
	}

	public GamesData getGamesData() {
		return gamesData;
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return m;
	}

	public Coordinates getAntPosition() {
		return antPosition;
	}

	public void setAntPosition(Coordinates antPosition) {
		this.antPosition = antPosition;
	}

	public void speedUpAnimations() {
		animationTimer.cancel();
		Timer endingTimer = new Timer();
		endingTimer.schedule(new PathsTask(endingTimer),0,1);
	}

	private class PathsTask extends TimerTask {

		Timer timer;

		public PathsTask(Timer timer) {
			this.timer = timer;
		}

		@Override
		public void run() {
			if(!movesList.isEmpty()) {
				board.modifyCellOf(movesList.get(0), -1);
				setAntPosition(movesList.get(0));
				movesList.remove(0);

				GUI.updateUI();
			}
			else
				timer.cancel();
		}
	}
}
