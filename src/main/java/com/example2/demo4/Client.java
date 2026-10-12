package com.example2.demo4;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        av av = new av();
        new mac(av).play("洪荒之力");
        System.out.println("_________________________");
        new windows(av).play("洪荒之力");
    }
}
