package com.datdeveloper.datmoddingapi.delayedEvents;

import com.datdeveloper.datmoddingapi.DatConfig;
import com.datdeveloper.datmoddingapi.Datmoddingapi;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.Queue;

@Mod.EventBusSubscriber(modid = Datmoddingapi.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DelayedEventsQueue {
    // Singleton Stuff
    private static final DelayedEventsQueue instance = new DelayedEventsQueue();

    private Queue<IDelayedEvent> eventQueue = new ArrayDeque<>();

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
