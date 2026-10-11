package com.example2.demo1;

public abstract class Drink {
    private float price;
    private String desc;

    public Drink() {
    }
    public Drink(float price, String desc) {
        this.price = price;
        this.desc = desc;
    }

    public abstract float cost();

    public float getPrice() {
        return this.price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
