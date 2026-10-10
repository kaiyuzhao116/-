package com.example.demo10;

import lombok.Data;

@Data
public class AgentContext {
    // 存放中间状态，可自由加字段
    private String agentRawText;
    private String intent;
    private Long userId;
}
