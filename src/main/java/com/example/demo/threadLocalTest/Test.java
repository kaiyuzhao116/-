package com.example.demo.threadLocalTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args){
//        log.info("test");
//        ThreadLocal<String> testThreadLocal = new ThreadLocal<>();
//
//        testThreadLocal.set("test");
//        log.info(testThreadLocal.get());
//        Thread thread = new Thread(() -> {
//            log.info(testThreadLocal.get());
//        });
//        thread.start();

        log.info("test");
        InheritableThreadLocal<String> testThreadLocal = new InheritableThreadLocal<>();

        testThreadLocal.set("你好");
        log.info(testThreadLocal.get());
        Thread thread = new Thread(() -> {
            log.info(testThreadLocal.get());
        });
        thread.start();

    }
}
