package com.example.agent;

import java.util.ArrayList;
import java.util.List;

public class BlockAssembler {
    private final StringBuilder textBuffer = new StringBuilder();
    private final StringBuilder toolCallBuffer = new StringBuilder();
    private final List<String> blocks = new ArrayList<>();

    public void push(StreamChunk chunk) {
        // Java 17 不支持 switch 模式匹配（Java 21 才转正），用 instanceof 链代替
        if (chunk instanceof StreamChunk.TextDelta delta) {
            textBuffer.append(delta.delta());
        } else if (chunk instanceof StreamChunk.ToolCallDelta json) {
            toolCallBuffer.append(json.jsonFragment());
        } else if (chunk instanceof StreamChunk.Retry retry) {
            blocks.add("[Retry]" + retry.msg());
        }
    }

    // 流结束后，取出组装完成的消息
    public List<String> blocks() {
        if(textBuffer.length()>0){
            blocks.add("TextBlock:" + textBuffer);
        }
        if(toolCallBuffer.length()>0){
            blocks.add("ToolCallBlock:" + toolCallBuffer);
        }
        return blocks;
    }

    public void reset(){
        textBuffer.setLength(0);
        toolCallBuffer.setLength(0);
        blocks.clear();
    }
}
