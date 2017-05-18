// G Roques E01
package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Point;

import application.Board;

public class Game extends Application {
	public Stage primaryStage;
	public Group root;
	public Board board;
	public Scene scene;
	
	public enum GameType {
		USER_CHOICE, RANDOM_PICK
	};
	
	public GameType gameType;
	
	// The speed at which the snake moves to the apple (in milliseconds)
	private final int ANIMATION_SPEED = 600;

	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Place apples and feed your snake");
		initializeGame(primaryStage);
	}
	
	/**
	 * Initialize the game with JavaFX elements and the board,
	 * then display.
	 */
	public void initializeGame(Stage primaryStage) {
	    root = new Group();
	    board = new Board();
	    scene = new Scene(root);
	    getGameType();
	    
	    // if game type is user choice, then listen for mouse clicks on the scene
	    if (gameType == GameType.USER_CHOICE) addClickHandlerToScene();
	    
	    // if game type is random pick, then listen for when the user presses the space bar
	    if (gameType == GameType.RANDOM_PICK) addKeyPressHandlerToScene();
	    
	    board.display();
	    
	    // set the root node to the canvas
	    root.getChildren().add(board.getCanvas());
	    
	    // show the stage
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	/**
	 * Displays a confirmation window asking the user to pick a game type.
	 */
	public void getGameType() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select A Game Type");
		alert.setHeaderText("Choose User Choice or Random Pick");
		alert.setContentText("Place apples by clicking in user choice, or by pressing space in random pick.");

		ButtonType userChoiceButton = new ButtonType("User Choice");
		ButtonType randomPickButton = new ButtonType("Random Pick");
		ButtonType cancelButton = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(userChoiceButton, randomPickButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == userChoiceButton) {
			gameType = GameType.USER_CHOICE;
		} else if (result.get() == randomPickButton) {
			gameType = GameType.RANDOM_PICK;
		} else {
			Platform.exit();
		}
	}
	
	/**
	 * Listen for mouse clicks on the scene.
	 */
	public void addClickHandlerToScene() {
	    scene.addEventFilter(MouseEvent.MOUSE_PRESSED, handleClick());
	}
	
	/**
	 * Place an apple on the board when clicked.
	 * 
	 * @return EventHandler<MouseEvent>
	 */
	public EventHandler<MouseEvent> handleClick() {
		return new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent mouseEvent) {
	        	// prevent placing two apples
	    	    if (!Apple.placed) {
	    	    	// determine where the user clicked
	    	    	Point click = getClickLocation(mouseEvent);
	            	
	            	// make sure the apple is within the bounds of the board and not on the snake
	            	if (!board.inBounds(click) || board.isSnake(click)) return;
	            	
	            	// place the apple where the user clicked
	            	placeApple(click);
	            }
	        }
		};
	}
	
	/**
	 * Get the point on the board where the user clicked
	 * 
	 * @param mouseEvent
	 * @return Point
	 */
	public Point getClickLocation(MouseEvent mouseEvent) {
		double canvasWidth = board.getCanvas().getWidth();
    	int x = (int) ((mouseEvent.getX() / canvasWidth) * board.dimension());
    	int y = (int) ((mouseEvent.getY() / canvasWidth) * board.dimension());
    	return new Point(x, y);
	}
	
	
	/**
	 * Listen for key presses on the scene.
	 */
	public void addKeyPressHandlerToScene() {
		scene.setOnKeyPressed(handleKeyPress());
	}
	
	/**
	 * Places an apple on the board randomly when a user presses space.
	 * 
	 * @return EventHandler<KeyEvent>
	 */
	public EventHandler<KeyEvent> handleKeyPress() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE && !Apple.placed) {
					placeRandomApple();
				}
			}
		};
	}
	
	/**
	 * Generates a random point on the board to place the apple.
	 * 
	 * Makes sure the point is within the bounds of the board and
	 * is not on the snake.
	 */
	public void placeRandomApple() {
		// generate a randomly valid point
		Point point;
		do {
			Random generator = new Random();
			int x = generator.nextInt(board.dimension());
			int y = generator.nextInt(board.dimension());
			point = new Point(x, y);
		} while (!board.inBounds(point) || board.isSnake(point));
		
		// place the apple at the point
		placeApple(point);
	}

	/**
	 * Places an apple on a point within the board.
	 * 
	 * @param location 	A point on the board
	 */
	public void placeApple(Point location) {
		// place apple on board
    	board.apple().place(location);
    	
    	// update and display the board
    	board.updateAndDisplay(true);
    	
    	// if the snake is trapped, then alert the user they lost
    	if (board.snakeIsTrapped()) createLoseAlert();
    	
    	// move the snake to the apple
    	moveSnakeToApple();
	}
	
	/**
	 * Alert the user they lost, then exit.
	 */
	public void createLoseAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sorry!");
		alert.setHeaderText("You lose!");
		alert.setContentText("Your snake got trapped! Better luck next time.");

		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				Platform.exit();
		    }
		});
	}
	
	/**
	 * Creates a timer that moves the snake closer to the apple each interval.
	 */
	public void moveSnakeToApple() {
    	Timer timer = new Timer();
    	TimerTask moveSnakeToApple = new MoveSnakeToApple(timer, board);	            	
    	timer.scheduleAtFixedRate(moveSnakeToApple, ANIMATION_SPEED/2, ANIMATION_SPEED/2);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
