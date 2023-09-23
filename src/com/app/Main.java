package com.app;

import com.propositions.PropositionInterpreter;
import com.propositions.PropositionParser;
import javafx.application.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import atlantafx.base.theme.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Initializing Propositions LIVE...");
        stage.setMinWidth(760);
        stage.setMinHeight(600);
        stage.setTitle("Propositions LIVE");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));

        // Attempt to select tab
//        TabPane tabs_pane = (TabPane) root.lookup("#tabs");
//        System.out.println("tabs = " + tabs_pane.toString());


        // Override Default Exception Handler
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            // Convert Stack Trace to readable message
            StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);

            // Create StackTrace Area
            TextArea exc = new TextArea(sw.toString());
            exc.setEditable(false); exc.setWrapText(false);
            exc.setMaxWidth(Double.MAX_VALUE); exc.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(exc, Priority.ALWAYS); GridPane.setHgrow(exc, Priority.ALWAYS);

            // Create Content
            GridPane content = new GridPane();
            content.setMaxWidth(Double.MAX_VALUE);
            content.add(new Label("Full Stacktrace: "), 0, 0);
            content.add(exc, 0, 1);

            // Create the alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("A runtime exception occurred");
            alert.setContentText("""
                    The following run-time exception occurred while trying to run your program.

                    "If the problem persists, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues""");
            alert.getDialogPane().setExpandableContent(content);
            alert.setHeight(400); alert.setWidth(200); alert.setResizable(true);
            alert.show();

            // Print error to user-console
            consoleerrorln("Handler caught exception: " + throwable + "\n" + sw);
        });

        // Handle user properties
        MainProperties.init();

        // Add cell resizing back to the stylesheets.
        if (MainProperties.INSTANCE.getDarkMode()) {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            root.getStylesheets().add(Styles.toDataURI("""
                #propView .list-cell {
                    -fx-cell-size: -1;
                }
                #propConsole .list-cell {
                    -fx-cell-size: -1;
                }
                """));
        }
        else {
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
            root.getStylesheets().add(Styles.toDataURI("""
                #propView .list-cell {
                    -fx-cell-size: -1;
                }
                #propConsole .list-cell {
                    -fx-cell-size: -1;
                }
                """));
        }

