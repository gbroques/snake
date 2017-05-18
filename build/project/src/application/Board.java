package application;

import java.awt.Point;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import application.Snake;
import application.Tile;
import application.Tile.Type;
import application.Apple;


public class Board {
	// The dimension of the board
	private final int BORDER = 1;
	private final int DIMENSION = BORDER + 10 + BORDER;
	
	// The size of the canvas
	public final double CANVAS_SIZE = 600;
	
	// The board
	private Tile board[][] = new Tile[DIMENSION][DIMENSION];
	
	// The canvas
	private Canvas canvas;
	
	// The graphics context to draw on the canvas
	private GraphicsContext gc;
	
	// The snake
	private Snake snake;
	
	// The apple
	private Apple apple;
		
	
	// Directions
	public enum Direction {
		NORTH, EAST, SOUTH, WEST;
		
		// Get the next direction
		public Direction next() {
			return values()[(ordinal() + 1) % values().length];
		}
	}
	
	// The color of empty tiles
	public Color tileColor = Color.WHITE;
	
	// The color of the border
	public Color borderColor = Color.web("#212121");
	
	/**
	 * Contstructor
	 * 
	 * Initializes the board with tiles, a border,
	 * and the initial location of the snake.
	 */
	Board() {
		this.canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
		this.gc = canvas.getGraphicsContext2D();
		this.apple = new Apple();
		this.snake = new Snake();
		
		for (int row = 0; row < DIMENSION; row++) {
			for (int column = 0; column < DIMENSION; column++) {
				// get the current location within the loop
				Point location = new Point(row, column);
				
				// initailze each space on the board with a blank tile
				board[row][column] = new Tile(location);
				
				// overwite the blank tile with a border tile, if the type is border
				if (isBorder(location)) set(location, Type.BORDER);

				// overwite the blank tile with a snake head tile, if the type is snake head
				if (location.equals(this.snake.head)) set(location, Type.SNAKE_HEAD);
			}
		}
	}
	
	/**
	 * Returns the dimension of the board.
	 * 
	 * @return int
	 */
	public int dimension() {
		return this.DIMENSION;
	}
	
	/**
	 * Get the canvas
	 * 
	 * @return Canvas canvas
	 */
	public Canvas getCanvas() {
		return this.canvas;
	}

	/**
	 * Get the graphics context
	 * 
	 * @return GraphicsContext gc
	 */
	public GraphicsContext getGraphicsContext() {
		return this.gc;
	}
	
	/**
	 * Returns the snake.
	 * 
	 * @return Snake snake
	 */
	public Snake snake() {
		return this.snake;
	}
	
	/**
	 * Returns the apple.
	 * 
	 * @return Apple apple
	 */
	public Apple apple() {
		return this.apple;
	}
	
	/**
	 * Set the type of a location on the board.
	 * 
	 * @param location 	A point on the board.
	 * @param type		The type of the tile.
	 */
	public void set(Point location, Type type) {
		this.board[(int) location.getX()][(int) location.getY()].setType(type);
	}
	
	/**
	 * Get the type of a location on the board.
	 * 
	 * @param location 	A point on the board.
	 * @return Type		The type of tile.
	 */
	public Type typeOf(Point location) {
		return this.board[(int) location.getX()][(int) location.getY()].type();
	}
	
	/**
	 * Updates the board.
	 * 
	 * Hides the last element of the snake's body,
	 * until an apple is eaten.
	 */
	public void update() {
		for (int row = 0; row < DIMENSION; row++) {
			for (int column = 0; column < DIMENSION; column++) {
				// get the current location within the loop
				Point location = new Point(row, column);
				
				// set the location to blank by default
				set(location, Type.BLANK);
				
				// overwrite the location to border, if type is border
				if (isBorder(location)) set(location, Type.BORDER);
				
				// overwrite the location to apple, if type is apple
				if (location.equals(this.apple.location)) set(location, Type.APPLE);
				
				// overwrite the location to snake head, if type is snake head
				if (location.equals(this.snake.head)) set(location, Type.SNAKE_HEAD);
				
				// overwrite the location to snake body, if type is snake body
				int bodySize = this.snake.body.size() - 1;
				for (int i = 0; i < bodySize; i++) {
					if (location.equals(this.snake.body.get(i))) {
						set(location, Type.SNAKE_BODY);
					}
				}
			}
		}
	}
	
	/**
	 * Updates the board.
	 * 
	 * Pass in true to show the last element of the snake's body.
	 * @param grow
	 */
	public void update(boolean grow) {
		for (int row = 0; row < DIMENSION; row++) {
			for (int column = 0; column < DIMENSION; column++) {
				// get the current location within the loop
				Point location = new Point(row, column);
				
				// set the location to blank by default
				set(location, Type.BLANK);
				
				// overwrite the location to border, if type is border
				if (isBorder(location)) set(location, Type.BORDER);
				
				// overwrite the location to apple, if type is apple
				if (location.equals(this.apple.location)) set(location, Type.APPLE);
				
				// overwrite the location to snake head, if type is snake head
				if (location.equals(this.snake.head)) set(location, Type.SNAKE_HEAD);
				
				// overwrite the location to snake body, if type is snake body
				int bodySize = this.snake.body.size() - 1;
				if (grow) bodySize++;
				for (int i = 0; i < bodySize; i++) {
					if (location.equals(this.snake.body.get(i))) {
						set(location, Type.SNAKE_BODY);
					}
				}
			}
		}
	}
	
