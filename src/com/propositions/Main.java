package com.propositions;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Propositions LIVE");
        stage.setMinHeight(360);
        stage.setMinWidth(480);
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
