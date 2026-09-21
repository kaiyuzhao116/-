package com.example.demo.DemoProPerties1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class testController {
    @Autowired
    private DemoProperties demoProperties;

    @RequestMapping("/test")
    public String test() {
        return demoProperties.getNameTest();
    }
}
