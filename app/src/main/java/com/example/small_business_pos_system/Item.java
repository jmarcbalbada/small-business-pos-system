package com.example.small_business_pos_system;

public class Item {
    private int it_id;
    private String name;
    private float price;

    Item(){};

    Item(int it_id,String name, float price)
    {
        this.it_id = it_id;
        this.name = name;
        this.price = price;
    }

    Item(String name, float price)
    {
        this.name = name;
        this.price = price;
    }

    Item(String name)
    {
        this.name = name;
    }

    //getters
    public String getName()
    {
        return this.name;
    }

    public int getIt_id()
    {
        return this.it_id;
    }

    public float getPrice()
    {
        return this.price;
    }

    //setters
    public void setName(String name)
    {
        this.name = name;
    }

    public void setIt_id(int it_id)
    {
        this.it_id = it_id;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    @Override
    public String toString()
    {
        String str = "Name = " + getName() + " Price = " + getPrice();
        return str;
    }
}
