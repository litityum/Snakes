package things;

import java.awt.Color;
import java.util.ArrayList;

import game.Direction;
import game.Drawable;
import snakes.Action;
import snakes.LocalInformation;
import ui.GridPanel;
/**
 * Snake class for the game
 * @author Ömer
 *
 */
public class Snake implements Drawable{
	//Snakes consist of Nodes and their data are GridSquare
	public MyLinkedList list;
	/**
	 * Constructor
	 * @param list list to be Snake's body
	 */
	public Snake(MyLinkedList list) {
		this.list = list;
	}
	/**
	 * Move method
	 * @param direction direction to be moved
	 */
	public void move(Direction direction) {
		attack(direction);
		
		list.remove(list.size() - 1);
		//System.out.println("Size = " + list.size());
	}
	/**
	 * Reproduce method
	 * @return the new Snake
	 */
	public Snake reproduce() {
		MyLinkedList child = new MyLinkedList();
		Node current = list.tail;
		for(int i = 0; i < 4; i++) {
			child.add(current.gridSquare);
			current = current.prev;
		}
		
		current = list.head;
		for(int i = 0; i < 3; i++) {
			current = current.next;
		}
		list.tail = current;
		current.next = null;
		
		
		
		return new Snake(child);
	}
	/**
	 * Attack method
	 * @param direction direction to be attacked
	 */
	public void attack(Direction direction) {
		GridSquare head;
		if(direction == Direction.UP) {
			head = new GridSquare(list.head.getX(), list.head.getY() - 1);

		}
		else if(direction == Direction.DOWN) {
			head = new GridSquare(list.head.getX(), list.head.getY() + 1);

		}
		else if(direction == Direction.LEFT) {
			head = new GridSquare(list.head.getX() - 1, list.head.getY());

		}
		else{
			head = new GridSquare(list.head.getX() + 1, list.head.getY());

		}
		
		list.insert(head, 0);
		
	}
	/**
	 * Choosing action
	 * 
	 * 
	 * @param localinfo local info of the head of the snake
	 * @return Action to be executed
	 */
	public Action chooseAction(LocalInformation localinfo) {
		//If it is possible then returns Attack action
		if(localinfo.getCoordinateUp() instanceof Food) {
			return new Action(Action.Type.ATTACK, Direction.UP);
		}
		
		else if(localinfo.getCoordinateDown() instanceof Food) {
			return new Action(Action.Type.ATTACK, Direction.DOWN);
		}
		
		else if(localinfo.getCoordinateLeft() instanceof Food) {
			return new Action(Action.Type.ATTACK, Direction.LEFT);
		}
		else if(localinfo.getCoordinateRight() instanceof Food) {
			return new Action(Action.Type.ATTACK, Direction.RIGHT);
		}
		//Move to the food if it is possible
		ArrayList<Direction> moveable = new ArrayList<Direction>();
		if(list.head.getX() > localinfo.getFood().x) {
			if(localinfo.getFreeDirections().contains(Direction.LEFT)) {
				moveable.add(Direction.LEFT);
			}
		} 
		else if(list.head.getX() < localinfo.getFood().x){
			if(localinfo.getFreeDirections().contains(Direction.RIGHT)) {
				moveable.add(Direction.RIGHT);
			}
		}
		
		if(list.head.getY() > localinfo.getFood().y) {
			if(localinfo.getFreeDirections().contains(Direction.UP)) {
				moveable.add(Direction.UP);
			}
		}
		else if(list.head.getY() < localinfo.getFood().y){
			if(localinfo.getFreeDirections().contains(Direction.DOWN)) {
				moveable.add(Direction.DOWN);
			}
		}
		
		if(!moveable.isEmpty()) {
			return new Action(Action.Type.MOVE, LocalInformation.getRandomDirection(moveable));
		}
		
		//if moving toward to food is impossible then move random free direction
		return new Action(Action.Type.MOVE, LocalInformation.getRandomDirection(localinfo.getFreeDirections()));
	}

	public void draw(GridPanel panel) {
		
		panel.drawSquare(list.head.getX(), list.head.getY(), Color.RED);
		Node current = list.head;
		
		while(current.next != null) {
			current = current.next;
			panel.drawSquare(current.getX(), current.getY(), Color.BLUE);
			
		}
		
	}

}
