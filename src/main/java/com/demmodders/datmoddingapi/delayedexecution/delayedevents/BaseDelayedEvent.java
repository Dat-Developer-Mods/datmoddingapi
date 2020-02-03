package com.demmodders.datmoddingapi.delayedexecution.delayedevents;

import com.demmodders.datmoddingapi.interfaces.IDelayedEventInterface;

public class BaseDelayedEvent implements IDelayedEventInterface {

    /**
     * The time after which the event can be executed
     */
    private long exeTime;

    public BaseDelayedEvent(int Delay){
        // Calculate the time after which we can execute, so we don't calculate it every time we check it
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
