package com.example.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String title;
    protected String description;
    protected int quantity;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    protected List<Comment> comments;
    protected String colour;
    protected String material;
    private LocalDate dateCreated;
    @ManyToOne
    private Cart cart;
    @ManyToOne
    private Warehouse warehouse;
    public Product(String title, String description, int quantity, String colour, String material) {
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.comments = new ArrayList<>();
        this.dateCreated = LocalDate.now();
        this.colour = colour;
        this.material = material;
    }
}
