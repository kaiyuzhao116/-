package com.example2.demo;

public class FriedNoodles extends FastFood{
    public FriedNoodles() {
        super(12, "炒面！");
    }

    @Override
    public float cost() {
        return getPrice();
    }
}
