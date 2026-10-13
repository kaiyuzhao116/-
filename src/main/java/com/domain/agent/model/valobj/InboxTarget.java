package com.domain.agent.model.valobj;

public enum InboxTarget {

    NEXT_TURN("next-turn"),
    NEXT_STEP("next-step");


    /**
     * The wire name.
     * - `wireName`：网络传输别名，后端和前端通信只用简短字符串，不传递 Java 枚举类名。
     *   - NEXT_STEP 对应前端字符串 `"next-step"`
     *   - NEXT_TURN 对应前端字符串 `"next-turn"`
     * - 构造器私有，枚举初始化时自动绑定传输字符串。
     */
    private final String wireName;

    InboxTarget(String wireName) {
        this.wireName = wireName;
    }
    /**
     * Get the wire name.
     * - `wireName`：网络传输别名，后端和前端通信只用简短字符串，不传递 Java 枚举类名。
     *   - NEXT_STEP 对应前端字符串 `"next-step"`
     *   - NEXT_TURN 对应前端字符串 `"next-turn"`
     * - 构造器私有，枚举初始化时自动绑定传输字符串。
     */
    public String wireName() {
        return wireName;
    }

    /**
     * 功能：前端传字符串，后端转回枚举，同时做合法性校验
     * 1. 遍历全部枚举，匹配传入的`wireName`字符串
     * 2. 匹配成功返回对应枚举；
     * 3. 前端传不存在的字符串（比如`"abc"`），直接抛非法参数异常，拦截非法消息分类。
     * 示例：
     * `InboxTarget.fromWireName("next-turn")` → 返回 `InboxTarget.NEXT_TURN`
     */
    public static InboxTarget fromWireName(String name) {
        for (InboxTarget t : values()) {
            if (t.wireName.equals(name)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown inbox target: " + name);
    }


}
