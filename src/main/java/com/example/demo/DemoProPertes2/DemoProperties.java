package com.example.demo.DemoProPertes2;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "test2")
@EnableConfigurationProperties(DemoProperties.class)
@Data
public class DemoProperties {
    private String nameTest;
}
