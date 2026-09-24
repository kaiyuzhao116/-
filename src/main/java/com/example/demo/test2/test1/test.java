package com.example.demo.test2.test1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {
    public  static void main(String[] args){
        cpu intel = new Intel();
        cpu amd = new AMD();

        memroy shandi = new Shandi();


        Computer computer = new Computer();
        computer.setProcessor(intel);
        //computer.setProcessor(amd);
        computer.setMemory(shandi);
        computer.run();
    }
}