    public void display() {
    	// clear board before displaying
    	clear();
    	
		for (int row = 0; row < DIMENSION; row++) {
			for (int column = 0; column < DIMENSION; column++) {
				// calculate the width of one tile
				int tileWidth = (int) (this.canvas.getWidth() / DIMENSION);
				
				// caculate the coordinates of each tile on the canvas
				int x = tileWidth * row;
				int y = tileWidth * column;
				
				// determine what each tile looks like
				switch (this.board[row][column].type()) {
					case BLANK:
						this.gc.setFill(tileColor);
						this.gc.fillRect(x, y, tileWidth, tileWidth);
						break;
					case BORDER:
						this.gc.setFill(borderColor);
						this.gc.fillRect(x, y, tileWidth, tileWidth);
						break;
					case SNAKE_HEAD:
						this.gc.setFill(this.snake.headColor);
						this.gc.fillRoundRect(x, y, tileWidth, tileWidth, 45, 45);
						break;
					case SNAKE_BODY:
						this.gc.setFill(this.snake.bodyColor);
						this.gc.fillRoundRect(x, y, tileWidth, tileWidth, 45, 45);
						break;
					case APPLE:
						this.gc.setFill(this.apple.color);
						this.gc.fillRoundRect(x, y, tileWidth, tileWidth, 60, 60);
						break;
				}
			}
		}
    }
    
    /**
     * Clears the board.
     * 
     * @param canvas
     * @param gc
     */
    public void clear() {
    	this.gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }
    
    /**
     * Updates and displays the board.
     * 
     * Hides the last element of the snake's body,
     * until an apple is eaten.
     */
    public void updateAndDisplay() {
    	update();
    	display();
    }
    
    /**
     * Updates and displays the board.
     * 
     * @param grow 	Shows the last element of the snake's body if true.
     */
    public void updateAndDisplay(boolean grow) {
    	update(grow);
    	display();
    }
	
	
	/**
	 * Moves the snake one point towards the apple.
	 */
	public void moveSnake() {
		// keep track of previous location
		this.snake.addToBody(this.snake.head);
		
		// get the direction to the apple
		Direction direction = getDirectionToApple();
		
		// find the next point the snake should move to
		Point nextPoint = this.snake.getNextPoint(direction);
		
		// make sure the next point isn't it's own body or the border
		while (isSnake(nextPoint) || !inBounds(nextPoint)) {
			direction = direction.next();
			nextPoint = this.snake.getNextPoint(direction);
		}
				
		this.snake.moveTo(nextPoint);
	}
	 
	/**
	 * Gets the direction to the apple in relation to the snake.
	 * 
	 * @return Direction
	 */
	public Direction getDirectionToApple() {
		// get the snake's distance to the apple
		int x = (int) (this.apple.location.getX() - this.snake.head.getX());
		int y = (int) (this.snake.head.getY() - this.apple.location.getY());
		
		// if the apple is north to north east
		if (x >= 0 && y > 0) {
			return Direction.NORTH;
		// if the apple is east to south east
		} else if (x > 0 && y <= 0) {
			return Direction.EAST;
		// if the apple is south to south west
		} else if (x <= 0 && y < 0) {
			return Direction.SOUTH;
		// if the apple is west to north west
		} else if (x < 0 && y >= 0)  {
			return Direction.WEST;
		}
		
		return Direction.NORTH;
	 }
	
	/**
	 * Returns true if the snake is trapped.
	 * 
	 * @return boolean
	 */
	public boolean snakeIsTrapped() {
		// get snake's x and y coordinates
		int x = (int) this.snake.head.getX();
		int y = (int) this.snake.head.getY();
		
		// get all the possible points the snake could move to
		Point northPoint = new Point(x, y + 1);
		Point eastPoint = new Point(x + 1, y);
		Point southPoint = new Point(x, y - 1);
		Point westPoint = new Point(x - 1, y);
		
		// return true if all the points are not valid points to move to
		if (!isValid(northPoint) && !isValid(eastPoint) && !isValid(southPoint) && !isValid(westPoint)) {
			return true;
		}
		
		return false;
	}
	
	 /**
	  * Returns true if the given point is a valid point for the snake to move to.
	  * 
	  * @param Point p
	  * @return boolean
	  */
	 public boolean isValid(Point p) {
		 if (typeOf(p) == Type.BLANK || typeOf(p) == Type.APPLE) {
			return true;
		}
		return false;
	}
	
	 /**
	  * Returns true if the given point is occupied by the snake.
	  * 
	  * @param Point p
	  * @return boolean
	  */
	 public boolean isSnake(Point p) {
		 if (typeOf(p) == Type.SNAKE_HEAD || typeOf(p) == Type.SNAKE_BODY) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the given point is within the bounds of the board.
	 * 
	 * @param Point p
	 * @return boolean
	 */
	public boolean inBounds(Point p) {
		if (p.getX() > 0 && p.getX() < DIMENSION - 1 && p.getY() > 0 && p.getY() < DIMENSION - 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the point is the border of the board.
	 * 
	 * @param Point p
	 * @return boolean
	 */
	public boolean isBorder(Point p) {
		if (p.getX() == 0 || p.getX() == DIMENSION - 1 || p.getY() == 0 || p.getY() == DIMENSION - 1) { 
			return true;
		}
		return false;
	}
}
