package com.example.demo8;

public class SDAdapterTf  implements SDcard  {

    private TFCard tfCar;

    public SDAdapterTf(TFCard tfCar) {
        this.tfCar = tfCar;
    }

    @Override
    public String readSD() {
        return tfCar.readTF();
    }

    @Override
    public void writeSD(String data) {
        tfCar.writeTF(data);
    }
}
