package com.example.demo;

/**
 * @author Calum Healy
 * Window object used for setting and referencing size for positioning of objects
 */

public class Window {

    // Variables
    private double windowWidth = 800; // Width in pixels of the window
    private double windowHeight = 600; // Height in pixels of the window

    /**
     * This method returns the width double value of the window
     * @return windowWidth
     */

    public double getWidth() {
        return this.windowWidth;
    }

    /**
     * This method returns the height double value of the window
     * @return windowHeight
     */

    public double getHeight() {
        return this.windowHeight;
    }

    /**
     * This method sets the width value of the window
     * @param windowWidth
     */

    public void setWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    /**
     * This method sets the height value of the window
     * @param windowHeight
     */

    public void setHeight(double windowHeight) {
        this.windowHeight = windowHeight;
    }
}
