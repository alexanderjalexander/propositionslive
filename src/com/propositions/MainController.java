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
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainController {

    // Set up proposition viewport
    @FXML
    private ListView<Pane> propView = new ListView<>();
    private ObservableList<Pane> props = FXCollections.observableArrayList();

    @FXML
    private TextField propField;

    // Set up proposition console viewport.
    @FXML
    private ListView<Pane> propConsole = new ListView<>();
    private ObservableList<Pane> propConsoleEntries = FXCollections.observableArrayList();

    public void consoleprintln(String s){
        System.out.println(s);

        // Create the text to print out.
        FlowPane text = new FlowPane();
        Label print = new Label(s);
        print.setWrapText(true);
        text.getChildren().add(print);

        // Add it to the list view & update it
        propConsoleEntries.add(text);
        propConsole.setItems(propConsoleEntries);
    }

    public void consoleerrorln(String s){
        System.err.println(s);

        // Create the text to print out.
        FlowPane text = new FlowPane();
        Label err = new Label(s);
        err.setWrapText(true);
        err.setTextFill(Color.RED);
        text.getChildren().add(err);

        // Add it to the list view & update it
        propConsoleEntries.add(text);
        propConsole.setItems(propConsoleEntries);
    }

    public void new_simple(ActionEvent e) {
        // If empty, do not parse. Warn user.
        if (propField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Input Error");
            alert.setContentText("Cannot parse an empty string. Try again!");
            alert.show();
            consoleerrorln("Error: Cannot parse an empty string.\nStopping interpretation sequence.");
        } else {
            // Attempt to parse the user input.
            PropositionInterpreter interp;
            try {
                interp = new PropositionInterpreter(propField.getText(), true);
            } catch (Exception error) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Interpreting Error");
                alert.setContentText("Error when interpreting string '" + propField.getText() + "': \n\"" + error.toString() + "\"");
                alert.show();
                consoleerrorln("Error: Problem when interpreting string '" + propField.getText() + "': \n\"" + error.toString() + "\"");
                consoleerrorln("Stopping interpretation sequence.");
                return;
            }

            // Print out for debugging purposes.
            consoleprintln("Interpreted \"" + propField.getText() + "\" as: " + interp.parse + "\tMode: Simple.");

            // Create the new HBox to put inside the ListView
            FlowPane prop = new FlowPane();
            prop.setVgap(10);
            prop.setHgap(25);
            prop.setPrefWidth(Region.USE_COMPUTED_SIZE);
            prop.setPrefWrapLength(Region.USE_COMPUTED_SIZE);
            Button remove = new Button("X");
            Label proposition = new Label(propField.getText());
            Label result = new Label(interp.truth_value ? "TRUE" : "FALSE");

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
        // If it's empty, do not parse. Warn user.
        if (propField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Input Error");
            alert.setContentText("Cannot parse an empty string. Try again!");
            alert.show();
            consoleerrorln("Error: Cannot parse an empty string.\nStopping interpretation sequence.");
        }
        else {
            // Attempt to parse the user input.
            PropositionInterpreter interp;
            try {
                interp = new PropositionInterpreter(propField.getText(), false, false);
            } catch (Exception error) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Interpreting Error");
                alert.setContentText("Error when interpreting string '" + propField.getText() + "': \n\"" + error.toString() + "\"");
                alert.show();
                consoleerrorln("Error: Problem when interpreting string '" + propField.getText() + "': \n\"" + error.toString() + "\"");
                consoleerrorln("Stopping interpretation sequence.");
                return;
            }

            // Print out tree, debugging purposes
            consoleprintln("Interpreted \"" + propField.getText() + "\" as: " + interp.parse + "\tMode: Complex.");

            // Create the new HBox to put inside the ListView
            HBox prop = new HBox(25);
            Button remove = new Button("X");
            remove.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent e) { props.remove(prop); }
            });
            Label proposition = new Label(propField.getText());

            // Create new text fields for each proposition
            VBox propvalues = new VBox(10);
            for (String i : interp.truthmaps.keySet()) {
                Label proplabel = new Label(i + ": ");
                TextField input = new TextField();
                input.setPromptText("\"" + i + "\"'s Truth Value");
                input.setId(i);

                propvalues.getChildren().add(proplabel);
                propvalues.getChildren().add(input);
            }
            propvalues.setAlignment(Pos.CENTER_LEFT);

            Label result = new Label("");
            VBox.setVgrow(result, Priority.ALWAYS);
            prop.setAlignment(Pos.CENTER_LEFT);

            Button interpret = new Button("INTERPRET");
            interpret.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent e) {
                    // Iterate through, updating the hashmap as needed
                    for (String i : interp.truthmaps.keySet()) {
                        for (Node j : propvalues.getChildren()) {
                            if ((j.getId() == i) && (j instanceof TextField)) {
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

                    // Print out each prop and truth values to system console.
                    consoleprintln("Updating Truth Values of Proposition: " + interp.parse);
                    for (String i : interp.truthmaps.keySet()) {
                        consoleprintln(i + "\t" + interp.truthmaps.get(i));
                    }

                    // Re-evaluate :)
                    interp.complex_eval();
                    result.setText(interp.truth_value ? "TRUE" : "FALSE");
                }
            });

            // Add all elements to the proposition
            prop.getChildren().addAll(remove, proposition, propvalues, interpret, new Label("->"), result);

            // Add it to the list view & update it
            props.add(prop);
            propView.setItems(props);
        }
        // Clear user-input field.
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
