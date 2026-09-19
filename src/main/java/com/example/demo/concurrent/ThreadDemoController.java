package com.example.demo.concurrent;

import com.example.demo.Config.ThreadPoolConfig;
import com.example.demo.Config.factory.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线程池实验台入口：浏览器逐个点 URL 观察现象
 */
@Slf4j
@RestController
@RequestMapping("/thread")
public class ThreadDemoController {

    private final ThreadDemoService threadDemoService;

    /** 按 Bean 名字注入你配置的那个池 */
    private final ThreadPoolTaskExecutor jiwuExecutor;

    public ThreadDemoController(ThreadDemoService threadDemoService,
                                @Qualifier(ThreadPoolConfig.JIWU_EXECUTOR) ThreadPoolTaskExecutor jiwuExecutor) {
        this.threadDemoService = threadDemoService;
        this.jiwuExecutor = jiwuExecutor;
    }

    /** ① 对照组：不发异步，只看 Tomcat 处理请求的线程叫什么 */
    @GetMapping("/main")
    public String main() {
        String name = Thread.currentThread().getName();
        log.info("【/thread/main】处理请求的线程: {}", name);
        return "处理线程: " + name + " （对比一下它不是 jiwu-executor-*）";
    }

    /** ② @Async 生效：接口秒回，2秒后日志里出现 jiwu-executor-N */
    @GetMapping("/async")
    public String async() {
        log.info("【/thread/async】提交前，当前线程: {}", Thread.currentThread().getName());
        threadDemoService.asyncHello();
        return "任务已丢给线程池，接口先返回。盯日志：2秒后出现 jiwu-executor-N";
    }

    /** ③ 核心实验：裸 execute() 抛异常 → 应该触发你的 GlobalUncaughtExceptionHandler
     *  玩法：先在 uncaughtException 方法里打断点，再访问本接口 */
    @GetMapping("/boom")
    public String boom() {
        // getThreadPoolExecutor() 拿到的是 JDK 原生池，execute() 完全绕过 Spring 异常处理
        jiwuExecutor.getThreadPoolExecutor().execute(() -> {
            log.info("【boom】任务开始，当前线程: {}", Thread.currentThread().getName());
            throw new RuntimeException("模拟裸任务的漏网异常——看看谁会接住它");
        });
        return "任务已提交并会抛异常。若第45行工厂生效→断点命中/日志出现 'Exception in thread jiwu-executor-N'";
    }

    /** ④ 直接验收工厂本身：手动调一次 newThread，检查挂上去的 handler 是谁 */
    @GetMapping("/factory")
    public String factory() {
        Thread thread = new MyThreadFactory(jiwuExecutor).newThread(() -> {
        });
        boolean hooked = thread.getUncaughtExceptionHandler()
                instanceof com.example.demo.Config.handler.GlobalUncaughtExceptionHandler;
        return "工厂新造的线程名=" + thread.getName() + "，已挂全局兜底handler=" + hooked;
    }

    /** ⑤ 选做：压满 10线程+200队列，看 CallerRunsPolicy 把 Controller 线程也拖去干活
     *  用法：/thread/flood?n=215 （n 可任意改，超过 210 才会触发拒绝） */
    @GetMapping("/flood")
    public String flood(@RequestParam(defaultValue = "215") int n) {
        java.util.concurrent.atomic.AtomicInteger poolCount = new java.util.concurrent.atomic.AtomicInteger();
        java.util.concurrent.atomic.AtomicInteger callerCount = new java.util.concurrent.atomic.AtomicInteger();
        long start = System.currentTimeMillis();
        for (int i = 1; i <= n; i++) {
            final int no = i;
            jiwuExecutor.execute(() -> {
                String thread = Thread.currentThread().getName();
                if (thread.startsWith("jiwu-executor")) {
                    poolCount.incrementAndGet();
                } else {
                    // 不是池里的线程 = 被 CallerRunsPolicy 拉来干活的"提交方倒霉蛋"
                    callerCount.incrementAndGet();
                    log.warn("【flood】任务#{} 竟然由提交线程执行: {}", no, thread);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        long cost = System.currentTimeMillis() - start;
        return String.format("提交%d个任务耗时 %dms | 池线程执行 %d 个 | 提交线程(http-nio-*)被迫代干 %d 个",
                n, cost, poolCount.get(), callerCount.get());
    }
}
