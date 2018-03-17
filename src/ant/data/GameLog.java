package ant.data;

import ant.game.Coordinates;

import java.util.ArrayList;

public class GameLog {

	private Coordinates startingPosition;
	private ArrayList<String> moves = new ArrayList<>();
	private ArrayList<Integer> scores = new ArrayList<>();
	private String name;

	public GameLog(String name) {
		this.name = name;
	}

	public GameLog(String name, Coordinates startingPosition) {
		this.name = name;
		this.startingPosition = startingPosition;
	}

	public void logMove(String move) {
		moves.add(move);
	}

	public void logScore(int score) {
		scores.add(score);
	}

	public int getMoveCount() {
		return moves.size();
	}

	public ArrayList<Integer> getScores() {
		return scores;
	}

	public String getName() {
		return name;
	}

	public Coordinates getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(Coordinates startingPosition) {
		this.startingPosition = startingPosition;
	}

	public void addMoveAndScore(String direction, int score) {
		moves.add(direction);
		scores.add(score);
	}

	public ArrayList<String> getMoves() {
		return moves;
	}
}
