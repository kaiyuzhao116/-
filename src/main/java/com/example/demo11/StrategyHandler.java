package com.example.demo11;

public interface StrategyHandler <I, C, O> {
    O apply(I petName, C context);
}
