package com.propositions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class MainController {

    @FXML
    private ListView<Pane> propView = new ListView<>();
    private ObservableList<Pane> props = FXCollections.observableArrayList();

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


            HBox prop = new HBox(25);

            Label proposition = new Label(propField.getText());
            Label result = new Label();
            HBox.setHgrow(result, Priority.ALWAYS);

            Button remove = new Button("X");
            remove.setOnAction(new EventHandler<>() {

                @Override
                public void handle(ActionEvent e) {
                    props.remove(prop);
                }
            });


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




}
