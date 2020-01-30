package com.demmodders.datmoddingapi.delayedexecution;

import com.demmodders.datmoddingapi.DatModdingAPIConfig;
import com.demmodders.datmoddingapi.interfaces.IDelayedEventInterface;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Handler;

@Mod.EventBusSubscriber
public class DelayHandler {
    // Singleton
    private static DelayHandler instance;
    public static DelayHandler getInstance(){
        if (instance == null){
            instance = new DelayHandler();
        }
        return instance;
    }

    private Queue<IDelayedEventInterface> executionQueue = new ArrayDeque<>();

    public static void addEvent(IDelayedEventInterface event){
        DelayHandler.getInstance().executionQueue.add(event);
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.WorldTickEvent event) {
        DelayHandler handler = DelayHandler.getInstance();
        if (!handler.executionQueue.isEmpty()){
            boolean finished;
            for(int dummy = 0; dummy < DatModdingAPIConfig.delayedEventsPerTick && dummy < handler.executionQueue.size(); dummy++) {
                finished = false;
                IDelayedEventInterface delayedEvent = handler.executionQueue.remove();
                if (delayedEvent.canExecute()) {
                    delayedEvent.execute();
                    finished = true;
                }
                if (delayedEvent.shouldRequeue(finished)){
                    handler.executionQueue.add(delayedEvent);
                }
            }
        }
    }
}
