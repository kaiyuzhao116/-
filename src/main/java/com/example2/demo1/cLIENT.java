package com.example2.demo1;

public class cLIENT {
    public static void main(String[] args) {

        Drink drink = new Coffee();
        System.out.println(drink.getDesc() + " " + drink.cost());
        System.out.println("----------------------" );
        drink = new Pudding(drink);
        System.out.println(drink.getDesc() + " " + drink.cost());
    }
}
