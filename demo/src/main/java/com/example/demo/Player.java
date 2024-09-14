package com.example.demo;

/**
 * @author Calum Healy
 * Represents a player in the game
 */

public class Player {

    // Variables
    private int YSpeed = 1; // Vertical speed of the player
    private String Name = "Player One"; // Name of the player
    private int XSize = 5 * 5; // Width of the player
    private int YSize = 2 * 75; // Height of the player

    /**
     * This method returns the name of a given Player object
     * @return Name
     */

    public String getName() {
        return Name;
    }

    /**
     * This method sets a name for a given Player object
     * @param newName
     */

    public void setName(String newName) {
        this.Name = newName;
    }

    /**
     * This method returns the width of a given Player object
     * @return XSize
     */

    public int getXSize() {
        return XSize;
    }

    /**
     * This methods sets the width for a given Player object
     * @param XSize
     */

    public void setXSize(int XSize) {
        this.XSize = XSize * 5;
    }

    /**
     * This method returns the height of a given Player object
     * @return YSize
     */

    public int getYSize() {
        return YSize;
    }

    /**
     * This method sets the YSize for a given Player object
     * @param YSize
     */

    public void setYSize(int YSize) {
        this.YSize = YSize * 75;
    }

    /**
     * This methods returns the XSize of a given Player Object, but adjusting it so it is suitable for serialization
     * @return Adjusted XSize
     */

    public int getXSizeForSer() {
        return this.XSize / 5;
    }

    /**
     * This method returns the YSize of a given Player object, but adjusting it so it is suitable for serialization
     * @return
     */

    public int getYSizeForSer() {
        return this.YSize / 75;
    }
}
