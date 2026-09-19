package com.example.demo.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 线程池实验台：验证 @Async 走的是哪个线程池
 */
@Slf4j
@Service
public class ThreadDemoService {

    /**
     * 标了 @Async → 方法体会被切到 ThreadPoolConfig.getAsyncExecutor() 提供的池里执行
     */
    @Async
    public void asyncHello() {
        log.info("【asyncHello】正在执行，当前线程: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(2000);// 模拟耗时任务
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("【asyncHello】执行完毕，当前线程: {}", Thread.currentThread().getName());
    }

    /**
     * 故意在异步任务里抛一个没人 catch 的异常，观察兜底 handler
     */
    @Async
    public void asyncBoom() {
        log.info("【asyncBoom】即将抛异常，当前线程: {}", Thread.currentThread().getName());
        throw new RuntimeException("模拟@Async任务里的漏网异常");
    }
}
