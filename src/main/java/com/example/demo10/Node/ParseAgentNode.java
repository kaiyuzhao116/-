package com.example.demo10.Node;

import com.example.demo10.AbstractegyRouter;
import com.example.demo10.AgentContext;
import com.example.demo10.StrategyHandler;

public class ParseAgentNode extends AbstractegyRouter<String, AgentContext,String> {


    @Override
    protected String doApply(String requestParameter, AgentContext dynamicContext) throws Exception {
        System.out.println(
                "解析Agent节点执行"
        );
        if (requestParameter == null || requestParameter.isBlank()){
            return "400:请求内容为空";
        }
        dynamicContext.setAgentRawText(requestParameter);

        return null;


    }

    @Override
    public StrategyHandler<String, AgentContext, String> getNext(String requestParameter, AgentContext dynamicContext) throws Exception {
        // 路由：固定下一个节点是识别意图节点；这里也可以写动态判断分支，变成策略树
        return new RecognizeIntentNode();
    }
}
