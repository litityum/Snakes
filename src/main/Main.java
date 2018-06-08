package main;

import java.awt.EventQueue;

import snakes.Snakes;
import things.*;
import ui.ApplicationWindow;

/**
 * The main class of the project.
 */
public class Main {

    /**
     * Main entry point for the application.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
    	
    	
    	
        EventQueue.invokeLater(() -> {
            try {
                // Create game
                // You can change the world width and height, size of each grid square in pixels or the game speed
                Snakes game = new Snakes(25, 25, 20, 100);
                //Adds the main snake
                MyLinkedList list = new MyLinkedList();
                for(int i = 0; i < 4; i++) {
                	list.add(new GridSquare(4 - i, 1));
                }
                Snake snake = new Snake(list);
                game.addSnake(snake);
                
    			int x = (int) (Math.random() * game.getGridWidth());
                int y = (int) (Math.random() * game.getGridHeight());
                
                while(game.coordinateMap[x][y] != null) {
                	x = (int) (Math.random() * game.getGridWidth());
                	y = (int) (Math.random() * game.getGridHeight());
                }

                
                game.addFood(new Food(x, y));
             


                // Create application window that contains the game panel
                ApplicationWindow window = new ApplicationWindow(game.getGamePanel());
                window.getFrame().setVisible(true);

                // Start game
                game.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
