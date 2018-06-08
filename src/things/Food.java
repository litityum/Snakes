package things;
import java.awt.Color;

import game.Drawable;
import ui.GridPanel;

public class Food extends GridSquare implements Drawable{
	
	
	
	public Food(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(GridPanel panel) {
		panel.drawSquare(x, y, Color.YELLOW);
		
	}

}
