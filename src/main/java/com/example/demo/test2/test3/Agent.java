package com.example.demo.test2.test3;

import lombok.Data;

@Data
public class Agent {


    private Company company;
    private Fans fans;
    private Star star;

    public void jianmiam(){
        System.out.println("company: " + company.getName() + ", fans: " + fans.getName() + ", star: " + star.getName() + " 一起赚钱");
    }

}
