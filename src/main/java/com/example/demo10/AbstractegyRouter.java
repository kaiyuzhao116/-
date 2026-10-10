package com.example.demo10;

public abstract class AbstractegyRouter<I, C, O> implements StrategyHandler<I, C, O> {
    /**
     * 节点自身业务逻辑，子类实现
     * @return 返回非null代表链路短路终止；返回null继续往下走
     */
    protected abstract O doApply(I requestParameter, C dynamicContext) throws Exception;


    /**
     * 路由决策：根据请求+上下文，动态获取下一个节点
     */
    public abstract StrategyHandler<I,C,O> getNext(I requestParameter, C dynamicContext) throws Exception;


    /**
     * final 禁止子类重写，固定链路骨架模板方法
     * 内部捕获节点抛出的受检异常，转成错误码返回，调用方无需 try-catch
     */
    @Override
    @SuppressWarnings("unchecked")
    public final O apply(I req, C ctx) {
        try {
            O result = doApply(req, ctx);
            //非null 短路，直接返回，终止链路
            if (result != null){
                return result;
            }
            //获取下一个节点，递归执行
            StrategyHandler<I, C, O> next = getNext(req, ctx);
            if (next == null){
                return null;
            }
            return next.apply(req, ctx);
        } catch (Exception e) {
            // 本 demo 中泛型 O 实际均为 String，故强转返回错误码；
            // 若 O 非 String 的子类链路复用此基类，需改为注入自定义异常转换器
            System.out.println("【链路异常】" + getClass().getSimpleName() + " 执行出错");
            e.printStackTrace();
            return (O) ("500:节点执行异常：" + e.getMessage());
        }
    }



}
