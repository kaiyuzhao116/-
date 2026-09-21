package com.example.demo.DemoProPerties1;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "test")
@Data
public class DemoProperties {

    private String nameTest;
}
