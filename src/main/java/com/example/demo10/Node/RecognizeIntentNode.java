package com.example.demo10.Node;

import com.example.demo10.AbstractegyRouter;
import com.example.demo10.AgentContext;
import com.example.demo10.StrategyHandler;

public class RecognizeIntentNode extends AbstractegyRouter<String, AgentContext, String> {

    @Override
    protected String doApply(String requestParameter, AgentContext dynamicContext) throws Exception {
        System.out.println("【识别意图节点】执行");
        String rawText = dynamicContext.getAgentRawText();
        // 模拟识别意图
        if(rawText.contains("订单")){
            dynamicContext.setIntent("QUERY_ORDER");
        }else if(rawText.contains("退款")){
            dynamicContext.setIntent("REFUND");
        }else{
            // 识别不到意图，短路返回404
            return "404:无法识别意图";
        }
        return null;
    }

    @Override
    public StrategyHandler<String, AgentContext, String> getNext(String requestParameter, AgentContext dynamicContext) throws Exception {
        // 路由到分发节点
        return new DispatchNode();
    }
}
