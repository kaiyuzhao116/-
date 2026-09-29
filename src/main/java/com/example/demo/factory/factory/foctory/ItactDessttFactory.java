package com.example.demo.factory.factory.foctory;

import com.example.demo.factory.factory.Coffee;
import com.example.demo.factory.factory.Dessert;
import com.example.demo.factory.factory.LatterCoffee;
import com.example.demo.factory.factory.MatchaMouse;

public class ItactDessttFactory implements DessertFactory {
    @Override
    public Dessert createDessert() {
        Dessert dessert = new MatchaMouse();
        return dessert;
    }

    @Override
    public Coffee createCoffee() {

        LatterCoffee latterCoffee = new LatterCoffee();
        return latterCoffee;
    }
}
