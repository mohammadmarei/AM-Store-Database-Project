package com.example.am_store;

public class t_product {
    private int id;
    private String name;
    private String category;
    private String brand;
    private String description;
    private int quantity;
    private double price;
    private String imagePath;

    public t_product(int id, String name, String category, String brand, String description, int quantity, double price, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public  String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public  double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
}

