package com.example.demo10;
/**
 * 策略处理器顶层接口
 * @param <I> Input 请求入参
 * @param <C> Context 上下文
 * @param <O> Output 返回结果
 */
public interface StrategyHandler <I,C,O>{

    O apply(I requestParameter, C dynamicContext);

}
