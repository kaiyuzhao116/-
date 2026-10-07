package com.example.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReactLoopAgent {
    private Phase phase = new Phase.Idle();
    private final BlockingQueue<String> inbox = new LinkedBlockingQueue<>();
    private final ILlmRuntimePort llmPort = new MockLlmRuntimePort();
    private final BlockAssembler assembler = new BlockAssembler();
    public static final long MAX_STEPS_PER_TURN = 50;
    public static final int MAX_TOKEN_CONTINUATIONS = 4;
    
    // 线程池提为类字段：避免每次 kick() 都 new 一个且从不关闭导致泄漏
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    //===================== kick：顶层驱动器 =====================
    public CompletableFuture<Void> kick() {
        return CompletableFuture.runAsync(() -> {
            while (true) {
                if (phase instanceof Phase.Running r && r.abort().get()) {
                    break;
                }
                if (inbox.isEmpty()) {
                    break;
                }
                boolean ok = turn();
                if (!ok) break;
            }
            // 跑完一轮，如果还有消息，递归kick，和文档一致
            if (!inbox.isEmpty()) {
                kick();
            } else {
                // inbox 跑空后状态回 Idle，否则 phase 停在 Running，后续 cancel/kick 判断都失真
                phase = new Phase.Idle();
            }
        }, executor);
    }

    //===================== turn：一轮会话 =====================
    private boolean turn() {
        phase = new Phase.Running(new AtomicBoolean(false));
        System.out.println("[TurnStart]");
        boolean midTurnContinuation = false;
        long stepCount = 0;
        while (stepCount < MAX_STEPS_PER_TURN) {
            StepResult stepResult = step();
            stepCount++;
            if (stepResult.kind == StepResult.Kind.CONTINUE) {
                midTurnContinuation = true;
                continue;
            } else {
                break;
            }
        }
        System.out.println("[TurnEnd]");
        return true;
    }

    //===================== step：单次LLM流式调用 =====================
    private StepResult step() {
        if (!(phase instanceof Phase.Running running)) {
            return new StepResult(StepResult.Kind.COMPLETED);
        }
        assembler.reset();
        String prompt = inbox.poll();
        if(prompt == null) return new StepResult(StepResult.Kind.COMPLETED);

        CountDownLatch doneLatch = new CountDownLatch(1);
        Flow.Publisher<StreamChunk> publisher = llmPort.stream(prompt);

        // 订阅流
        publisher.subscribe(new Flow.Subscriber<>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription s) {
                this.subscription = s;
                s.request(Long.MAX_VALUE);
            }

            // 每收到一块chunk onNext
            @Override
            public void onNext(StreamChunk chunk) {
                // 1. 检查abort 协作取消
                if(running.abort().get()){
                    subscription.cancel();
                    return;
                }
                // 2. 组装chunk
                assembler.push(chunk);
                // 3. 模拟SSE推前端
                if(chunk instanceof StreamChunk.TextDelta td){
                    System.out.println("[SSE推前端]:"+td.delta());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                doneLatch.countDown();
            }

            @Override
            public void onComplete() {
                doneLatch.countDown();
            }
        });

        // ====== 100ms轮询等待流结束（文档代码） ======
        boolean done = false;
        try {
            while (!doneLatch.await(100, TimeUnit.MILLISECONDS)) {
                if(running.abort().get()){
                    System.out.println("[step 检测到abort，取消]");
                    done = true;
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 流结束，拿到组装后的完整消息
        List<String> blocks = assembler.blocks();
        System.out.println("[组装完成 blocks]:"+blocks);
        // 判断是否是工具调用块，如果是，返回CONTINUE，继续下一轮step
        boolean hasToolCall = blocks.stream().anyMatch(b -> b.startsWith("ToolCallBlock"));
        if(hasToolCall){
            System.out.println("检测工具调用，放回inbox继续step");
            inbox.offer("工具返回结果");
            return new StepResult(StepResult.Kind.CONTINUE);
        }
        return new StepResult(StepResult.Kind.COMPLETED);
    }

    //===================== 协作式取消 cancel =====================
    public synchronized void cancel(boolean keepInbox){
        if(phase instanceof Phase.Running r){
            r.abort().set(true);
        }
        // 取消后状态回 Idle，step 检测到 abort 退出后，重新 kick() 还能继续消费保留的消息
        phase = new Phase.Idle();
        if(!keepInbox){
            inbox.clear();
        }
    }

    // 关闭后台线程池（程序退出或不再使用 agent 时调用）
    public void shutdown(){
        executor.shutdown();
    }

    // 投递消息进入inbox
    public void sendMessage(String msg){
        inbox.offer(msg);
    }

    // step返回结果类型
    public record StepResult(Kind kind){
        public enum Kind { CONTINUE, COMPLETED, ABORTED }
    }
}
