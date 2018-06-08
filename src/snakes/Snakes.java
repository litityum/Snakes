package snakes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.Direction;
import game.GridGame;
import things.GridSquare;
import things.Food;
import things.Node;
import things.Snake;


public class Snakes extends GridGame {

	private List<Snake> snakes;
	public GridSquare[][] coordinateMap;

	/**
	 * Creates a new Snakes game instance
	 * @param gridWidth number of grid squares along the width
	 * @param gridHeight number of grid squares along the height
	 * @param gridSquareSize size of a grid square in pixels
	 * @param frameRate frame rate (number of timer ticks per second)
	 * @throws Exception 
	 */
	public Snakes(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) throws Exception {

		super(gridWidth, gridHeight, gridSquareSize, frameRate);
		if(gridWidth < 5 || gridHeight < 2) {
			throw new Exception("hoppa");
		}

		snakes = new ArrayList<>();
		coordinateMap = new GridSquare[gridWidth][gridHeight];
	}

	@Override
	protected void timerTick() {
		// Determine and execute actions for all snakes
		ArrayList<Snake> snakesCopy = new ArrayList<>(snakes);
		int i = 0;
		for(;i < snakesCopy.size();) {
			Snake snake = snakesCopy.get(i);
			
			i++;
			LocalInformation localinfo = createLocalInformationForSnake(snake);
			
			Node current = snake.list.head;
			// Reset current snake's map position (its position will be marked again)
			while(current != null) {
				updateCoordinateMap(current.getX(), current.getY(), null);
				current = current.next;
			}
			current = snake.list.head;
			// Choose action
			Action action = snake.chooseAction(localinfo);
			// Execute action
			if(action != null) {
				//MOVE
				if(action.getType() == Action.Type.MOVE) {
					
					if(isDirectionFree(current.getX(), current.getY(), action.getDirection())) {
						snake.move(action.getDirection());
					}
				}
				//ATTACK
				if(action.getType() == Action.Type.ATTACK) {
					
					if(isDirectionFree(current.getX(), current.getY(), action.getDirection())) {
						
						removeFood(getCoordinateAtDirection(current.getX(), current.getY(), action.getDirection()));
						snake.attack(action.getDirection());
						//After attack if the length = 8 then reproduce
						if(snake.list.size() == 8) {
							Node curr = snake.list.tail;
							for(int j = 0; j < 4; j++) {
								coordinateMap[curr.getX()][curr.getY()] = null;
								curr = curr.prev;
							}
							addSnake(snake.reproduce());
						}

					}
				}
				
			}
			//Updating the map again
			current = snake.list.head;
			while(current != null) {
				updateCoordinateMap(current.getX(), current.getY(), current.gridSquare);
				current = current.next;
			}
			//if the food is eaten then create it again
			if(action.getType() == Action.Type.ATTACK) {
				int x = (int) (Math.random() * getGridWidth());
	            int y = (int) (Math.random() * getGridHeight());
	            
	            while(coordinateMap[x][y] != null) {
	            	x = (int) (Math.random() * getGridWidth());
	            	y = (int) (Math.random() * getGridHeight());
	            }
	            
	            addFood(new Food(x, y));
			}

			
			
			current = snake.list.head;
		}

	}
	
	/**
	 * Adds a new food to the game
	 * @param food food to be added
	 */
	public void addFood(Food food) {
		if (food != null) {
			addDrawable(food);
			coordinateMap[food.x][food.y] = food;
		}
		
	}
	/**
	 * Removes the food from the game
	 * @param food food to be removed
	 */
    private void removeFood(GridSquare food) {
        if (food != null) {
        	
            removeDrawable((Food) food);
            if (isPositionInsideGrid(food.x, food.y)) {
                coordinateMap[food.x][food.y] = null;
            }
        }
    }
    
