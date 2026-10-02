package com.example.demo5;

public class OfoBuiler extends Buider {
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
