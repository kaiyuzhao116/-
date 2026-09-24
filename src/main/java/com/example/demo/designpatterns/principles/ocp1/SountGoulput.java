package com.example.demo.designpatterns.principles.ocp1;

public class SountGoulput {

    private Preson preson;

    public void  setPreson(Preson preson) {
        this.preson = preson;
    }

    public void display1() {
        preson.display();
    }
}
