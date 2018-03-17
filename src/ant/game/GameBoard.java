package ant.game;

import java.security.SecureRandom;
import java.util.Arrays;

public class GameBoard {

	private int N, m;
	private int[][] board;
	private int defaultValue, foodValue, outOfBoundValue;
	private int totalFood;

	public GameBoard(int N, int m, int defaultValue, int foodValue, int outOfBoundValue, int totalFood) {
		this.N = N;
		this.m = m;
		this.defaultValue = defaultValue;
		this.foodValue = foodValue;
		this.outOfBoundValue = outOfBoundValue;
		this.totalFood = totalFood;

		board = new int[N+2*m][N+2*m];

		initializeBoard();
	}

	public GameBoard(GameBoard gameBoard) {
		this.N = gameBoard.getN();
		this.m = gameBoard.getM();
		this.defaultValue = gameBoard.getDefaultValue();
		this.foodValue = gameBoard.getFoodValue();
		this.outOfBoundValue = gameBoard.getOutOfBoundValue();
		this.totalFood = gameBoard.getTotalFood();
		this.board = gameBoard.getBoardCopy();
	}

	private void initializeBoard() {
		//Default values + bounds
		for (int i = 0; i < board.length; i++)
			if (i < m || i >= board.length - m)
				Arrays.fill(board[i], outOfBoundValue);
			else
				for (int j = 0; j < board[i].length; j++)
					board[i][j] = (j < m || j >= board[i].length - m) ? outOfBoundValue : defaultValue;

		//Food
		SecureRandom rng = new SecureRandom();
		for (int i = 0; i < totalFood; i++) {
			int row = rng.nextInt(board.length);
			int col = rng.nextInt(board[i].length);

			//Prevent food placement on out of bounds cells
			if(row < m || row >= board.length - m || col < m || col >= board[row].length - m)   --i;
			else board[row][col] = foodValue;
		}

		//Testing print
		for(int[] row : board) {
			for (int cell : row)
				System.out.print(cell + " ");
			System.out.println();
		}
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

	public String[] getDataRatio(Coordinates center) {
		String[] data = new String[((2*m+1)*(2*m+1))];  //(2*m+1)*(2*m+1)-1 grid elements, last one left free for direction
		int count = 0;
		for (int j = center.y()-m; j <= center.y() + m; j++) {
			for (int i = center.x()-m; i <= center.x()+m; i++) {
				if(i!=center.x() || j!=center.y()) {
					data[count] = String.valueOf(getCellPoints(new Coordinates(i, j)));
					count++;
				}
			}
		}
		return data;
	}

	public String[][] getDataRatioClockwiseRotations(Coordinates center) {

		String[][] rotations = new String[4][((2*m+1)*(2*m+1))];

		rotations[0] = getDataRatio(center);

		//First rotation
		int count1 = 0;
		for (int j = center.y()+m; j >= center.y() - m; j--) {
			for (int i = center.x()-m; i <= center.x()+m; i++) {
				if(i!=center.x() || j!=center.y()) {

					rotations[1][count1] = String.valueOf(getCellPoints(new Coordinates(i, j)));
					count1++;
				}
			}
		}

		//Second rotation
		int count2 = 0;
		for (int j = center.y()+m; j >= center.y() - m; j--) {
			for (int i = center.x()+m; i >= center.x()-m; i--) {
				if(i!=center.x() || j!=center.y()) {

					rotations[2][count2] = String.valueOf(getCellPoints(new Coordinates(i, j)));
					count2++;
				}
			}
		}

		//Third rotation
		int count3 = 0;
		for (int j = center.y()-m; j <= center.y() + m; j++) {
			for (int i = center.x()+m; i >= center.x()-m; i--) {
				if(i!=center.x() || j!=center.y()) {

					rotations[3][count3] = String.valueOf(getCellPoints(new Coordinates(i, j)));
					count3++;
				}
			}
		}

		return rotations;
	}

	int[][] getBoard() {
		return board;
	}

	int[][] getBoardCopy() {
		int[][] copy = new int[board.length][board[0].length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				copy[i][j] = board[i][j];
			}
		}

		return copy;
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return m;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public int getFoodValue() {
		return foodValue;
	}

	public int getOutOfBoundValue() {
		return outOfBoundValue;
	}

	public int getTotalFood() {
		return totalFood;
	}
}
