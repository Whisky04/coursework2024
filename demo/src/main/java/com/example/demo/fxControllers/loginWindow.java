package com.example.demo.fxControllers;
import com.example.demo.HelloApplication;
import com.example.demo.hibernate.ShopHibernate;
import com.example.demo.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginWindow implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    private EntityManagerFactory entityManagerFactory;

    public void validateAndLoadMain() throws IOException {

        ShopHibernate hibernateShop = new ShopHibernate(entityManagerFactory);
        var user = hibernateShop.getUserByCredentials(loginField.getText(), passwordField.getText());
        //If user exists, open main window form
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
            //Load resources associated with the form. We need this step to access controllers
            Parent parent = fxmlLoader.load();
            //Access controller of main window. Each form has its own controller, so make sure that you make no mistake here
            main main = fxmlLoader.getController();
            main.setData(entityManagerFactory, (User) user);
            //Every element in the form knows to which scene it belongs and scene knows to which stage (window it belongs)
            Stage stage = (Stage) loginField.getScene().getWindow();
            Scene scene = new Scene(parent);
            fxUtils.setStageParameters(stage, scene, false);
        } else {
            //Something went wrong, generate an alert
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, "Login error", "No such user or wrong credentials");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityManagerFactory = Persistence.createEntityManagerFactory("Shop");
    }

    public void openRegistration() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registrationWindow.fxml"));
        Parent parent = fxmlLoader.load();
        //Access controller of main window. Each form has its own controller, so make sure that you make no mistake here
        registrationWindow registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, true);
        Stage stage = (Stage) loginField.getScene().getWindow();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, false);
    }
}

