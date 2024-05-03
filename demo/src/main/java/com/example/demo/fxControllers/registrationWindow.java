package com.example.demo.fxControllers;

import com.example.demo.HelloApplication;
import com.example.demo.hibernate.ShopHibernate;
import com.example.demo.model.Customer;
import com.example.demo.model.Manager;
import com.example.demo.model.User;
import com.example.demo.model.PasswordHasher;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
public class registrationWindow {

    @FXML
    public TextField addressField;

    @FXML
    public TextField billingAddressField;

    @FXML
    public DatePicker birthDateField;

    @FXML
    public RadioButton customerCheckbox;

    @FXML
    public CheckBox isAdminCheck;

    @FXML
    public TextField loginField;

    @FXML
    public TextField nameField;

    @FXML
    public PasswordField passwordField;
    public String hashedPassword;

    @FXML
    public PasswordField repeatPasswordField;

    @FXML
    public TextField surnameField;

    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton managerCheckbox;
    @FXML
    public TextField emailField;
    ///<
//    @FXML
//    public TextField cardNoField;
    @FXML
    public TextField employeeIdField;

    public void showHideAdminCheckBox(ActionEvent event) {
        isAdminCheck.setVisible(managerCheckbox.isSelected());
        if (!managerCheckbox.isSelected()) {
            isAdminCheck.setSelected(false);
        }
    }

    //    @FXML
//    public TextField medCertificateField;
//    @FXML
//    public DatePicker employmentDateField;
    ///>

    private EntityManagerFactory entityManagerFactory;

    public void setData(EntityManagerFactory entityManagerFactory, boolean showManagerFields) {
        this.entityManagerFactory = entityManagerFactory;
        disableFields(showManagerFields);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Manager manager) {
        this.entityManagerFactory = entityManagerFactory;
        toggleFields(customerCheckbox.isSelected(), manager);
    }

    private void disableFields(boolean showManagerFields) {
        if (!showManagerFields) {
            isAdminCheck.setVisible(false);
            managerCheckbox.setVisible(false);
            ///<
//            employeeIdField.setVisible(false);
//            medCertificateField.setVisible(false);
//            employmentDateField.setVisible(false);
            ///>
        }
    }

    private void toggleFields(boolean isManager, Manager manager) {
        if (isManager) {
            addressField.setDisable(true);
            ///<
//            cardNoField.setDisable(true);
//            employeeIdField.setDisable(false);
//            medCertificateField.setDisable(false);
//            employmentDateField.setDisable(false);
            ///>
            if (manager.isAdmin()) isAdminCheck.setDisable(false);
        } else {
            addressField.setDisable(false);
            isAdminCheck.setDisable(true);
            ///<
//            cardNoField.setDisable(false);
//            employeeIdField.setDisable(true);
//            medCertificateField.setDisable(true);
//            employmentDateField.setDisable(true);
            ///>
        }
    }

    public void createUser() throws IOException {
        ShopHibernate shopHibernate = new ShopHibernate(entityManagerFactory);
        //String hashedPassword = PasswordHasher.hashPassword(passwordField.getText());
        if (customerCheckbox.isSelected()) {
            User user = new Customer(nameField.getText(), surnameField.getText(), loginField.getText(), passwordField.getText(), addressField.getText(), billingAddressField.getText(), birthDateField.getValue(), emailField.getText());
            shopHibernate.create(user);
            returnToLogin();
        } else {
            //Create Manager
            Manager manager = new Manager(nameField.getText(), surnameField.getText(), loginField.getText(), passwordField.getText(), isAdminCheck.isSelected(), emailField.getText());
            shopHibernate.create(manager);
            returnToLogin();
        }
    }

    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginWindow.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage) loginField.getScene().getWindow();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, false);
    }
}