//        System.out.println("TabPane State: " + tabs);

        propView.setId("propView");
        propConsole.setId("propConsole");

        System.out.println("Starting stage...");
        System.out.println("-----------------");

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() {
        System.out.println("-----------------");
        MainProperties.INSTANCE.exit();
        System.out.println("Exiting...");
    }

    // ---------------------------------------------------------

    // Set up proposition viewport
    @FXML
    private ListView<Pane> propView = new ListView<>();
    private final ObservableList<Pane> props = FXCollections.observableArrayList();

    // Set up text fields
    @FXML
    private TextField propField;
    @FXML
    private TextField truthField;

    // Set up TabPane
    @FXML
    private Tab propositions, truth_tables;
    @FXML
    private TabPane tabs;

    // Setting up clipboard parameters
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Set up proposition console viewport.
    @FXML
    private ListView<Pane> propConsole = new ListView<>();
    private final ObservableList<Pane> propConsoleEntries = FXCollections.observableArrayList();

    @FXML
    private MenuItem darkModeMenu;

    @FXML
    private TableView<List<String>> truth_table = new TableView<>();


    public void userAlert(String title, String message, Exception error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message + ":\n\"" + error + "\"");
        alert.setResizable(true);
        alert.show();

        consoleerrorln(message + "\n\"" + error + "\"");
    }

    public void update_darkmode_menu() {
        if (MainProperties.INSTANCE.getDarkMode()) {
            darkModeMenu.setText("Light Mode");
        } else {
            darkModeMenu.setText("Dark Mode");
        }
    }

    public void darkmode() {
        if (MainProperties.INSTANCE.getDarkMode()) {
            MainProperties.INSTANCE.setDarkMode(false);
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        } else {
            MainProperties.INSTANCE.setDarkMode(true);
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        }
    }

    /**
     * Copies current proposition from propField to the user's clipboard.'
     */
    public void copyPropField() {
        StringSelection data = new StringSelection(propField.getText());
        clipboard.setContents(data, data);
    }
    /**
     * Cuts current proposition from propField to the user's clipboard.'
     */
    public void cutPropField() {
        StringSelection data = new StringSelection(propField.getText());
        clipboard.setContents(data, data);
        propField.clear();
    }

    /**
     * Pastes current proposition to the user's clipboard.'
     */
    public void pastePropField() {
        try {
            Transferable t = clipboard.getContents(null);
            if (t != null) {
                if (t.isDataFlavorSupported(DataFlavor.stringFlavor))
                    propField.setText(propField.getText() + t.getTransferData(DataFlavor.stringFlavor));
            }
        } catch (Exception error) {
            userAlert("Paste Error", "Error when attempting to paste" +
                    "\nIf the problem persists, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues\"",
                    error);
        }
    }

    public void about() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://github.com/alexanderjalexander/propositionslive"));
            }
        } catch (Exception error) {
            userAlert("Website Link Error", "Error when attempting to open up link 'https://github.com/alexanderjalexander/propositionslive'", error);
        }
    }

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

    public void new_simple() {
        // If empty, do not parse. Warn user.
        if (propField.getText().isEmpty()) {
            userAlert("Empty Input Error", "Cannot parse an empty string. Try again!", new IOException("Empty Input is Invalid."));
        } else {
            // Attempt to parse the user input.
            PropositionInterpreter interp;
            try {
                interp = new PropositionInterpreter(propField.getText(), true, false);
            } catch (PropositionParser.ParseError error) {
                userAlert("Interpreting Error",
                        ("Error when interpreting string '" + propField.getText() + "':"
                                + "\nPlease check your proposition and try again."
                                + "\nIf you believe this is an error, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues"),
                        error);
                return;
            } catch (IllegalArgumentException error) {
                userAlert("Interpreting Error",
                        ("Error when interpreting string '" + propField.getText() + "':"
                                + "\n" + error.getMessage()
                                + "\nIf you believe this is an error, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues"),
                        error);
                return;
            }

            // Print out for debugging purposes.
            consoleprintln("Interpreted \"" + propField.getText() + "\" as: " + interp.parse + "\tMode: Simple.");

            // Create the new HBox to put inside the ListView
            FlowPane prop = new FlowPane();
            prop.setVgap(10); prop.setHgap(25);
            prop.setPrefWidth(Region.USE_COMPUTED_SIZE);
            prop.setPrefHeight(Region.USE_COMPUTED_SIZE);

            Button remove = new Button("X");
            Label proposition = new Label(propField.getText());
            proposition.setStyle("-fx-font-style: italic;");

            Label result = new Label(interp.truth_value ? "TRUE" : "FALSE");
            result.setStyle("-fx-font-style: italic;");

            remove.setOnAction(e1 -> props.remove(prop));

            prop.setAlignment(Pos.CENTER_LEFT);
            prop.getChildren().addAll(remove, proposition, new Label("->"), result);

            props.add(prop);
            propView.setItems(props);
        }
        propField.clear();
    }

    public void new_complex() {
        // If it's empty, do not parse. Warn user.
        if (propField.getText().isEmpty()) {
            userAlert("Empty Input Error", "Cannot parse an empty string. Try again!", new IOException("Empty Input is Invalid."));
        }
        else {
            // Attempt to parse the user input.
            PropositionInterpreter interp;
            try {
                interp = new PropositionInterpreter(propField.getText(), false, false);
            } catch (PropositionParser.ParseError error) {
                userAlert("Interpreting Error",
                        ("Error when interpreting string '" + propField.getText() + "':"
                        + "\nPlease check your proposition and try again."
                        + "\nIf you believe this is an error, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues"),
                        error);
                return;
            }

            // Print out tree, debugging purposes
            consoleprintln("Interpreted \"" + propField.getText() + "\" as: " + interp.parse + "\tMode: Complex.");

            // Create the new HBox to put inside the ListView
            HBox prop = new HBox(25);
            Button remove = new Button("X");
            remove.setOnAction(e1 -> props.remove(prop));
            Label proposition = new Label(propField.getText());
            proposition.setStyle("-fx-font-style: italic;");

            Label result = new Label("");
            result.setStyle("-fx-font-style: italic;");

            // Times New Roman

            // Create radio buttons for each proposition
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
                        consoleprintln("Updating Truth Value of Proposition: " + interp.parse);
                        consoleprintln("\t"+ i + "\t" + interp.truthmaps.get(i));
                        consoleprintln("\tNew Resulting Truth Value: " + (interp.truth_value ? "TRUE" : "FALSE"));

                        // Re-evaluate :)
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
                        consoleprintln("Updating Truth Value of Proposition: " + interp.parse);
                        consoleprintln("\t"+ i + "\t" + interp.truthmaps.get(i));
                        consoleprintln("\tNew Resulting Truth Value: " + (interp.truth_value ? "TRUE" : "FALSE"));

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

            // Print out each prop and truth values to system console.
            consoleprintln("Updating Truth Values of Proposition: " + interp.parse);
            for (String i : interp.truthmaps.keySet()) {
                consoleprintln(i + "\t" + interp.truthmaps.get(i));
            }

            // Re-evaluate :)
            interp.complex_eval();
            result.setText(interp.truth_value ? "TRUE" : "FALSE");

            // Add all elements to the proposition
            prop.getChildren().addAll(remove, proposition, propvalues, new Label("->"), result);

            // Add it to the list view & update it
            props.add(prop);
            propView.setItems(props);
        }
        // Clear user-input field.
        propField.clear();
    }

    public void clear_truth_table() {
        truth_table.getColumns().clear();
        truth_table.getItems().clear();
    }
    public void new_truth_table() {
        // Clear Truth Table
        clear_truth_table();

        truth_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create new proposition interp and attempt to evaluate
        PropositionInterpreter interp;
        try {
            interp = new PropositionInterpreter(truthField.getText(), false, false);
        } catch (PropositionParser.ParseError error) {
            userAlert("Interpreting Error",
                    ("Error when interpreting string '" + truthField.getText() + "':"
                            + "\nPlease check your proposition and try again."
                            + "\nIf you believe this is an error, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues"),
                    error);
            return;
        }

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
                // TODO: find out why artifact columns don't automatically divide size like IntelliJ does
                TableColumn<List<String>, String> col = new TableColumn<>(names.get(i));
                col.setSortable(false);
                final int temp = i;
                col.setCellValueFactory(data -> {
                    return new ReadOnlyStringWrapper(data.getValue().get(temp));
                });
                truth_table.getColumns().add(col);
                System.out.println("Column with name \"" + names.get(i) + "\" added. Width = " + col.getWidth());
            } else {
                // Create a final column for the proposition as a whole
                TableColumn<List<String>, String> col = new TableColumn<>(truthField.getText());
                col.setSortable(false);
                final int temp = i;
                col.setCellValueFactory(data -> {
                    return new ReadOnlyStringWrapper(data.getValue().get( temp ));
                });
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

    private boolean truth_next_assignment(ArrayList<Boolean> values) {
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

    private TextField tab_textfield_check() throws IOException {
        String id = tabs.getSelectionModel().getSelectedItem().getId();
        switch(id) {
            case "propositions":
                return propField;
            case "truth_tables":
                return truthField;
            default:
                throw new IOException("Attempted to read nonexistent tab '" + id + "'.");
        }
    }

    public void insert_conjunction() throws IOException {
        TextField temp = tab_textfield_check();
        temp.setText(temp.getText() + " & ");
    }

    public void insert_disjunction() throws IOException {
        TextField temp = tab_textfield_check();
        temp.setText(temp.getText() + " | ");
    }

    public void insert_negation() throws IOException {
        TextField temp = tab_textfield_check();
        temp.setText(temp.getText() + "!");
    }

    public void insert_condition() throws IOException {
        TextField temp = tab_textfield_check();
        temp.setText(temp.getText() + " -> ");
    }

    public void insert_bicondition() throws IOException {
        TextField temp = tab_textfield_check();
        temp.setText(temp.getText() + " <-> ");
    }
}
