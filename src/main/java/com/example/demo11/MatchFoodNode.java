package com.example.demo11;

public class MatchFoodNode extends AbstractStractegyRounter<String, PetContext, String> {

    @Override
    protected String doApply(String petName, PetContext context){
        System.out.println("【节点2：匹配食物】");
        String type = context.getPetType();
        if ("cat".equals(type)) {
            context.setFood("猫粮");
        } else if ("dog".equals(type)) {
            context.setFood("狗粮");
        }
        // 匹配完成，继续到喂食节点
        return null;
    }

    @Override
    public StrategyHandler<String, PetContext, String> getNext(String petName, PetContext context) {
        // 下一个节点：喂食
        return new FeedPetNode();
    }
}
