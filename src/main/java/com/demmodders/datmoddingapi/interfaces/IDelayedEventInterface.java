package com.demmodders.datmoddingapi.interfaces;

public interface IDelayedEventInterface {
    /**
     * The code that will execute when the delay has passed
     */
    void execute();

    /**
     * The check to ensure the conditions are correct for the
     * @return True if the conditions are right to execute this delayed event
     */
    boolean canExecute();

    /**
     * Checks to see if the code should be requeued to continue waiting to be executed
     * Can be used to restart the delay for the event
     * @param hasFinished True if the event has executed
     * @return True if the event should be requeued
     */
    boolean shouldRequeue(boolean hasFinished);
}
