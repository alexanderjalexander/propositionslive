package com.app;

import com.propositions.PropositionInterpreter;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;

public class PropositionsHandler {
    public static boolean new_simple(String prop_string, ObservableList<Pane> props) {
        PropositionInterpreter interp = new PropositionInterpreter(prop_string, true, false);

        FlowPane prop = new FlowPane();
        prop.setVgap(10); prop.setHgap(25);
        prop.setPrefWidth(Region.USE_COMPUTED_SIZE);
        prop.setPrefHeight(Region.USE_COMPUTED_SIZE);

        Button remove = new Button("X");
        Label proposition = new Label(prop_string);
        proposition.setStyle("-fx-font-style: italic;");

        Label result = new Label(interp.truth_value ? "TRUE" : "FALSE");
        result.setStyle("-fx-font-style: italic;");

        remove.setOnAction(e1 -> props.remove(prop));

        prop.setAlignment(Pos.CENTER_LEFT);
        prop.getChildren().addAll(remove, proposition, new Label("->"), result);

        props.add(prop);

        return interp.truth_value;
    }

    public static void new_complex(String proposition) {

    }


}
