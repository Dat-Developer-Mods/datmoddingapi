package com.demmodders.datmoddingapi.interfaces;

public interface IDelayedEventInterface {
    void execute();
    boolean canExecute();
    boolean shouldRequeue(boolean hasFinished);
}
