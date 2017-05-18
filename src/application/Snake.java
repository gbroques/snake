package application;

import java.util.LinkedList;
import java.awt.Point;
import javafx.scene.paint.Color;

import application.Board.Direction;

public class Snake {
	// The length of the snake
	public int length;

	// The head of the snake
	public Point head;
	
	// The body of the snake
	public LinkedList<Point> body;
	
	// The color of the head
	public Color headColor = Color.web("#4CAF50");
	
	// The color of the body
	public Color bodyColor = Color.web("#8BC34A");
	
	/**
	 * Constructor
	 * 
	 * Initialize snake length to 1,
	 * location to the fifth row, fifth column of the board,
	 * and create a linked list to keep track of the body.
	 */
	Snake() {
		this.length = 1;
		this.head = new Point(5, 5);
		this.body = new LinkedList<Point>();
	}
	
	/**
	 * Increment the length of the snake
	 */
	public void eatApple() {
		this.length++;
	}
	
	/**
	 * Move the snake to a new point on the board.
	 * 
	 * @param point P 	The point to move the snake to.
	 */
	public void moveTo(Point p) {
		this.head = new Point((int) p.getX(), (int) p.getY());
	}
	
	/**
	 * Returns the next point the snake could move to
	 * based upon a direction and it's current location.
	 * 
	 * @param direction 	NORTH, EAST, SOUTH, WEST
	 * 
	 * @return Point 		The next point for the snake to move to.
	 */
	public Point getNextPoint(Direction direction) {
		switch (direction) {
			case NORTH:
				return new Point((int) this.head.getX(), (int) this.head.getY() - 1);
			case EAST:
				return new Point((int) this.head.getX() + 1, (int) this.head.getY());
			case SOUTH:
				return new Point((int) this.head.getX(), (int) this.head.getY() + 1);
			case WEST:
				return new Point((int) this.head.getX() - 1, (int) this.head.getY());
			default:
				return new Point();
		}
	}
	
	/**
	 * Adds an element to the body of the snake.
	 * 
	 * @param Point p 	The location of the body element to add
	 */
	public void addToBody(Point p) {
		this.body.addFirst(new Point((int) p.getX(), (int) p.getY()));
		
		trimBody();
	}
	
	/**
	 * Trims the body if it exceeds the length of the snake.
	 * 
	 * Keep track of an additional tile for when the snake eats an apple.
	 */
	private void trimBody() {
		if (this.body.size() > this.length) {
			this.body.removeLast();
		}
	}
}