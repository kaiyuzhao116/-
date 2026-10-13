package com.domain.agent.model.valobj;


/**
 * // 示例1：前端传横杠小写
 * ApprovalModeVO.from("request-approval"); // REQUEST_APPROVAL
 * // 示例2：传空字符串，兜底默认人工审批
 * ApprovalModeVO.from(""); // REQUEST_APPROVAL
 * // 示例3：传全开放
 * ApprovalModeVO.from("full-open"); // FULL_OPEN
 */
//1. **REQUEST_APPROVAL（默认模式）**
//任意高危工具（fs_edit、shell 等）触发调用时，阻塞 Agent，弹出前端审批窗口，等待人工点同意 / 拒绝，对应 `IRuntimeApprovalBroker` 阻塞等待逻辑。
//2. **AUTO_APPROVE**
//检测到高危工具，**自动直接放行**，不阻塞、不弹出审批窗口，跳过人工裁决流程，工具立刻执行。
//3. **FULL_OPEN**
//完全放开管控，不做任何工具风险拦截，所有工具调用全部直接执行，跳过运行期 Gate 校验逻辑。
public enum ApprovalModeVO {
    REQUEST_APPROVAL, // 调用高危工具必须人工审批（默认）
    AUTO_APPROVE,     // 自动放行，不用人工确认
    FULL_OPEN;        // 全部工具无限制，所有工具直接执行



    //前端传参格式不统一，做统一格式化适配：
    //
    //1. 空值兜底：没传审批模式，默认强制人工审批，保障安全；
    //2. 兼容小写、横杠写法：
    //   - 前端传 `request-approval` → 替换为 `REQUEST_APPROVAL`，正常匹配枚举；
    //   - 前端传 `auto_approve` / `AutoApprove` 都能正常解析；
    public static ApprovalModeVO from(String value) {
        // 1. 空字符串/空白 → 默认走人工审批
        if (value == null || value.isBlank()) {
            return REQUEST_APPROVAL;
        }
        // 2. 格式化入参：去空格 → 全大写 → 横杠替换下划线
        return valueOf(value.trim().toUpperCase().replace('-', '_'));
    }

}
