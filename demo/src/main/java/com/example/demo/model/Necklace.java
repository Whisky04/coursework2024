package com.example.demo.model;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Necklace extends Product{
    private Double length;
    public Necklace(String title, String description, int quantity, String colour, String material, Double length) {
        super(title, description, quantity, colour, material);
        this.length = length;
    }
}
