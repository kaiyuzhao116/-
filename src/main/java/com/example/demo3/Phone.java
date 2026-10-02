package com.example.demo3;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Phone {
    private String brand;
    private String camera;

    private Phone(Bulider builder){

            this.brand = builder.brand;
            this.camera = builder.camera;
    }

    public static final class Bulider{
        private String brand;
        private String camera;
        public Bulider setBrand(String brand){
             this.brand = brand;
             return this;
        }
        public Bulider setCamera(String camera){
            this.camera = camera;
            return this;
        }
        public Phone build(){
            return new Phone(this);

        }


    }

    @Override
    public String toString() {
        return "Phone{" +
                "brand='" + brand + '\'' +
                ", camera='" + camera + '\'' +
                '}';
    }
}
