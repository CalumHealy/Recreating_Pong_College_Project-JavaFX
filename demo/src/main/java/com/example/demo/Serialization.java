package com.example.demo;

/**
 * @author Calum Healy
 * This class creates an object containing the settings options so they can be serialized and saved
 */

import java.io.Serializable;

public class Serialization implements Serializable{
    private static final long serialVersionUID = 1L;

    // Variables
    private String playerOneName; // Name of player one
    private String playerTwoName; // Name of player two
    private int ballSpeed; // Ball speed setting
    private int playerOneXSize; // Width of player one
    private int playerOneYSize; // Height of player one
    private int playerTwoXSize; // Width of player two
    private int playerTwoYSize; // Height of player two
    private int winningScore; // Number of points required to win game
    private int ballSpeedIncreaseNumber; // Number of bounces required to increase the speed of the ball

    // Getters

    /**
     * This method returns the name of player one
     * @return playerOneName
     */

    public String getPlayerOneName() {
        return this.playerOneName;
    }

    /**
     * This method returns the name of player two
     * @return playerTwoName
     */

    public String getPlayerTwoName() {
        return this.playerTwoName;
    }

    /**
     * This method returns the ball speed setting
     * @return ballSpeed
     */

    public int getBallSpeed() {
        return this.ballSpeed;
    }

    /**
     * This method returns the width of player one
     * @return playerOneXSize
     */

    public int getPlayerOneXSize() {
        return this.playerOneXSize;
    }

    /**
     * This method returns the height of player one
     * @return playerOneYSize
     */

    public int getPlayerOneYSize() {
        return this.playerOneYSize;
    }

    /**
     * This method returns the width of player two
     * @return playerTwoXSize
     */

    public int getPlayerTwoXSize() {
        return this.playerTwoXSize;
    }

    /**
     * This method returns the height of player two
     * @return playerTwoYSize
     */

    public int getPlayerTwoYSize() {
        return this.playerTwoYSize;
    }

    /**
     * This method returns the number of points required to win the game
     * @return winningScore
     */

    public int getWinningScore() {
        return this.winningScore;
    }

    /**
     * This method returns the number of bounces required to increase the speed of the ball
     * @return ballSpeedIncreaseNumber
     */

    public int getBallSpeedIncreaseNumber() {
        return this.ballSpeedIncreaseNumber;
    }

    // Setters

    /**
     * This method stores the name of player one for serialization
     * @param newNameOne
     */

    public void setPlayerOneName(String newNameOne) {
        this.playerOneName = newNameOne;
    }

    /**
     * This method stores the name of player two for serialization
     * @param newNameTwo
     */

    public void setPlayerTwoName(String newNameTwo) {
        this.playerTwoName = newNameTwo;
    }

    /**
     * This method stores the speed of the ball for serialization
     * @param newSpeed
     */

    public void setBallSpeed(int newSpeed) {
        this.ballSpeed = newSpeed;
    }

    /**
     * This method stores the width of player one for serialization
     * @param newXSizeOne
     */

    public void setPlayerOneXSize(int newXSizeOne) {
        this.playerOneXSize = newXSizeOne;
    }

    /**
     * This method stores the height of player one for serialization
     * @param newYSizeOne
     */

    public void setPlayerOneYSize(int newYSizeOne) {
        this.playerOneYSize = newYSizeOne;
    }

    /**
     * This method stores the width of player two for serialization
     * @param newXSizeTwo
     */

    public void setPlayerTwoXSize(int newXSizeTwo) {
        this.playerTwoXSize = newXSizeTwo;
    }

    /**
     * This method stores the height of player two for serialization
     * @param newYSizeTwo
     */

    public void setPlayerTwoYSize(int newYSizeTwo) {
        this.playerTwoYSize = newYSizeTwo;
    }

    /**
     * This method stores the number of points required to win the game for serialization
     * @param newWinningScore
     */

    public void setWinningScore(int newWinningScore) {
        this.winningScore = newWinningScore;
    }

    /**
     * This method stores the number of bounces required to increase the speed of the ball for serialization
     * @param newBounceNumber
     */

    public void setBallSpeedIncreaseNumber(int newBounceNumber) {
        this.ballSpeedIncreaseNumber = newBounceNumber;
    }
}
