package application;

import java.awt.Point;

import application.Tile.Type;
import javafx.scene.paint.Color;

public class Apple {
	// The location of the apple
	public Point location;
	
	// Whether or not the apple is placed on the board
	public static boolean placed = false;
	
	// The color of the apple
	public Color color = Color.web("#f44336");

	/**
	 * Places the apple at a given point on the board.
	 * 
	 * @param Point p 	The point to place the apple on.
	 */
	public void place(Point p) {
		location = new Point((int) p.getX(), (int) p.getY());
		placed = true;
	}
}
