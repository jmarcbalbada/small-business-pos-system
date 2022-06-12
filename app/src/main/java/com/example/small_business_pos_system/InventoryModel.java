package com.example.small_business_pos_system;

public class InventoryModel {
    private String name;
    private float price;
    private int quantity;

    public InventoryModel(String name, float price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        String str = "\nName: " + this.getName() + "\nPrice: " +  String.format("%.2f", this.getPrice())
                + "\nQuantity: " + this.getQuantity() + "\n";
        return str;
    }
}
