package com.example.demo8;

public class TFCardIpml implements TFCard {
    @Override
    public String readTF() {
        return "TFCard readsss";
    }

    @Override
    public void writeTF(String data) {
        // write data to TF card

        System.out.println("TFCard write: " + data);

    }
}
