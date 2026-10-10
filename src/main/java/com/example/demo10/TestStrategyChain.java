package com.example.demo10;


import com.example.demo10.Node.ParseAgentNode;


public class TestStrategyChain {


    public static void main(String[] args){
        // 起点：第一个节点
        StrategyHandler<String, AgentContext, String> startNode = new ParseAgentNode();
        AgentContext ctx = new AgentContext();

        // case1：正常场景
        String res1 = startNode.apply("帮我查订单", ctx);
        System.out.println("返回结果1：" + res1);
        System.out.println("========");

        // case2：无法识别意图场景，会在识别节点短路
        AgentContext ctx2 = new AgentContext();
        String res2 = startNode.apply("随便闲聊", ctx2);
        System.out.println("返回结果2：" + res2);
        System.out.println("========");

        // case3：空参数，在解析节点短路
        AgentContext ctx3 = new AgentContext();
        String res3 = startNode.apply("", ctx3);
        System.out.println("返回结果3：" + res3);
    }
}
