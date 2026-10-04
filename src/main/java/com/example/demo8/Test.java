package com.example.demo8;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args) {

        System.out.println("Hello World!");

//        SDcardOImpL sDcardOImpL = new SDcardOImpL();
//
//        Computer computer = new Computer();
//        String s = computer.readSD(sDcardOImpL);
//        System.out.println(s);

        SDAdapterTf sdAdapterTf = new SDAdapterTf();
        Computer computer = new Computer();
        computer.readSD(sdAdapterTf);
        log.info("{}", sdAdapterTf.readSD());

    }
}
