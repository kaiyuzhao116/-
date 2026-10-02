package com.example.demo4;

public class OfoBulider extends Builder {
    @Override
    public void buildFrame() {
        bike.setFrame("Ofo frame");
    }

    @Override
    public void buildSeat() {
        bike.setSeat("Ofo seat");
    }
    @Override
    public Bike construct() {
        buildFrame();
        buildSeat();
        return bike;
    }
}
