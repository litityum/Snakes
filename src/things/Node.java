package things;
/**
 * Nodes for the Linked List
 * @author Ömer
 *
 */
public class Node {
	public GridSquare gridSquare;
	public Node next;
	public Node prev;
	
	public Node(GridSquare coor) {
		this.gridSquare = coor;
	}
	
	public int getX() {
		return gridSquare.x;
	}
	public int getY() {
		return gridSquare.y;
	}

}
