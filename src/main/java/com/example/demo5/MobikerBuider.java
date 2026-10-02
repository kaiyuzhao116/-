package com.example.demo5;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MobikerBuider extends Buider {
    @Override
    public void buildFrame() {
        bike.setFrame("Mobiker frame");
    }


    @Override
    public void buildSeat() {
        bike.setSeat("Mobiker seat");
    }

    @Override
    public Bike construct() {
        buildSeat();
        buildFrame();

        return bike;
    }
}
