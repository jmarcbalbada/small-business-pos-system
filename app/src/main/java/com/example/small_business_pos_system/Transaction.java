package com.example.small_business_pos_system;

public class Transaction {
    private Item items;
    private int ref_no;
    private int quantity;
    private float totalPrice;
    private String dateOfPurchase;

    public Transaction(){}

    public Transaction (Item items, int ref_no, int quantity, float totalPrice, String dateOfPurchase)
    {
        this.items = items;
        this.ref_no = ref_no;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateOfPurchase = dateOfPurchase;
    }

    public Transaction (String name, float price, int ref_no, int quantity, float totalPrice, String dateOfPurchase)
    {
        items.setName(name);
        items.setPrice(price);
        this.ref_no = ref_no;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateOfPurchase = dateOfPurchase;
    }

    //getters
    public Item getItems()
    {
        return this.items;
    }

    public int getRef_no()
    {
        return this.ref_no;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public float getTotalPrice()
    {
        return this.totalPrice;
    }

    public String getDateOfPurchase()
    {
        return this.dateOfPurchase;
    }

    //setters
    public void setItems(Item items)
    {
        this.items = items;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setRef_no(int ref_no)
    {
        this.ref_no = ref_no;
    }

    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public void setDateOfPurchase(String dateOfPurchase)
    {
        this.dateOfPurchase = dateOfPurchase;
    }
}
