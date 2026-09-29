package com.example.demo2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;

import java.util.HashMap;

@Slf4j
public class teste {

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Hello, World!");
        Test test = new Test();
        test.setAge(18);
        test.setName("张三");

        Test clone = test.clone();



        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        test.setMap(objectObjectHashMap);


        System.out.println(test.getMap()==clone.getMap()); // 输出: false
        System.out.println(test.getAge()+test.getName()); // 输出: 18张三
        System.out.println(clone.getAge()+clone.getName()); // 输出: 20李四
        System.out.println(test.getAge()==clone.getAge());

        System.out.println(clone==test); // 输出: false
    }
}
