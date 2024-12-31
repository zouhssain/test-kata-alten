package com.alten.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private double price;
    private int quantity;
    private String internalReference;
    private int shellId;
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;
    private int rating;
    private Date createdAt;
    private Date updatedAt;
    public enum InventoryStatus {
        INSTOCK, LOWSTOCK, OUTOFSTOCK;
    }
}