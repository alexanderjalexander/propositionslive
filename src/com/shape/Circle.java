package com.shape;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Circle {
    // Rectangles primarily used only for literals

    private final int radius = 10;
    public int getRadius() {
        return radius;
    }

    private final javafx.scene.shape.Circle circle;
    private final Text text;
    private final Text value;

    public Circle(String text, String value) {
        circle = new javafx.scene.shape.Circle(10);
        circle.setFill(Color.rgb(0,0,0,0));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);

        this.text = new Text(text); this.value = new Text(value);

    }
}
