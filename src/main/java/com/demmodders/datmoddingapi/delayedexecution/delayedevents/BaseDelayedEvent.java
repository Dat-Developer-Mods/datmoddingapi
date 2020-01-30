package com.demmodders.datmoddingapi.delayedexecution.delayedevents;

import com.demmodders.datmoddingapi.interfaces.IDelayedEventInterface;

public class BaseDelayedEvent implements IDelayedEventInterface {

    private long exeTime;

    public BaseDelayedEvent(int Delay){
        exeTime = System.currentTimeMillis() + (Delay * 1000);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean canExecute() {
        return System.currentTimeMillis() >= exeTime;
    }

    @Override
    public boolean shouldRequeue(boolean hasFinished) {
        return !hasFinished;
    }
}
