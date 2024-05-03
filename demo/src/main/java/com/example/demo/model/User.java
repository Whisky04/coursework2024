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
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String name;
    protected String surname;
    protected String email;
    protected LocalDate dateCreated;
    protected String login;
    protected String password;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    protected List<Comment> comments;
    @Transient
    protected String noNeedToSave;

    public User(String name, String surname, String login, String password, String email) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.dateCreated = LocalDate.now();
    }
}
