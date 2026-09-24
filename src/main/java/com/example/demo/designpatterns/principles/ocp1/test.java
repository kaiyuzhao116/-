package com.example.demo.designpatterns.principles.ocp1;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class test {
    public static void main(String[] args){

        SountGoulput p =  new SountGoulput();
        p.setPreson(new Student());
        p.display1();

        p.setPreson(new Teacher());
        p.display1();
    }
}
