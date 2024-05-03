package com.example.demo.fxControllers;

import com.example.demo.HelloApplication;
import com.example.demo.fxControllers.tableParameters.ManagerTableParameters;
import com.example.demo.hibernate.ShopHibernate;
import com.example.demo.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class main implements Initializable {
    //I add @FXML above all attributes that are linked to form element ids
    //<editor-fold desc="Here are the fields for Shop tab">
    @FXML
    //ListView without a type is just a raw usage. It is best to specify the type of objects that will be stored in that list
    //In our case it is Product
    public ListView<Product> shopProducts;
    @FXML
    public Tab shopTab;
    @FXML
    public ListView<Product> myCartItems;
    //</editor-fold>

    //<editor-fold desc="Here are the fields for products tab">
    @FXML
    public Tab productsTab;
    @FXML
    public ListView<Product> productAdminList;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea productDescriptionField;
    @FXML
    public TextField productQuantityField;
    @FXML
    public TextField productColourField;
    @FXML
    public TextField productMaterialField;
    @FXML
    public TextField productDiameterField;
    @FXML
    public TextField productLengthField;
    @FXML
    public RadioButton productRingRadio;
    @FXML
    public RadioButton productNecklaceRadio;
    //</editor-fold>
    @FXML
    public Button logOutButton;

    @FXML
    private TextField materialFilterField;
    @FXML
    private TextField colorFilterField;
    @FXML
    private RadioButton ringFilterRadio;
    @FXML
    private RadioButton necklaceFilterRadio;

    //<editor-fold desc="Here are the fields for User tab">
    public TableColumn<ManagerTableParameters, Integer> managerColId;
    public TableColumn<ManagerTableParameters, String> managerColLogin;
    public TableColumn<ManagerTableParameters, String> managerColName;
    public TableView<ManagerTableParameters> managerTable;
    public TableView<Customer> customerTable;
    public TableColumn<ManagerTableParameters, Void> dummyCol;
    public Tab usersTab;
    private ObservableList<ManagerTableParameters> data = FXCollections.observableArrayList();
    //</editor-fold>

    public Tab ordersTab;
    public Tab warehousesTab;
    @FXML
    public TabPane tabPane;
    private EntityManagerFactory entityManagerFactory;
    private ShopHibernate shopHibernate;
    //I need to know which user is selected
    private User user;

    //When class implements Initializable interface, you will be required to implements this method.
    // It allows us to access all the fields before they are rendered
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logOutButton = new Button();
        //Initializing TableViews
        //TODO Complete remaining columns
        managerTable.setEditable(true);
        //setCellValueFactory allows to display the data
        managerColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        managerColLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        //setCellFactory and setOnEditCommit allows us to edit cell value
        managerColName.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColName.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            //Before updating, get the latest version from database
            Manager manager = shopHibernate.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setName(event.getNewValue());
            shopHibernate.update(manager);
        });
        managerColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        //This portion of the code is responsible for generating a graphic element (button) in a cell
        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> callback = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        shopHibernate.delete(Manager.class, row.getId());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };
        dummyCol.setCellFactory(callback);
        //TODO complete Customer TableView
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.shopHibernate = new ShopHibernate(entityManagerFactory);
        this.user = user;
        loadTabData();
        setCustomerView();
    }

    private void setCustomerView() {
        //Customer should not have any access or knowledge about tabs that are intended for Managers/Admins
        if (user instanceof Customer) {
            //You could simply disable tabs, but it is better to not render them
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(productsTab);
            tabPane.getTabs().remove(warehousesTab);
        } else if (!((Manager) user).isAdmin()) {
            tabPane.getTabs().remove(usersTab);
        }
    }

    //<editor-fold desc="Logic for User Tab">
    private void fillManagerTable() {
        //get all records from the database for Manager TableView
        List<Manager> managers = shopHibernate.getAllRecords(Manager.class);
        for (Manager m : managers) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setName(m.getName());
            //TODO complete remaining columns
            data.add(managerTableParameters);
        }
        managerTable.setItems(data);
    }
    //</editor-fold>

    //<editor-fold desc="Logic for Products Tab">
    //A method that is called once Add button is clicked
    public void createRecord() {
        if (productRingRadio.isSelected()) {
            Ring ring = new Ring(productTitleField.getText(),
                    productDescriptionField.getText(),
                    Integer.parseInt(productQuantityField.getText()),
                    productColourField.getText(),
                    productMaterialField.getText(),
                    Double.parseDouble(productDiameterField.getText()));
            shopHibernate.create(ring);
        } else if (productNecklaceRadio.isSelected()) {
            Necklace necklace = new Necklace(productTitleField.getText(),
                    productDescriptionField.getText(),
                    Integer.parseInt(productQuantityField.getText()),
                    productColourField.getText(),
                    productMaterialField.getText(),
                    Double.parseDouble(productLengthField.getText()));
            shopHibernate.create(necklace);
        }
        //refresh the product list
        productAdminList.getItems().clear();
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    public void updateRecord() {
        //Once the product is selected, load that information to the fields for easier editing
        //You can also implement a TableView for easier manipulation
        Product product = shopHibernate.getEntityById(Product.class, productAdminList.getSelectionModel().getSelectedItem().getId());
        if (product instanceof Ring) {
            Ring ring = (Ring) product;
            product.setTitle(productTitleField.getText());
            productDescriptionField.setText(product.getDescription());
            ring.setDescription(productDescriptionField.getText());
            shopHibernate.update(ring);
        } else if (product instanceof Necklace) {
            Necklace necklace = (Necklace) product;
            product.setTitle(productTitleField.getText());
            productDescriptionField.setText(product.getDescription());
            necklace.setDescription(productDescriptionField.getText());
            shopHibernate.update(necklace);
        }
        productAdminList.getItems().clear();
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    //Delete operations are more complicated, because we need to control what stays in the database and what should be removed
    //For this reason generic delete will not work for us, therefore I create custom delete methods
    public void deleteRecord() {
        Product product = productAdminList.getSelectionModel().getSelectedItem();
        productAdminList.getItems().remove(product);
        productAdminList.getItems().clear();
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    //This method enables/disables fields based on product type
    public void disableFields() {
        if (productRingRadio.isSelected()) {
            productLengthField.setDisable(true);
        } else if (productNecklaceRadio.isSelected()) {
            productDiameterField.setDisable(true);
        }
    }

    //This method is called when you select a product from ListView. This ListView displays all products that are currently in the database
    //It populates the data in the GUI fields for faster data manipulation
    public void loadProductData() {
        //ListView element has getSelectionModel().getSelectedItem() method, it will return the selected item, which is a Product
        Product product = productAdminList.getSelectionModel().getSelectedItem();
        //Because ListView<Product> stores Product, we can add all child class objects
        //This way we can access only those attributes and methods that are defined in Product class
        //Use instanceof to determine what child class object is there and fill the appropriate fields
        if (product instanceof Ring ring) {
            //See above, I have a pattern variable, this way I do not have to initialize in a separate line
            productTitleField.setText(ring.getTitle());
            productDescriptionField.setText(ring.getDescription());
            productQuantityField.setText(String.valueOf(ring.getQuantity()));
            productDiameterField.setText(String.valueOf(ring.getDiameter()));
            productColourField.setText(ring.getColour());
            productMaterialField.setText(ring.getMaterial());
        } else if (product instanceof Necklace necklace) {
            productTitleField.setText(necklace.getTitle());
            productDescriptionField.setText(necklace.getDescription());
            productQuantityField.setText(String.valueOf(necklace.getQuantity()));
            productLengthField.setText(String.valueOf(necklace.getLength()));
            productColourField.setText(necklace.getColour());
            productMaterialField.setText(necklace.getMaterial());
        }
        productAdminList.getItems().clear();
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }
    //</editor-fold>

    //<editor-fold desc="Logic for Shop tab">
    //Do not create cart, only when user clicks "Buy"
    public void removeFromCart() {
        Product product = myCartItems.getSelectionModel().getSelectedItem();
        shopProducts.getItems().add(product);
        myCartItems.getItems().remove(product);
    }

    public void addToCart() {
        Product product = shopProducts.getSelectionModel().getSelectedItem();
        myCartItems.getItems().add(product);
        shopProducts.getItems().remove(product);
    }

    public void buyItems() {
        //When the user clicks buy, call a specific method, not generic, because this is more complicated
        shopHibernate.createCart(myCartItems.getItems(), user);
    }
    //</editor-fold>

    public void loadTabData() {
        if (shopTab.isSelected()) {
            shopProducts.getItems().addAll(shopHibernate.loadAvailableProducts());
        } else if (usersTab.isSelected()) {
            fillManagerTable();
            //TODO complete Customer TableView
            //fillCustomerTable();
        }
        //TODO fill only when the tab is clicked
    }

//    public void deleteCart() {
//        shopHibernate.deleteCart();
//    }
    public void loadProductReviewForm() throws IOException {
        //Get resources: fxml, controller, grapics, styles...
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("productReview.fxml"));
        //Load resources, without this step I cannot access controllers
        Parent parent = fxmlLoader.load();

        productReview productReview = fxmlLoader.getController();
        //Forms do not know about each other, therefore I must pass info between them
        productReview.setData(entityManagerFactory, user);
        //Create a completely new window
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Comments");
        stage.setScene(scene);
        fxUtils.setStageParameters(stage, scene, true);
    }

    @FXML
    public void filter(ActionEvent event) {
        // Retrieve filter criteria from UI elements
        String material = materialFilterField.getText();
        String color = colorFilterField.getText();
        boolean isRingSelected = ringFilterRadio.isSelected();
        boolean isNecklaceSelected = necklaceFilterRadio.isSelected();

        // If no filter criteria are specified, show all products
        if (material.isEmpty() && color.isEmpty() &&!isRingSelected &&!isNecklaceSelected) {
            shopProducts.getItems().clear();
            shopProducts.getItems().addAll(shopHibernate.loadAvailableProducts());
            return;
        }

        // Perform filtering logic based on criteria
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for (Product product : shopProducts.getItems()) {
            // Check if the product matches the filter criteria
            if ((material.isEmpty() || product.getMaterial().equalsIgnoreCase(material)) &&
                    (color.isEmpty() || product.getColour().equalsIgnoreCase(color)) &&
                    ((isRingSelected && product instanceof Ring) || (isNecklaceSelected && product instanceof Necklace))) {
                filteredProducts.add(product);
            }
        }

        // Update the displayed products with the filtered list
        shopProducts.getItems().clear();
        shopProducts.setItems(filteredProducts);
    }
    @FXML
    public void logOut(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginWindow.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            Scene scene = new Scene(parent);
            fxUtils.setStageParameters(stage, scene, false);
    }
}