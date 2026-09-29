package com.example.demo2;

import java.util.HashMap;

public class Test implements Cloneable{

    private String name;
    private int age;

    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }
    public String getName() {
        return name;
    }

    private HashMap<String, String> map ;

    public HashMap<String, String> getMap() {
        return map;
    }
    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
    @Override
    protected Test clone() throws CloneNotSupportedException {

        System.out.println("浅克隆成功！！");
        return (Test) super.clone();
    }
}
