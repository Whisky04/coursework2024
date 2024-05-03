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
public final class Ring extends Product {
    private Double diameter;

    public Ring(String title, String description, int quantity, String colour, String material, double diameter) {
        super(title, description, quantity, colour, material);
        this.colour = colour;
        this.diameter = diameter;
    }

    @Override
    public String toString() {
        return title + ":" + quantity;
    }
}
