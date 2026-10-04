package com.example.demo8;

public class SDAdapterTf extends TFCardIpml implements SDcard  {


    @Override
    public String readSD() {
        return readTF();
    }

    @Override
    public void writeSD(String data) {
        writeTF(data);
    }
}
