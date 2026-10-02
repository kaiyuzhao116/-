package com.example.demo5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dirceter {

    protected Buider builder;
    Dirceter(Buider builder){
        this.builder = builder;
    }

    public Bike construct(){
        return builder.construct();
    }
    public void setFrame(String frame){
        builder.buildFrame();
    }
    public void setSeat(String seat){
        builder.buildSeat();
    }


}