    /**
     * Adds a snake to the game
     */
	public void addSnake(Snake snake) {
		if (snake != null) {
			snakes.add(snake);
			addDrawable(snake);
			
			Node current = snake.list.head;
			
			while(current != null) {
				coordinateMap[current.getX()][current.getY()] = current.gridSquare;
				
				current = current.next;
			}
			
		}

	}
	
	/**
	 * Creates local information for the snake's head
	 * @param snake snake to get local information
	 * @return local information
	 */
    private LocalInformation createLocalInformationForSnake(Snake snake) {
        int x = snake.list.head.getX();
        int y = snake.list.head.getY();
        
        Food food = null;
        
        for(GridSquare[] coordinate: coordinateMap) {
        	for(GridSquare coor: coordinate) {
        		if(coor instanceof Food)
        			food =(Food) coor;
        	}
        }

        HashMap<Direction, GridSquare> gridSquares = new HashMap<>();
        gridSquares.put(Direction.UP, getCoordinateAtPosition(x, y - 1));
        gridSquares.put(Direction.DOWN, getCoordinateAtPosition(x, y + 1));
        gridSquares.put(Direction.LEFT, getCoordinateAtPosition(x - 1, y));
        gridSquares.put(Direction.RIGHT, getCoordinateAtPosition(x + 1, y));

        ArrayList<Direction> freeDirections = new ArrayList<>();
        if (gridSquares.get(Direction.UP) == null && isPositionInsideGrid(x, y - 1)) {
            freeDirections.add(Direction.UP);
            
        }
        if (gridSquares.get(Direction.DOWN) == null && isPositionInsideGrid(x, y + 1)) {
            freeDirections.add(Direction.DOWN);
          
        }
        if (gridSquares.get(Direction.LEFT) == null && isPositionInsideGrid(x - 1, y)) {
            freeDirections.add(Direction.LEFT);
           
        }
        if (gridSquares.get(Direction.RIGHT) == null && isPositionInsideGrid(x + 1, y)) {
            freeDirections.add(Direction.RIGHT);
            
        }

        return new LocalInformation(getGridWidth(), getGridHeight(), gridSquares, freeDirections, food);
    }


	private boolean isPositionInsideGrid(int x, int y) {
		return (x >= 0 && x < getGridWidth()) && (y >= 0 && y < getGridHeight());
	}

	private void updateCoordinateMap(int x, int y, GridSquare coor) {
		if (isPositionInsideGrid(x, y)) {
			coordinateMap[x][y] = coor;
			
		}
	}

	private GridSquare getCoordinateAtPosition(int x, int y) {
		if (!isPositionInsideGrid(x, y)) {
			return null;
		}
		return coordinateMap[x][y];
	}

	private GridSquare getCoordinateAtDirection(int x, int y, Direction direction) {
		if (direction == null) {
			return null;
		}
		int xTarget = x;
		int yTarget = y;
		if (direction == Direction.UP) {
			yTarget--;
		} else if (direction == Direction.DOWN) {
			yTarget++;
		} else if (direction == Direction.LEFT) {
			xTarget--;
		} else if (direction == Direction.RIGHT) {
			xTarget++;
		}
		return getCoordinateAtPosition(xTarget, yTarget);
	}

	private boolean isPositionFree(int x, int y) {
		return isPositionInsideGrid(x, y) && (getCoordinateAtPosition(x, y) == null || 
				getCoordinateAtPosition(x, y) instanceof Food);
	}

	public boolean isDirectionFree(int x, int y, Direction direction) {
		if (direction == null) {
			return false;
		}
		
		int xTarget = x;
		int yTarget = y;
		if (direction == Direction.UP) {
			yTarget--;
		} else if (direction == Direction.DOWN) {
			yTarget++;
		} else if (direction == Direction.LEFT) {
			xTarget--;
		} else if (direction == Direction.RIGHT) {
			xTarget++;
		}
		return isPositionFree(xTarget, yTarget);
	}

}
