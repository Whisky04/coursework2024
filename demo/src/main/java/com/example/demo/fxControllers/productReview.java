package com.example.demo.fxControllers;

import com.example.demo.HelloApplication;
import com.example.demo.hibernate.ShopHibernate;
import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.model.Ring;
import com.example.demo.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class productReview {
    @FXML
    public ListView<Product> productListField;
    @FXML
    public TreeView<Comment> commentTreeField;
    private EntityManagerFactory entityManagerFactory;
    private ShopHibernate shopHibernate;
    private User user;

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.shopHibernate = new ShopHibernate(entityManagerFactory);
        this.user = user;
        fillLists();
    }

    private void fillLists() {
        productListField.getItems().clear();
        productListField.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    public void previewProduct() {
        //Use alert to display full product info
        Product product = shopHibernate.getEntityById(Product.class, productListField.getSelectionModel().getSelectedItem().getId());
        if (product instanceof Ring ring) {
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, ring.getTitle(), ring.toString());
            //TODO make sure that classes have toString method, otherwise you will get address
        }
        //TODO remaining alerts for other products
    }

    public void addReview() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("commentForm.fxml"));
        Parent parent = fxmlLoader.load();
        commentForm commentForm = fxmlLoader.getController();
        commentForm.setData(entityManagerFactory, user, productListField.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
        commentForm.setStage(stage);
    }

    public void loadReviews() {
        //Get info about product from database
        Product product = shopHibernate.getEntityById(Product.class, productListField.getSelectionModel().getSelectedItem().getId());
        commentTreeField.setRoot(new TreeItem<>());
        commentTreeField.setShowRoot(false);
        commentTreeField.getRoot().setExpanded(true);
        product.getComments().forEach(comment -> addTreeItem(comment, commentTreeField.getRoot()));
        commentTreeField.setCellFactory(treeView -> new TreeCell<Comment>() {
            @Override
            protected void updateItem(Comment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCommentTitle()); // Display the comment title
                }
            }
        });
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }

    public void updateComment() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("commentForm.fxml"));
        Parent parent = fxmlLoader.load();
        commentForm commentForm = fxmlLoader.getController();
        commentForm.setData(entityManagerFactory, commentTreeField.getSelectionModel().getSelectedItem().getValue());
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
    }

    public void reply() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("commentForm.fxml"));
        Parent parent = fxmlLoader.load();
        commentForm commentForm = fxmlLoader.getController();
        commentForm.setData(entityManagerFactory, commentTreeField.getSelectionModel().getSelectedItem().getValue(), user);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
    }

    public void delete() {
        shopHibernate.deleteComment(commentTreeField.getSelectionModel().getSelectedItem().getValue().getId());
    }
}
