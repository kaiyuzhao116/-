package com.example.demo.DemoProPertes2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class testController {

    @Autowired
    private DemoProperties demoProperties;

    @RequestMapping("/hello")
    public String hello() {
        String nameTest = demoProperties.getNameTest();
        System.out.println(nameTest);
        return "hello world";
    }
}
