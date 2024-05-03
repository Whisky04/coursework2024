package com.example.demo;

import com.example.demo.fxControllers.fxUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxUtils.setStageParameters(stage, scene, false);
    }

    public static void main(String[] args) {
        launch();
    }
}