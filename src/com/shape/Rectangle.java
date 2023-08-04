package com.shape;

import javafx.scene.paint.Color;

public final class Rectangle {

    // Rectangles primarily used only for literals

    private final int width = 50;
    public int getWidth() {
        return width;
    }

    private final int height = 25;
    public int getHeight() {
        return height;
    }

    private final int arcWidth = 10, arcHeight = 10;

    private final javafx.scene.shape.Rectangle rectangle;
    private final String text;
    private final String value;

    public Rectangle(String text, String value) {
        rectangle = new javafx.scene.shape.Rectangle(width, height);
        rectangle.setArcWidth(arcWidth); rectangle.setArcHeight(arcHeight);
        rectangle.setFill(Color.rgb(0,0,0,0));
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);

        this.text = text; this.value = value;

    }
}
