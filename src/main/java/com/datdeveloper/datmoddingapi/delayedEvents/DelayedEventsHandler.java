package com.datdeveloper.datmoddingapi.delayedEvents;

import com.datdeveloper.datmoddingapi.DatConfig;
import com.datdeveloper.datmoddingapi.Datmoddingapi;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A system for delaying the execution of events until a condition is met
 * <p>DelayedEvents <b>do not</b> execute in a different thread, rather they on the main server tick after their {@link IDelayedEvent#canExecute()} function tests true.</p>
 * <p>For executing tasks on another thread, see {@link com.datdeveloper.datmoddingapi.concurrentTask.ConcurrentHandler}.</p>
 * <p>For an example of a delayed event, see {@link DelayedTeleportEvent}</p>
 *
 * @see IDelayedEvent
 * @see TimeDelayedEvent
 */
@Mod.EventBusSubscriber(modid = Datmoddingapi.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DelayedEventsHandler {
    // Singleton Stuff
    private static final DelayedEventsHandler INSTANCE = new DelayedEventsHandler();

    private final Queue<IDelayedEvent> eventQueue = new ArrayDeque<>();

    /**
     * Add a DelayedEvent to the Delay Queue
     * @see IDelayedEvent
     * @param event The event being added to the queue
     */
    public static void addEvent(final IDelayedEvent event) {
        INSTANCE.eventQueue.add(event);
    }

    /**
     * A tick event to execute delayed events
     * @param event The server tick event
     */
    @SubscribeEvent
    public static void onTick(final TickEvent.ServerTickEvent event) {
        for (int dummy = 0; dummy < DatConfig.getDelayedEventsPerTick() && !INSTANCE.eventQueue.isEmpty(); ++dummy) {
            boolean executed = false;
            final IDelayedEvent nextEvent = INSTANCE.eventQueue.remove();

            if (nextEvent.canExecute()) {
                nextEvent.execute();
                executed = true;
            }

            if (nextEvent.shouldRequeue(executed)) {
                INSTANCE.eventQueue.add(nextEvent);
            }
        }
    }

}
