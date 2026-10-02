package com.example.demo6;



public class test {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Student student  =new Student.Builder().setName("张三").setAge(18).bulid();
        System.out.println(student);
    }
}
