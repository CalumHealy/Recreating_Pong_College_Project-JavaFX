package com.example.demo;

/**
 * @author Calum Healy
 * Ball objects is a "physical" component of the game which interacts with the player rackets
 */

public class Ball {

    // Variables
    private int straightSpeed = 3; // Speed of the ball in the direction it is travelling
    private Vector vectorSpeed; // A vector object containing the X and Y speeds
    private int Radius = 1; // The radius of the ball
    private int XPosition; // The position of the ball on the X axis
    private int YPosition; // The position of the ball on the Y axis

    // Speed getter and setter

    /**
     * This method returns the straightSpeed of the ball
     * @return straightSpeed
     */

    public int getStraightSpeed() {
        return straightSpeed;
    }

    /**
     * This method sets the straightSpeed of the ball
     * @param straightSpeed
     */

    public void setStraightSpeed(int straightSpeed) {
        this.straightSpeed = straightSpeed;
    }

    /**
     * This method is used to initialize the vector speed of the ball, setting both values to 1 so the ball travels at a 45 degree angle to the ground
     * @param x
     * @param y
     */

    public void initializeVectorSpeed(int x, int y){
        vectorSpeed = new Vector();
        vectorSpeed.setVector(x, y);
    }

    /**
     * This method is used to update the vector speed, used in collision with the top and bottom of the game scene
     * @param x
     * @param y
     */

    public void setVectorSpeed(int x, int y){
        vectorSpeed.setVector(x, y);
    }

    /**
     * This method returns the vectorSpeed of the ball
     * @return vectorSpeed
     */

    public Vector getVectorSpeed() {
        return this.vectorSpeed;
    }

    /**
     * This method returns just the X value of the vectorSpeed of the ball
     * @return vectorSpeed.X
     */

    public float getVectorSpeedX() {
        return vectorSpeed.getVectorX();
    }

    /**
     * This method returns just the Y value of the vectorSpeed of the ball
     * @return vectorSpeed.Y
     */

    public float getVectorSpeedY() {
        return vectorSpeed.getVectorY();
    }

    // Position getters and setters

    /**
     * This method returns the position of the ball on the X axis
     * @return XPosition
     */

    public int getXPosition() {
        return XPosition;
    }

    /**
     * This method sets the position of the ball on the X axis
     * @param XPosition
     */

    public void setXPosition(int XPosition) {
        this.XPosition = XPosition;
    }

    /**
     * This method returns the position of the ball on the Y axis
     * @return YPositon
     */

    public int getYPosition() {
        return YPosition;
    }

    /**
     * This method sets the position of the ball on the Y axis
     * @param YPosition
     */

    public void setYPosition(int YPosition) {
        this.YPosition = YPosition;
    }

    // Radius getter and setter

    /**
     * This method returns the radius of the ball
     * @return Radius
     */

    public int getRadius() {
        return this.Radius;
    }

    /**
     * This method sets the radius of the ball
     * @param Radius
     */

    public void setRadius(int Radius) {
        this.Radius = Radius;
    }

}
