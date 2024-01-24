package gameOfLife;

public class Step {
	public int number;
	public String type;
	public String direction;
	public int row;
	public int col;
	
	
	public Step(int num, String type, String dirc, int row, int col) {
		number = num;
		this.type = type;
		direction = dirc;
		this.row = row;
		this.col = col;
	}

}
