package com.example.demo11;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractStractegyRounter<I,C,O> implements StrategyHandler<I,C,O> {

    protected abstract O doApply(I petName, C context);

    protected abstract StrategyHandler<I,C,O> getNext(I petName, C context);


    //final 避免子类重写该方法
    @Override
    public final O apply(I petName, C context){
        O o = doApply(petName, context);
        if (o != null){
            return o;
        }
        StrategyHandler<I,C,O> next = getNext(petName, context);
        if (next == null){
            return null;
        }

        return next.apply(petName, context);
    }
}
