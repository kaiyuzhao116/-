package com.example.agent;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public interface ILlmRuntimePort {
    Flow.Publisher<StreamChunk> stream(String prompt);
}

// 模拟实现
class MockLlmRuntimePort implements ILlmRuntimePort {
    @Override
    public Flow.Publisher<StreamChunk> stream(String prompt) {
        SubmissionPublisher<StreamChunk> publisher = new SubmissionPublisher<>();
        // 开新线程，模拟LLM慢慢吐出chunk
        new Thread(() -> {
            try {
                if(prompt.contains("天气")){
                    // 模拟返回工具调用的chunk碎片
                    publisher.submit(new StreamChunk.ToolCallDelta("{\"name\":\"getWeather\",\"city\":\"北京"));
                    Thread.sleep(300);
                    publisher.submit(new StreamChunk.ToolCallDelta("\"}"));
                }else{
                    publisher.submit(new StreamChunk.TextDelta("你好"));
                    Thread.sleep(300);
                    publisher.submit(new StreamChunk.TextDelta("，这是流式返回"));
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            publisher.close();
        }).start();
        return publisher;
    }
}
