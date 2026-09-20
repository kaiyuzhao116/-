package com.example.demo.concurrent.threadlocal;

import com.example.demo.Config.ThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal 实验台：浏览器逐个点 /tl/* 观察现象
 * 玩法顺序：① → ② → ③(串线事故) → ④(跨线程失效)
 */
@Slf4j
@RestController
@RequestMapping("/tl")
public class ThreadLocalDemoController {

    private final ThreadPoolTaskExecutor jiwuExecutor;

    public ThreadLocalDemoController(@Qualifier(ThreadPoolConfig.JIWU_EXECUTOR) ThreadPoolTaskExecutor jiwuExecutor) {
        this.jiwuExecutor = jiwuExecutor;
    }

    // ============================================================
    // 实验①：线程隔离 —— set 和 get 落在不同 Tomcat 线程上会怎样
    // ============================================================

    /** ①a 在当前请求线程 set 一个值（故意不 remove），并告诉你线程名 */
    @GetMapping("/set")
    public String set(@RequestParam(defaultValue = "张三") String name) {
        String thread = Thread.currentThread().getName();
        UserContext.set(name);
        log.info("【/tl/set】线程 {} 写入了 {}", thread, name);
        return "线程[" + thread + "] 已写入 " + name + "（没 remove）。"
                + "现在连点多次 /tl/get，观察：命中同一线程才有值，否则是 null";
    }

    /** ①b 在（可能不同的）请求线程里 get，体会"每个线程一份、互不可见" */
    @GetMapping("/get")
    public String get() {
        String thread = Thread.currentThread().getName();
        String val = UserContext.get();
        log.info("【/tl/get】线程 {} 读到 {}", thread, val);
        return "线程[" + thread + "] 读到: " + val
                + "  → null 说明这个请求换了个 Tomcat 线程，没接到上一步的写入";
    }

    // ============================================================
    // 实验②：免传参的"隐形通道" —— Controller 深处 Service 都能 get 到
    // ============================================================

    /** ② Controller 里 set，然后调 Service，Service 不接任何参数却能拿到值 */
    @GetMapping("/chain")
    public String chain() {
        String thread = Thread.currentThread().getName();
        UserContext.set("当前登录用户-李四");
        try {
            // 注意：doBusiness 签名里没有任何 user 参数，全靠 ThreadLocal 传递
            String result = doBusinessWithoutPassingArg();
            log.info("【/tl/chain】{}", result);
            return result + "<br>（全程没给 Service 方法传参，值是靠 ThreadLocal 顺过去的）";
        } finally {
            UserContext.clear();
        }
    }

    /** 故意不接收 user 参数，内部直接 UserContext.get() */
    private String doBusinessWithoutPassingArg() {
        return "Service 层拿到: " + UserContext.get() + "（线程 " + Thread.currentThread().getName() + "）";
    }

    // ============================================================
    // 实验③：核心坑 —— 线程池复用 + 不 remove = 数据串线（生产事故复现）
    // ============================================================

    /**
     * ③a 污染：在池线程里 set 且【故意不 remove】，脏值就留在那个可复用的线程身上
     * 反复点这个接口，会看到脏值不断覆盖，但永远不清空
     */
    @GetMapping("/pollute")
    public String pollute(@RequestParam(defaultValue = "王五") String name) throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        jiwuExecutor.execute(() -> {
            String thread = Thread.currentThread().getName();
            UserContext.set(name + "的私密数据");
            log.warn("【/tl/pollute】线程 {} 写入脏值且未清理", thread);
            future.complete(thread);
        });
        String dirtyThread = future.get(3, TimeUnit.SECONDS);
        return "已在池线程[" + dirtyThread + "]写入脏值且【没 remove】。"
                + "下一步点 /tl/check，若被同一个线程处理就会读到别人的数据！";
    }

    /**
     * ③b 验收：只读 get()，看会不会读到上一次 pollute 残留的脏值
     * 因为线程池会复用线程，多点几次大概率读到 "王五的私密数据" —— 这就是身份串号事故
     */
    @GetMapping("/check")
    public String check() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        jiwuExecutor.execute(() -> {
            String thread = Thread.currentThread().getName();
            String leaked = UserContext.get();
            log.info("【/tl/check】线程 {} 读到: {}", thread, leaked);
            future.complete("线程[" + thread + "] 读到: " + leaked
                    + (leaked != null ? "  ⚠️ 串线了！这是上次 pollute 的残留" : "  ✅ 这次换到新线程，干净的"));
        });
        return future.get(3, TimeUnit.SECONDS) + "<br>多刷几次对比，然后去点 /tl/pollute-safe 修复它";
    }

    /** ③c 修复版：同样的任务，try-finally 里 remove()，脏值不再残留 */
    @GetMapping("/pollute-safe")
    public String polluteSafe(@RequestParam(defaultValue = "赵六") String name) throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        jiwuExecutor.execute(() -> {
            String thread = Thread.currentThread().getName();
            try {
                UserContext.set(name + "的数据");
                log.info("【/tl/pollute-safe】线程 {} 用完，finally 会清掉", thread);
            } finally {
                UserContext.clear(); // ← 关键一行
            }
            future.complete("线程[" + thread + "] 已 set+remove，再点 /tl/check 应该读不到脏值");
        });
        return future.get(3, TimeUnit.SECONDS);
    }

    // ============================================================
    // 实验④：跨线程拿不到 —— 普通 TL / Inheritable 在线程池下都失效
    // ============================================================

    /** ④a 主(Tomcat)线程 set，池线程 get → null，证明普通 ThreadLocal 不会跟任务过河 */
    @GetMapping("/async-empty")
    public String asyncEmpty() throws Exception {
        String parentThread = Thread.currentThread().getName();
        UserContext.set("主线程的值");
        try {
            CompletableFuture<String> future = new CompletableFuture<>();
            jiwuExecutor.execute(() -> {
                String childThread = Thread.currentThread().getName();
                String val = UserContext.get();
                log.info("【/tl/async-empty】父 {} 写了'主线程的值'，子 {} 读到 {}", parentThread, childThread, val);
                future.complete("父线程[" + parentThread + "]写了值，子线程[" + childThread + "]读到: " + val);
            });
            return future.get(3, TimeUnit.SECONDS) + "  → null 说明普通 ThreadLocal 不跨线程";
        } finally {
            UserContext.clear();
        }
    }

    /** ④b Inheritable 只对"set 之后新建的子线程"有效，线程池里复用线程 → 依然拿不到 */
    @GetMapping("/inherit")
    public String inherit() throws Exception {
        // 用【新建线程】验证：能继承
        UserContext.setInheritable("父线程的继承值");
        CompletableFuture<String> newborn = new CompletableFuture<>();
        new Thread(() -> newborn.complete("新建子线程读到: " + UserContext.getInheritable())).start();
        String newbornResult = newborn.get(3, TimeUnit.SECONDS);

        // 用【池里已存在的复用线程】验证：大概率读不到（线程早就建好了，不会重新复制）
        CompletableFuture<String> pooled = new CompletableFuture<>();
        jiwuExecutor.execute(() ->
                pooled.complete("线程池复用线程读到: " + UserContext.getInheritable()));
        String pooledResult = pooled.get(3, TimeUnit.SECONDS);

        UserContext.clearInheritable();
        return newbornResult + "<br>" + pooledResult
                + "<br>→ 新建线程能继承、池线程读不到(null)，因为 Inheritable 只在【线程创建那一刻】复制一次";
    }
}
