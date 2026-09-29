package com.example.demo.factory.factory.foctory;

import com.example.demo.factory.factory.AmericanCoffe;
import com.example.demo.factory.factory.Coffee;
import com.example.demo.factory.factory.Dessert;
import com.example.demo.factory.factory.Tiramisu;

public class AmeriacnDessFactory implements DessertFactory {
    @Override
    public Dessert createDessert() {

        Tiramisu tiramisu = new Tiramisu();
        return tiramisu;
    }

    @Override
    public Coffee createCoffee() {

        AmericanCoffe americanCoffe = new AmericanCoffe();
        return americanCoffe;
    }
}
