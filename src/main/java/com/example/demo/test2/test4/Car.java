package com.example.demo.test2.test4;

import lombok.Data;

@Data
public class Car implements redCad, BlueCar {
    private String name;

    @Override
    public void drive() {
        System.out.println("Car drive");
    }

    @Override
    public void fly() {
        System.out.println("Car fly");
    }
}
