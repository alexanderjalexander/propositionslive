package com.app;

import com.propositions.PropositionInterpreter;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class TruthTableHandler {
    public static void clear_truth_table(TableView<List<String>> truth_table) {
        truth_table.getColumns().clear();
        truth_table.getItems().clear();
    }

    public static void new_truth_table(TableView<List<String>> truth_table, TextField truthField) {
        // Clear Truth Table
        clear_truth_table(truth_table);

        truth_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create new proposition interp and attempt to evaluate
        PropositionInterpreter interp;
        interp = new PropositionInterpreter(truthField.getText(), false, false);


        // Create two arrays, one with the names, one with the truth values
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Boolean> values = new ArrayList<>();
        ArrayList<ArrayList<Boolean>> results = new ArrayList<>();

        // The results string will hold the columns of the truth values
        // Thus, it will look something like this
        // {
        //  "P"      [T,T,F,F]
        //  "Q"      [T,F,T,F]
        //  "P & Q"  [T,F,F,F]
        // }

        // For every proposition within the hashmap provided, create new columns in the table
        for (String i : interp.truthmaps.keySet()) {
            // Also set it to true
            interp.truthmaps.replace(i, true);

            // Add the names to the values
            names.add(i);
            values.add(interp.truthmaps.get(i));

            // Initiate results with "columns" representing each prop
            results.add(new ArrayList<>());
        }

        // Add another row for the result column's rows
        results.add(new ArrayList<>());

        // Calculate every possible value using the new values
        do {
            for (int i = 0; i < names.size(); i++) {
                interp.truthmaps.replace(names.get(i), values.get(i));
                results.get(i).add(values.get(i));
            }
            interp.complex_eval();
            results.get(results.size() - 1).add(interp.truth_value);
        } while(truth_next_assignment(values));

        // Name Column Creation
        for (int i = 0; i <= names.size(); i++) {
            if (i < names.size()) {
                // Create the new column
                TableColumn<List<String>, String> col = new TableColumn<>(names.get(i));
                col.setSortable(false);
                final int temp = i;
                col.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(temp)));
                truth_table.getColumns().add(col);
                System.out.println("Column with name \"" + names.get(i) + "\" added. Width = " + col.getWidth());
            } else {
                // Create a final column for the proposition as a whole
                TableColumn<List<String>, String> col = new TableColumn<>(truthField.getText());
                col.setSortable(false);
                final int temp = i;
                col.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get( temp )));
                truth_table.getColumns().add(col);
                System.out.println("Column with name \"" + truthField.getText() + "\" added. Width = " + col.getWidth());
            }
        }

        // Add data to rows one by one
        for (int i = 0; i < results.get(0).size(); i++) {
            ArrayList<String> row = new ArrayList<>();
            for (int j = 0; j < truth_table.getColumns().size(); j++) {
                row.add(results.get(j).get(i) ? "T" : "F");
            }
            truth_table.getItems().add(row);
        }
    }

    private static boolean truth_next_assignment(ArrayList<Boolean> values) {
        // Walking from Right to Left, search for a True to make False
        int flipIndex = values.size() - 1;
        while ((flipIndex >= 0) && !values.get(flipIndex)) { flipIndex--; }

        // If none found, return false and finish, none found to flip
        if (flipIndex == -1) return false;

        /* Otherwise, flip this index to true and all following values to false. */
        values.set(flipIndex, false);

        for (int i = flipIndex + 1; i < values.size(); i++) {
            values.set(i, true);
        }

        return true;
    }
}
