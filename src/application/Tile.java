package application;

import java.awt.Point;

public class Tile {
	// Possible types of the tile
	public enum Type {
		BLANK, BORDER, SNAKE_HEAD, SNAKE_BODY, APPLE
	}
	
	// The type of the tile
	private Type type;
	
	// The location of the tile
	private Point location;
	
	/**
	 * Initialize the tile with a location,
	 * set the default type to blank.
	 */
	Tile(Point location) {
		this.type = Type.BLANK;
		this.location = location;
	}
	
	// Get the type of the tile
	public Type type() {
		return this.type;
	}
	
	// Set the type of the tile
	public void setType(Type type) {
		this.type = type;
	}
	
	// Get the location of the tile
	public Point location() {
		return this.location;
	}
	
	// Set the location of the tile
	public void setLocation(Point location) {
		this.location = location;
	}
}
