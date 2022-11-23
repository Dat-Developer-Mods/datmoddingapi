package com.datdeveloper.datmoddingapi.delayedEvents;

/**
 * An abstract implementation of {@link IDelayedEvent} that waits until the given delay has passed before executing
 */
public abstract class TimeDelayedEvent implements IDelayedEvent {
    /**
     * The time after which the event can be executed
     */
    protected long exeTime;

    public TimeDelayedEvent(final int Delay){
        // Calculate the time after which we can execute, so we don't calculate it every time we check it
        exeTime = System.currentTimeMillis() + (((long) Delay) * 1000L);
    }

    @Override
    public boolean canExecute() {
        return System.currentTimeMillis() >= exeTime;
    }

    @Override
    public boolean shouldRequeue(final boolean hasFinished) {
        return !hasFinished;
    }
}
