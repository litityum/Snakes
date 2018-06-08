package things;
/**
 *
 * @author Ömer
 *
 */
public class GridSquare {
	public int x;
	public int y;
	
	public GridSquare(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	public boolean equals(GridSquare coor) {
		return this.x == coor.x && this.y == coor.y;
	}
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
