package com.example.demo.designpatterns.principles.ocp;

public class DefaultSkin extends AbstractSkin {
    @Override
    public void display() {
        System.out.println("Displaying default skin");
    }
}
