package com.app;

import com.propositions.PropositionInterpreter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Objects;

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

    public static boolean new_complex(String prop_string,
                                      ObservableList<Pane> props,
                                      ObservableList<Pane> propConsoleEntries,
                                      ListView<Pane> propConsole) {
        PropositionInterpreter interp = new PropositionInterpreter(prop_string, false, false);

        // Create the new HBox to put inside the ListView
        HBox prop = new HBox(25);
        Button remove = new Button("X");
        remove.setOnAction(e1 -> props.remove(prop));
        Label proposition = new Label(prop_string);
        proposition.setStyle("-fx-font-style: italic;");

        Label result = new Label("");
        result.setStyle("-fx-font-style: italic;");

        VBox propvalues = new VBox(10);
        for (String i : interp.truthmaps.keySet()) {
            HBox propvalue = new HBox(10);

            Label proplabel = new Label(i + ": ");
            ToggleGroup radios = new ToggleGroup();
            RadioButton true_button = new RadioButton("True");
            true_button.setId(i);
            true_button.setToggleGroup(radios);
            true_button.setSelected(true);
            true_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // Update the truth value of proposition
                    interp.truthmaps.replace(i, true_button.isSelected());

                    // Print out each prop and truth values to system console.
                    consoleprintln("Updating Truth Value of Proposition: " + interp.parse, propConsoleEntries, propConsole);
                    consoleprintln("\t" + i + "\t" + interp.truthmaps.get(i), propConsoleEntries, propConsole);
                    consoleprintln("\tNew Resulting Truth Value: " + (interp.truth_value ? "TRUE" : "FALSE"), propConsoleEntries, propConsole);

                    // Re-evaluate
                    interp.complex_eval();
                    result.setText(interp.truth_value ? "TRUE" : "FALSE");
                }
            });

            RadioButton false_button = new RadioButton("False");
            false_button.setToggleGroup(radios);
            false_button.setId(i);
            false_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // Update the truth value of proposition
                    interp.truthmaps.replace(i, !false_button.isSelected());

                    // Print out each prop and truth values to system console.
                    consoleprintln("Updating Truth Value of Proposition: " + interp.parse, propConsoleEntries, propConsole);
                    consoleprintln("\t" + i + "\t" + interp.truthmaps.get(i), propConsoleEntries, propConsole);
                    consoleprintln("\tNew Resulting Truth Value: " + (interp.truth_value ? "TRUE" : "FALSE"), propConsoleEntries, propConsole);
                    // Re-evaluate :)
                    interp.complex_eval();
                    result.setText(interp.truth_value ? "TRUE" : "FALSE");
                }
            });

            propvalue.getChildren().addAll(proplabel, true_button, false_button);
            propvalues.getChildren().add(propvalue);
        }
        propvalues.setAlignment(Pos.CENTER_LEFT);

        VBox.setVgrow(result, Priority.ALWAYS);
        prop.setAlignment(Pos.CENTER_LEFT);

        // Iterate through, updating the hashmap as needed
        for (String i : interp.truthmaps.keySet()) {
            for (Node j : propvalues.getChildren()) {
                for (Node k : ((HBox) j).getChildren()) {
                    if ((Objects.equals(k.getId(), i)) && (k instanceof RadioButton)) {
                        if (((RadioButton) k).getText().compareToIgnoreCase("True") == 0) {
                            interp.truthmaps.replace(i, ((RadioButton) k).isSelected());
                        } else {
                            interp.truthmaps.replace(i, !((RadioButton) k).isSelected());
                        }
                    }
                }
            }
        }

        for (String i : interp.truthmaps.keySet()) {
            System.out.println(i + "\t" + interp.truthmaps.get(i));
        }

        // Re-evaluate :)
        interp.complex_eval();
        result.setText(interp.truth_value ? "TRUE" : "FALSE");

        // Add all elements to the proposition
        prop.getChildren().addAll(remove, proposition, propvalues, new Label("->"), result);

        // Add it to the list view & update it
        props.add(prop);

        return interp.truth_value;
    }

    private static void consoleprintln(String s,
                                       ObservableList<Pane> propConsoleEntries,
                                       ListView<Pane> propConsole){
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
}
