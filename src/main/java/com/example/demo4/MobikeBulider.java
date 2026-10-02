package com.example.demo4;

public class MobikeBulider extends Builder {
    @Override
    public void buildFrame() {
        bike.setFrame("Mobike frame");
    }

    @Override
    public void buildSeat() {
        bike.setSeat("Mobike seat");
    }
    @Override
    public Bike construct() {
        buildFrame();
        buildSeat();
        return bike;
    }
}
