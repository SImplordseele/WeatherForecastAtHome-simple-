package org.example.weatherforecast;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("Mainboard.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Weather Forecast At Home");
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
