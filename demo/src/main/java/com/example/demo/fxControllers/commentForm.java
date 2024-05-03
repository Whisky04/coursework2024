package com.example.demo.fxControllers;

import com.example.demo.hibernate.GenericHibernate;
import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class commentForm {
    @FXML
    public TextField commentTitleField;
    @FXML
    public TextArea commentBodyField;
    @FXML
    public Slider ratingField;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private Product product;
    private GenericHibernate genericHibernate;
    private Comment comment;
    private boolean isUpdate;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user, Product product) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.product = product;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Comment comment, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.comment = comment;
        this.currentUser = user;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Comment comment) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.comment = comment;
        this.isUpdate = true;

        Comment commentToUpdate = genericHibernate.getEntityById(Comment.class, comment.getId());
        ratingField.setVisible(false);
        commentTitleField.setText(commentToUpdate.getCommentTitle());
        commentBodyField.setText(commentToUpdate.getCommentBody());
    }
    public void saveComment() {
        if (!isUpdate && comment == null) {
            Product currentProduct = genericHibernate.getEntityById(Product.class, product.getId());
            Comment reviewForProduct = new Comment(commentTitleField.getText(), commentBodyField.getText(), ratingField.getValue(), currentUser, currentProduct);
            currentProduct.getComments().add(reviewForProduct);
            genericHibernate.update(currentProduct);
        } else if (!isUpdate) {
            Comment parentComment = genericHibernate.getEntityById(Comment.class, comment.getId());
            Comment replyToComment = new Comment(commentTitleField.getText(), commentBodyField.getText(), currentUser, parentComment);
            parentComment.getReplies().add(replyToComment);
            genericHibernate.update(parentComment);
        } else {
            Comment commentToUpdate = genericHibernate.getEntityById(Comment.class, comment.getId());
            commentToUpdate.setCommentBody(commentBodyField.getText());
            commentToUpdate.setCommentTitle(commentTitleField.getText());
            genericHibernate.update(commentToUpdate);
        }
        Stage stage = (Stage) commentTitleField.getScene().getWindow();
        stage.close();
    }
}
