package com.example.demo2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;

import java.util.HashMap;

@Slf4j
public class teste {

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Hello, World!");
        Test test = new Test();
        Student student = new Student();


        test.setStudent(student);
        test.getStudent().setName("张三");


        Test clone = test.clone();
        System.out.println(test.getStudent());
        System.out.println(clone.getStudent()); // 输出: true
        System.out.println("");
        clone.getStudent().setName("李四");
        System.out.println(test.getStudent());
        System.out.println(clone==test); // 输出: false
    }
}
