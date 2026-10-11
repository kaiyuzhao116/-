package com.example.demo11;

public class IdentifyPetNode extends AbstractStractegyRounter<String, PetContext, String> {
    @Override
    protected String doApply(String petName, PetContext context) {
        System.out.println("节点一：识别宠物名：");
        if(petName.contains("猫") || petName.contains("狗")){
            context.setPetType(petName);
            return "识别宠物名成功";
        }else {
            return "识别宠物名失败";
        }
    }

    @Override
    protected StrategyHandler<String, PetContext, String> getNext(String petName, PetContext context) {
        return new MatchFoodNode();
    }
}
