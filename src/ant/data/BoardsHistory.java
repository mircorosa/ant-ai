package ant.data;

import ant.game.Coordinates;
import ant.game.GameBoard;

import java.util.ArrayList;

public class BoardsHistory {

	private ArrayList<GameBoard> boards = new ArrayList<>();
	private ArrayList<Coordinates> startingPositions = new ArrayList<>();

	public BoardsHistory() {
	}

	public void addGameBoard(GameBoard gameBoard) {
		boards.add(new GameBoard(gameBoard));
	}

	public ArrayList<GameBoard> getBoards() {
		return boards;
	}

	public GameBoard getGameBoard(int gameNumber) {
		return boards.get(gameNumber);
	}

	public void addStartingPosition(Coordinates position) {
		startingPositions.add(position);
	}

	public ArrayList<Coordinates> getStartingPositions() {
		return startingPositions;
	}

	public Coordinates getStartingPosition(int gameNumber) {
		return startingPositions.get(gameNumber);
	}
}
