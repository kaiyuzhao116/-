package com.example.demo.threadLocalTest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Test {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        log.info("test");
//        ThreadLocal<String> testThreadLocal = new ThreadLocal<>();
//
//        testThreadLocal.set("test");
//        log.info(testThreadLocal.get());
//        Thread thread = new Thread(() -> {
//            log.info(testThreadLocal.get());
//        });
//        thread.start();

//
        InheritableThreadLocal<String> local =
                new InheritableThreadLocal<>();

        ExecutorService pool = Executors.newFixedThreadPool(1);

        try {
            local.set("用户 A");

            // 默认线程工厂在这里创建工作线程，继承 A
            pool.submit(() -> {
                System.out.println(local.get()); // 用户 A
                local.remove();
            }).get();

            // 复用已有工作线程，不会重新继承 B
            pool.submit(() -> {
                System.out.println(local.get()); // 还是用户 A！
            }).get();

            System.out.println(local.get());
        } finally {
//            local.remove();
            pool.shutdown();
        }

    }
}
