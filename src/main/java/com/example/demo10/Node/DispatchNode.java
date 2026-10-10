package com.example.demo10.Node;

import com.example.demo10.AbstractegyRouter;
import com.example.demo10.AgentContext;
import com.example.demo10.StrategyHandler;

public class DispatchNode extends AbstractegyRouter<String, AgentContext, String> {

    @Override
    protected String doApply(String requestParameter, AgentContext dynamicContext) throws Exception {
        System.out.println("【分发节点】执行");
        String intent = dynamicContext.getIntent();
        String result = "分发成功，意图：" + intent;
        // 这里返回非null，链路结束
        return result;
    }

    @Override
    public StrategyHandler<String, AgentContext, String> getNext(String requestParameter, AgentContext dynamicContext) throws Exception {
        // 最后一个节点，没有下一个
        return null;
    }
}
