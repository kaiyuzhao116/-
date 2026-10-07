package com.example.agent;

public sealed interface StreamChunk {
    // 文本增量，AI打字的一小段
    record TextDelta(String delta) implements StreamChunk {}
    // 工具调用块增量
    record ToolCallDelta(String jsonFragment) implements StreamChunk {}
    // 重试提示
    record Retry(String msg) implements StreamChunk {}
}
