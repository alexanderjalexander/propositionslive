package com.propositions;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.*;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

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
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import atlantafx.base.theme.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private MainProperties properties;

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Propositions LIVE");
        stage.setMinHeight(480);
        stage.setMinWidth(480);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));

        // Override Default Exception Handler
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            consoleprintln("Handler caught exception: " + throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("A Run-time Exception Occurred");
            alert.setContentText("The following run-time exception occurred while trying to run your program:\n\"" + throwable.toString() + ": " + throwable.getMessage()
                    + "\"\nIf the problem persists, please raise an issue at https://github.com/alexanderjalexander/propositionslive/issues");
            alert.show();
        });

        // Handle user properties
        MainProperties.init();
        System.out.println("Reading User Properties...");
        System.out.println("MainProperties.getDarkMode() = " + MainProperties.INSTANCE.getDarkMode());

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
        } else {
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

        propView.setId("propView");
        propConsole.setId("propConsole");

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws IOException {
        MainProperties.INSTANCE.exit();
        System.out.println("Exiting...");
    }

    // ---------------------------------------------------------

    // Set up proposition viewport
    @FXML
    private ListView<Pane> propView = new ListView<>();
    private final ObservableList<Pane> props = FXCollections.observableArrayList();

    // Set up text field
    @FXML
    private TextField propField;

    // Setting up clipboard parameters
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Set up proposition console viewport.
    @FXML
    private ListView<Pane> propConsole = new ListView<>();
    private final ObservableList<Pane> propConsoleEntries = FXCollections.observableArrayList();

    @FXML
    private MenuItem darkModeMenu;


    public void userAlert(String title, String message, Exception error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message + ":\n\"" + error + "\": \"" + error.getMessage());
        alert.show();
        consoleerrorln(message + "\n\"" + error + "\": \"" + error.getMessage());
    }

    public void update_darkmode_menu() {

        System.out.print("update_darkmode_menu() called. ");
        if (MainProperties.INSTANCE.getDarkMode()) {
            System.out.println("Updating button to \"Light Mode\"");
            darkModeMenu.setText("Light Mode");
        } else {
            System.out.println("Updating button to \"Dark Mode\"");
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
                interp = new PropositionInterpreter(propField.getText(), false, false);
            } catch (PropositionParser.ParseError error) {
                userAlert("Interpreting Error",
                        ("Error when interpreting string '" + propField.getText() + "':"
                                + "\nPlease check your proposition and try again."
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
            Label result = new Label(interp.truth_value ? "TRUE" : "FALSE");

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

            // Create radio buttons for each proposition
            VBox propvalues = new VBox(10);
            for (String i : interp.truthmaps.keySet()) {
                HBox propvalue = new HBox(10);

                Label proplabel = new Label(i + ": ");
                ToggleGroup radios = new ToggleGroup();
                RadioButton truebutton = new RadioButton("True");
                truebutton.setId(i);
                truebutton.setToggleGroup(radios);
                truebutton.setSelected(true);

                RadioButton falsebutton = new RadioButton("False");
                falsebutton.setToggleGroup(radios);
                falsebutton.setId(i);

                propvalue.getChildren().addAll(proplabel, truebutton, falsebutton);
                propvalues.getChildren().add(propvalue);
            }
            propvalues.setAlignment(Pos.CENTER_LEFT);

            Label result = new Label("");
            VBox.setVgrow(result, Priority.ALWAYS);
            prop.setAlignment(Pos.CENTER_LEFT);

            Button interpret = new Button("INTERPRET");
            interpret.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
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


    public void insert_conjunction() {
        
        propField.setText(propField.getText() + "&");
    }

    public void insert_disjunction() {
        
        propField.setText(propField.getText() + "|");
    }

    public void insert_negation() {
        
        propField.setText(propField.getText() + "!");
    }

    public void insert_condition() {
        
        propField.setText(propField.getText() + "->");
    }

    public void insert_bicondition() {
        
        propField.setText(propField.getText() + "<->");
    }
}
