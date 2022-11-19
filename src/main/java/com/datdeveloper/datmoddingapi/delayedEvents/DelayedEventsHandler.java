package com.datdeveloper.datmoddingapi.delayedEvents;

import com.datdeveloper.datmoddingapi.DatConfig;
import com.datdeveloper.datmoddingapi.Datmoddingapi;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A system for delaying the execution of events
 * <p>DelayedEvents <b>do not</b> execute in a different thread, rather they on the main server tick after their {@link IDelayedEvent#canExecute()} function tests true.</p>
 * <p>For executing tasks on another thread, see {@link com.datdeveloper.datmoddingapi.asyncTask.AsyncHandler}.</p>
 * <p>For an example of a delayed event, see {@link DelayedTeleportEvent}</p>
 *
 * @see IDelayedEvent
 * @see BaseDelayedEvent
 */
@Mod.EventBusSubscriber(modid = Datmoddingapi.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DelayedEventsHandler {
    // Singleton Stuff
    private static final DelayedEventsHandler instance = new DelayedEventsHandler();

    private final Queue<IDelayedEvent> eventQueue = new ArrayDeque<>();

    /**
     * Add a DelayedEvent to the Delay Queue
     * @param event The event being added to the queue
     * @see IDelayedEvent
     */
    public static void addEvent(IDelayedEvent event) {
        instance.eventQueue.add(event);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        for (int dummy = 0; dummy < DatConfig.getDelayedEventsPerTick() && !instance.eventQueue.isEmpty(); ++dummy) {
            boolean executed = false;
            IDelayedEvent nextEvent = instance.eventQueue.remove();

            if (nextEvent.canExecute()) {
                nextEvent.execute();
                executed = true;
            }

            if (nextEvent.shouldRequeue(executed)) {
                instance.eventQueue.add(nextEvent);
            }
        }
    }

}
