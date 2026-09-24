package com.example.demo.test2.test3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test2 {
    public static void main(String[] args) {
        Agent agent = new Agent();
        Company company = new Company();
        Fans fans = new Fans();
        Star star = new Star();

        company.setName("三星");
        fans.setName("小王");
        star.setName("王源");

        agent.setCompany(company);
        agent.setFans(fans);
        agent.setStar(star);
        agent.jianmiam();
    }
}

