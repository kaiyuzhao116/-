package com.example2.demo1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class pearl extends Topping{

    public pearl(Drink drink) {
        super(drink,2,"珍珠");

    }
    public float cost() {
        return getPrice() + getDrink().cost();
    }
    public String getDesc() {
        return super.getDesc() + getDrink().getDesc();
    }
}
