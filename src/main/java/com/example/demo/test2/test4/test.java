package com.example.demo.test2.test4;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {
    public static void main(String[] args) {
        System.out.println("hello world");


        youcar youcar = new youcar();
        youcar.fly();
        youcar.drive();


        System.out.println("<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>");

        diancar diancar = new diancar();
        diancar.drive();
        diancar.fly();
    }
}
