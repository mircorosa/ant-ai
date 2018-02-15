package ant.game;

public class Coordinates {

	private int x, y;

	public Coordinates() {
	}

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int y() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void moveXOf(int value) {
		x+=value;
	}

	public void moveYOf(int value) {
		y+=value;
	}

	public void setPosition(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public void printPosition() {
		System.out.println("Position: x="+x+" y="+y);
	}
}
