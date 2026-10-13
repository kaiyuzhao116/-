package com.domain.agent.model.entity;
/**
 * Agent options.
 * 1. `maxTokens`
 * 限制对话上下文总长度，防止上下文过长超限报错；
 * 内置校验：`maxTokens <=0` 直接抛非法参数，必须是正整数。
 * 2. `reasoningEffort`
 * 控制模型思考深度，仅支持支持深度思考的大模型；空 / 空白不修改配置。
 * 3. `approvalMode ApprovalModeVO.REQUEST_APPROVAL`
 * 默认审批模式：调用高危工具时**需要人工审批**，对应前面讲的 `RuntimeApprovalBroker` 运行期审批逻辑。
 */

public record AgentOptions(
        String channelCode,       // 渠道唯一编码，区分多套模型配置
        String provider,          // 大模型厂商：deepseek、bailian、openai 等
        String model,             // 模型名称：qwen3.5-max、deepseek-v3 等
        Integer maxTokens,        // 单次对话最大上下文token上限
        String reasoningEffort,   // 思考强度：low/medium/high，控制模型思维链深度
        String baseUrl,           // 模型接口请求地址
        String apiKey,            // 模型鉴权密钥
        String protocol,          // 通信协议：兼容openai、claude协议等
        ApprovalModeVO approvalMode // 运行期工具审批模式
) {
    //不传思考强度，自动填`null`，审批模式默认「需要人工审批」。
    public AgentOptions(String channelCode, String provider, String model, Integer maxTokens,
                        String baseUrl, String apiKey, String protocol) {
        this(channelCode, provider, model, maxTokens, null, baseUrl, apiKey, protocol,
                ApprovalModeVO.REQUEST_APPROVAL);
    }

    //自定义思考强度，审批依旧默认人工审批。
    public AgentOptions(String channelCode, String provider, String model, Integer maxTokens,
                        String reasoningEffort, String baseUrl, String apiKey, String protocol) {
        this(channelCode, provider, model, maxTokens, reasoningEffort, baseUrl, apiKey, protocol,
                ApprovalModeVO.REQUEST_APPROVAL);
    }

    //紧凑构造器（record 专属校验逻辑）
    //内置校验：`maxTokens <=0` 直接抛非法参数，必须是正整数。
    //这是 Java Record 独有的**紧凑构造器**，不需要写参数列表，会在所有重载构造器执行完、正式给字段赋值**之前**自动执行。
    //不管你调用哪个构造方法创建 `AgentOptions` 对象，这段校验代码一定会跑一遍。
    //
    //## 2. 代码逻辑翻译
    //
    //- `maxTokens != null`：用户传了最大 token 值，不是空
    //- `maxTokens <= 0`：传入的值是 0 或者负数（比如 0、-2000、-5000）
    //- 满足两个条件 → 直接抛出非法参数异常，**拒绝创建这个配置对象**
    //
    //报错提示：`maxTokens must be a positive integer`，意思是「最大 Token 必须是正整数」。
    //对象创建时自动校验：最大 token 不能小于等于 0，非法配置直接拦截，避免下游调用模型报错。
    public AgentOptions {
        if (maxTokens != null && maxTokens <= 0) {
            throw new IllegalArgumentException("maxTokens must be a positive integer");
        }
    }


    //1. withReasoningEffort 修改思考强度
    public AgentOptions withReasoningEffort(String value) {
        if (value == null || value.isBlank()) {
            return this; // 空值不改动，返回原对象
        }
        // 仅替换reasoningEffort，其余所有配置原样拷贝，生成全新AgentOptions
        return new AgentOptions(channelCode, provider, model, maxTokens,
                value.trim().toLowerCase(), baseUrl, apiKey, protocol, approvalMode);
    }



    //withApprovalMode 修改工具审批模式
    public AgentOptions withApprovalMode(ApprovalModeVO mode) {
        if (mode == null) {
            return this;
        }
        // 只替换审批模式，其他配置不变，返回新配置对象
        return new AgentOptions(channelCode, provider, model, maxTokens,
                reasoningEffort, baseUrl, apiKey, protocol, mode);
    }






}
