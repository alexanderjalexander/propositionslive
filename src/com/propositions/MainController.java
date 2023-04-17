package com.propositions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

public class MainController {

    @FXML
    private ListView<Pane> propView = new ListView<>();
    private ObservableList<Pane> props = FXCollections.observableArrayList();;

    @FXML
    private TextField propField;


    public void new_simple(ActionEvent e) {
        if (propField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot parse an empty string. Try again!");
            alert.show();
        } else {

            // Attempt to parse the user input.



            Label proposition = new Label(propField.getText());
            Label result = new Label();
            HBox.setHgrow(result, Priority.ALWAYS);
            Button remove = new Button("X");

            HBox prop = new HBox(25);
            prop.setAlignment(Pos.CENTER_LEFT);
            prop.getChildren().addAll(remove, proposition, new Label("->"), result);


            props.add(prop);
            propView.setItems(props);
        }
        propField.clear();
    }

    public void new_complex(ActionEvent e) {
        /*if (propField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot parse an empty string. Try again!");
            alert.show();
        } else {
            names.add(new Button());
            propView.setItems(names);
        }
        propField.clear(); */
    }

    public void remove_prop(ActionEvent e) {

    }


}
