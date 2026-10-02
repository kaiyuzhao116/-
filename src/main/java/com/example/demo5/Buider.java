package com.example.demo5;

public abstract class Buider {
    protected Bike bike = new Bike();

    public abstract void buildFrame();
    public abstract void buildSeat();
    public abstract Bike construct();




}
