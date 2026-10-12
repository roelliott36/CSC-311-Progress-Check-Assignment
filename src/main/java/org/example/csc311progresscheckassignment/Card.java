package org.example.csc311progresscheckassignment;

public class Card {

    private int value;
    private String imageLocation;

    public Card(int value, String imageLocation) {
        this.value = value;
        this.imageLocation = imageLocation;
    }

    public int getValue() {
        return value;
    }

    public String getImageLocation() {
        return imageLocation;
    }

}
