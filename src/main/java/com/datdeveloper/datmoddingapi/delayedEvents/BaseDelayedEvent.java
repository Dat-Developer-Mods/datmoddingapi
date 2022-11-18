package com.datdeveloper.datmoddingapi.delayedEvents;

public abstract class BaseDelayedEvent implements IDelayedEvent {
    /**
     * The time after which the event can be executed
     */
    protected long exeTime;

    public BaseDelayedEvent(int Delay){
        // Calculate the time after which we can execute, so we don't calculate it every time we check it
        exeTime = System.currentTimeMillis() + (((long) Delay) * 1000L);
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
