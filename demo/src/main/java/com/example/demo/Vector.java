package com.example.demo;

/**
 * @author Calum Healy
 * Creates vectors for storing velocities
 */

public class Vector {

    // Variables
    float x;
    float y;

    /**
     * This method sets new X and Y values for a given Vector object
     * @param x
     * @param y
     */
    public void setVector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method returns the X value of a given Vector object
     * @return x
     */
    public float getVectorX() {
        return this.x;
    }

    /**
     * This method returns the Y value of a given Vector object
     * @return y
     */
    public float getVectorY() {
        return this.y;
    }
}
