package com.example.demo11;

public class FeedPetNode extends AbstractStractegyRounter<String, PetContext, String> {

    @Override
    protected String doApply(String petName, PetContext context) {
        System.out.println("【节点3：执行喂食】");
        String res = "投喂成功！" + petName + " 吃：" + context.getFood();
        // 返回非null，链路结束
        return res;
    }

    @Override
    public StrategyHandler<String, PetContext, String> getNext(String petName, PetContext context){
        // 没有下一个节点
        return null;
    }
}
