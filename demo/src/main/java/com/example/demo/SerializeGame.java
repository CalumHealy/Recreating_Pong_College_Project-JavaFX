package com.example.demo;

public class SerializeGame {
    private Vector ballPosition = new Vector(); // The position of the ball
    private int playerOneYPosition; // The position of player one
    private int playerTwoYPosition; // The position of player two
    private int playerOneScore; // Current player one score
    private int playerTwoScore; // Current player two score


    // Getters

    /**
     * This method returns the position of the ball
     * @return ballPosition
     */

    public Vector getBallPosition() {
        return this.ballPosition;
    }

    /**
     * This method returns the Y position of player one
     */

    public int getPlayerOneYPosition() {
        return this.playerOneYPosition;
    }

    /**
     * This method returns the Y position of player two
     */

    public int getPlayerTwoYPosition() {
        return this.playerTwoYPosition;
    }

    /**
     * This method returns player one's score
     */

    public int getPlayerOneScore() {
        return this.playerOneScore;
    }

    /**
     * This method returns player two's score
     */

    public int getPlayerTwoScore() {
        return this.playerTwoScore;
    }

    // Setters

    /**
     * This method sets the ball position for serialization and saving
     */

    public void setBallPosition(Vector vector) {
        this.ballPosition = vector;
    }
}
