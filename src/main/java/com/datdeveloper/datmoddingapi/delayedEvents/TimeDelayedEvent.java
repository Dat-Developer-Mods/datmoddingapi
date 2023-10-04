package com.datdeveloper.datmoddingapi.delayedEvents;

/**
 * An abstract implementation of {@link IDelayedEvent} that waits until the given delay has passed before executing
 */
public abstract class TimeDelayedEvent implements IDelayedEvent {
    /**
     * The time after which the event can be executed
     */
    protected long exeTime;

    /**
     * @param delay The amount of time in seconds before the delayed event will execute
     */
    protected TimeDelayedEvent(final int delay) {
        // Calculate the time after which we can execute, so we don't calculate it every time we check it
        exeTime = System.currentTimeMillis() + (delay * 1000L);
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
