package application;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class MoveSnakeToApple extends TimerTask {
	public Timer timer;
	public Board board;
	public Snake snake;
	public Apple apple;
	
	MoveSnakeToApple(Timer timer, Board board) {
		this.timer = timer;
		this.board = board;
		this.snake = board.snake();
		this.apple = board.apple();
	}
	
	/**
	 * Moves the snake to the apple,
	 * and eats the apple once there.
	 */
	public void run() {
		this.board.moveSnake();
		this.board.updateAndDisplay();
    	
		// if the snake reaches the apple's location
    	if (this.snake.head.equals(this.apple.location)) {
    		// cancel the timer
    		cancelTimer();
    		
    		// eat the apple
    		this.snake.eatApple();
    		Apple.placed = false;
    		
    		// update and display the board
    		this.board.updateAndDisplay(true);
    		
    		// check to see if the user won
    		checkForWin();
    	}
	}

	/**
	 * Cancels the timer.
	 */
	public void cancelTimer() {
		this.timer.cancel();
		this.timer.purge();
	}
	
	
	/**
	 * If snake length exceeds 10, then alert the user they won.
	 * 
	 * Use Platform.runLater() to execute the alert on the JavaFX thread,
	 * instead of the Timer thread.
	 */
	public void checkForWin() {
		if (this.board.snake().length > 10) {
			Platform.runLater(() -> createWinAlert());
		}
	}
	
	/**
	 * Alert the user they won, then exit.
	 */
	public void createWinAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("You Win!");
		alert.setHeaderText("You Win!");
		alert.setContentText("Congratulations! Your snake grew to length " + this.board.snake().length + "!");
		
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				Platform.exit();
		    }
		});
	}
}
