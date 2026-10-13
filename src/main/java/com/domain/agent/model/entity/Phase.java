package com.domain.agent.model.entity;

import java.util.concurrent.atomic.AtomicBoolean;

public sealed interface Phase permits Phase.Idle, Phase.Maintenance, Phase.Running {


    /**
     * - 含义：当前 Agent**没有任何正在执行的任务**，对话跑完、维护任务全部结束。
     * - 字段 `lastTurn`：记录上一轮完整对话的轮次编号，用来做会话重放、上下文计数。
     * - 此时可以直接接收唤醒消息，立刻开启新一轮`Running`对话。
     * @param lastTurn
     */
    record Idle(long lastTurn) implements Phase {}

    /**
     * - Agent 不跑用户对话，在执行后台维护任务（日志落盘、会话持久化、缓存清理等），不会处理用户提问。
     * - 字段解释：
     *   1. `AtomicBoolean abort`：原子中断标记，用户点击停止时置为 true，通知维护任务终止；
     *   2. `lastTurn`：上一轮对话编号；
     *   3. `AtomicBoolean wakeRequested`：原子唤醒请求标记，维护期间如果来了唤醒消息，先存这个标记，
     *   **等维护结束切回 Idle 再补发唤醒执行对话。
     * @param abort
     * @param lastTurn
     * @param wakeRequested
     */
    record Maintenance(
            AtomicBoolean abort,
            long lastTurn,
            AtomicBoolean wakeRequested
    ) implements Phase {
        // 便捷构造，默认abort=false、无唤醒请求
        public Maintenance(long lastTurn) {
            this(new AtomicBoolean(false), lastTurn, new AtomicBoolean(false));
        }
    }


    /**
     * - 含义：当前 Agent 正在执行用户对话。
     * - 字段解释：
     *   1. `AtomicBoolean abort`：原子中断标记，用户点击停止时置为 true，通知对话任务终止；
     *   2. `long turn`：当前对话编号；
     *   3. `long step`：当前对话的轮次编号；
     *   4. `AtomicBoolean wakeRequested`：原子唤醒请求标记，对话期间如果来了唤醒消息，先存这个标记，
     *   **等对话结束切回 Idle 再补发唤醒执行对话。
     * @param abort
     * @param turn
     * @param step
     * @param wakeRequested
     */
    record Running(
            AtomicBoolean abort,
            long turn,
            long step,
            AtomicBoolean wakeRequested
    ) implements Phase {
        // 初始化新一轮运行，step从0开始，无中断、无唤醒请求
        public Running(long lastTurn) {
            this(new AtomicBoolean(false), lastTurn, 0, new AtomicBoolean(false));
        }
    }


}
