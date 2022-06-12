package com.example.small_business_pos_system;

public class Inventory {
    private int in_id;
    private Item item;
    private int quantity;

    public Inventory(){}

    public Inventory(int in_id,Item item, int quantity)
    {
        this.in_id = in_id;
        this.item = item;
        this.quantity = quantity;
    }

    public Inventory(Item item, int quantity)
    {
        this.item = item;
        this.quantity = quantity;
    }

    //getters
    public Item getItem()
    {
        return this.item;
    }

    public int getIn_id()
    {
        return this.in_id;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    //setters
    public void setItem(Item item)
    {
        this.item = item;
    }

    public void setIn_id(int in_id)
    {
        this.in_id = in_id;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String toString()
    {
        String str = "Name: " + this.item.getName() + "\n Price: " +  this.getItem().getPrice()
                + "\n Quantity: " + this.getQuantity();
        return str;
    }
}
