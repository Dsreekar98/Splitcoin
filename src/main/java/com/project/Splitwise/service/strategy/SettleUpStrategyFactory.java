package com.project.Splitwise.service.strategy;

public class SettleUpStrategyFactory {
    // TODO: 29-10-2023 add enum for different strategies
    public static SettleUpStrategy getSettUpStrategy(){
        return new HeapBasedSettleUpStrategy();
    }
}
