package com.example.demo.test2;

import lombok.Data;

@Data
public class Computer {
    private memroy memory;
    private cpu processor;

    public void run(){
        processor.calculate();
        memory.store();

    }
}
