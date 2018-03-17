package ant.paths;

import ant.game.Coordinates;

import java.util.Arrays;

public class PathsBoard {

	private int N, m;
	private int[][] board;
	private int defaultValue, outOfBoundValue;

	public PathsBoard(Paths paths, int defaultValue, int outOfBoundValue) {
		this.N = paths.getN();
		this.m = paths.getM();
		this.defaultValue = defaultValue;
		this.outOfBoundValue = outOfBoundValue;

		board = new int[N+2*m][N+2*m];

		initializeBoard();
	}

	private void initializeBoard() {
		//Default values + bounds
		for (int i = 0; i < board.length; i++)
			if (i < m || i >= board.length - m)
				Arrays.fill(board[i], outOfBoundValue);
			else
				for (int j = 0; j < board[i].length; j++)
					board[i][j] = (j < m || j >= board[i].length - m) ? outOfBoundValue : defaultValue;
	}

	public int getCellPoints(Coordinates coordinates) {
		return board[coordinates.y()][coordinates.x()];
	}

	public void setCellPoints(Coordinates coordinates, int points) {
		board[coordinates.y()][coordinates.x()]=points;
	}

	public void modifyCellOf(Coordinates coordinates, int points) {
//		if(coordinates.x()>m && coordinates.x()<N+m && coordinates.y()>m && coordinates.y()<N+m)
			board[coordinates.y()][coordinates.x()]+=points;
	}

	int[][] getBoard() {
		return board;
	}
}
