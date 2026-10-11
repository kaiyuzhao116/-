package com.example2.demo;

public class Clinent {

    public static void main(String[] args) {

        FastFood food = new FriedRice();
        System.out.println(food.getDesc() + " " + food.cost());

        System.out.println("----------------------");


        food = new Egg(food);
        System.out.println(food.getDesc() + " " + food.cost());


        System.out.println("----------------------");

        food = new Bacan(food);

        System.out.println(food.getDesc() + " " + food.cost());

    }
}
