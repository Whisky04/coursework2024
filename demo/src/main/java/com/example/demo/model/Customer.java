package com.example.demo.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer extends User{
    private String deliveryAddress;
    private String billingAddress;
    private LocalDate birthDate;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Cart> myCarts = new ArrayList<>();
    public Customer(String name, String surname, String login, String password, String deliveryAddress, String billingAddress, LocalDate birthDate, String email) {
        super(name, surname, login, password, email);
        this.deliveryAddress = deliveryAddress;
        this.billingAddress = billingAddress;
        this.birthDate = birthDate;
    }
    public Customer(String name, String surname, String login, String password, int id, String email) {
        super(name, surname, login, password, email);
    }

    @Override
    public String toString() {
        return "login: " + login;
    }
}
