package ant.game;

public class Ant {
	private int startingX, startingY;
	private int m;
	private int score;
	private Coordinates position = new Coordinates();
	private String status;
	private Game game;

	public Ant(int startingX, int startingY, int m, Game game) {
		this.startingX = startingX;
		this.startingY = startingY;
		this.m = m;
		this.game=game;
		position.setPosition(startingX,startingY);
		this.status="Ready to start";
		score=0;
	}

	public Coordinates move(String direction) {
		switch (direction) {
			case "N":
				position.moveYOf(-1);
				break;
			case "S":
				position.moveYOf(1);
				break;
			case "E":
				position.moveXOf(1);
				break;
			case "W":
				position.moveXOf(-1);
				break;
			default:
				System.err.println("Invalid direction");
				return null;
		}

//		position.printPosition();
		return position;
	}

	public int updateScore(int points) {
		int oldScore = score;
		if(game.isAntInbound()) {
			score += points>=0 ? points : 0;
			status = points > 0 ? "Yummy!" : "Searching for food";
		}
		else {
			score += points;
			status = "Dead";
		}
		return score-oldScore;
	}

	public Coordinates getPosition() {
		return position;
	}

	public void setPosition(Coordinates position) {
		this.position = position;
	}

	public int getScore() {
		return score;
	}

	public String getStatus() {
		return status;
	}
}
