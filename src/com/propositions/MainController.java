package com.propositions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
            alert.setTitle("Empty Input Error");
            alert.setContentText("Cannot parse an empty string. Try again!");
            alert.show();
        } else {
            // Attempt to parse the user input.
            PropositionInterpreter interp;
            try {
                interp = new PropositionInterpreter(propField.getText(), true);
            } catch (Exception error) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Interpreting Error");
                alert.setContentText("Error when interpreting string '" + propField.getText() + "': "
                    + error.toString());
                alert.show();
                return;
            }

            // Create the new HBox to put inside the ListView
            HBox prop = new HBox(25);
            Button remove = new Button("X");
            Label proposition = new Label(propField.getText());
            Label result = new Label(interp.truth_value ? "TRUE" : "FALSE");
            HBox.setHgrow(result, Priority.ALWAYS);

            remove.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent e) { props.remove(prop); }
            });

            prop.setAlignment(Pos.CENTER_LEFT);
            prop.getChildren().addAll(remove, proposition, new Label("->"), result);


            props.add(prop);
            propView.setItems(props);
        }
        propField.clear();
    }

    public void new_complex(ActionEvent e) {
        if (propField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Input Error");
            alert.setContentText("Cannot parse an empty string. Try again!");
            alert.show();
        }
        else {
            // Attempt to parse the user input.
            PropositionInterpreter interp;
            try {
                interp = new PropositionInterpreter(propField.getText(), false, false);
            } catch (Exception error) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Interpreting Error");
                alert.setContentText("Error when interpreting string '" + propField.getText() + "': "
                        + error.toString());
                alert.show();
                return;
            }

            // Create the new HBox to put inside the ListView
            HBox prop = new HBox(25);
            Button remove = new Button("X");
            remove.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent e) { props.remove(prop); }
            });
            Label proposition = new Label(propField.getText());

            // Create new text fields for each proposition
            HBox propvalues = new HBox(10);
            for (String i : interp.truthmaps.keySet()) {
                Label proplabel = new Label(i + ": ");
                TextField input = new TextField();
                input.setPromptText("\"" + i + "\"'s Truth Value");
                input.setId(i);
                propvalues.getChildren().add(proplabel);
                propvalues.getChildren().add(input);
            }
            propvalues.setAlignment(Pos.CENTER_LEFT);

            Label result = new Label(interp.truth_value ? "TRUE" : "FALSE");
            HBox.setHgrow(result, Priority.ALWAYS);
            prop.setAlignment(Pos.CENTER_LEFT);

            Button interpret = new Button("INTERPRET");
            interpret.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent e) {
                    // Iterate through, updating the hashmap as needed
                    for (String i : interp.truthmaps.keySet()) {
                        for (Node j : propvalues.getChildren()) {
                            if (j instanceof TextField) {
                                if ( ((TextField)j).getText().compareToIgnoreCase("t") == 0 ) {
                                    interp.truthmaps.replace(i, true);
                                } else if ( ((TextField)j).getText().compareToIgnoreCase("true") == 0 ) {
                                    interp.truthmaps.replace(i, true);
                                } else if ( ((TextField)j).getText().compareToIgnoreCase("f") == 0 ) {
                                    interp.truthmaps.replace(i, false);
                                } else if ( ((TextField)j).getText().compareToIgnoreCase("false") == 0 ) {
                                    interp.truthmaps.replace(i, false);
                                } else {
                                    Alert alert = new Alert(AlertType.ERROR);
                                    alert.setTitle("Interpreting Error");
                                    alert.setContentText("Error when interpreting '" + ((TextField)j).getText() + "': Not a valid input.");
                                    alert.show();
                                    return;
                                }
                            }
                        }
                    }
                    // Re-evaluate :)
                    interp.complex_eval();
                    result.setText(interp.truth_value ? "TRUE" : "FALSE");
                }
            });

            prop.getChildren().addAll(remove, proposition, propvalues, interpret, new Label("->"), result);

            props.add(prop);
            propView.setItems(props);
        }
        propField.clear();
    }

    public void insert_conjunction(ActionEvent e) {
        propField.setText(propField.getText() + "&");
    }

    public void insert_disjunction(ActionEvent e) {
        propField.setText(propField.getText() + "|");
    }

    public void insert_negation(ActionEvent e) {
        propField.setText(propField.getText() + "!");
    }

    public void insert_condition(ActionEvent e) {
        propField.setText(propField.getText() + "->");
    }

    public void insert_bicondition(ActionEvent e) {
        propField.setText(propField.getText() + "<->");
    }
}
