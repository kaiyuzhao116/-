package com.example2.demo;

public class FriedRice extends FastFood{

    public FriedRice(){
        super(10, "炒饭！");
    }

    @Override
    public float cost(){
        return getPrice();
    }
}
