package com.datdeveloper.datmoddingapi.concurrentTask;

import com.datdeveloper.datmoddingapi.DatConfig;
import com.datdeveloper.datmoddingapi.Datmoddingapi;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * A system for running tasks concurrently in another thread, optionally with a delay
 * <p>Concurrent Tasks <b>do</b> execute in another thread, and therefore can lead to problems with thread unsafety</p>
 * <p>For executing tasks with a delay but on the server thread (with everything else, nice and thread safely), use {@link com.datdeveloper.datmoddingapi.delayedEvents.DelayedEventsHandler}</p>
 *
 * @see Callable
 * @see Runnable
 */
@Mod.EventBusSubscriber(modid = Datmoddingapi.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ConcurrentHandler {
    private static final Logger logger = LogUtils.getLogger();
    private static final String NOT_READY_WARNING = "Something attempted to schedule a task before the ConcurrentHandler was ready, ignoring";

    // Threading stuff
    ScheduledThreadPoolExecutor service;
    private static final Queue<Runnable> mainThreadQueue = new ConcurrentLinkedQueue<>();

    // Singleton
    boolean initialised = false;
    private static final ConcurrentHandler instance = new ConcurrentHandler();

    /**
     * Set up the Concurrent handler, expects the config to be loaded
     */
    public static void initialise() {
        instance.service = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(Math.min(5, Math.max(DatConfig.getMaxConcurrentThreadCount() / 4, 1)));
        instance.service.setKeepAliveTime(5, TimeUnit.MINUTES);
        instance.service.setMaximumPoolSize(DatConfig.getMaxConcurrentThreadCount());

        instance.initialised = true;
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.ServerTickEvent event) {
        // Poll The mainThread queue and execute any tasks that have been queued
        for (int dummy = 0; dummy < DatConfig.getDelayedEventsPerTick() && !mainThreadQueue.isEmpty(); ++dummy) {
            mainThreadQueue.poll().run();
        }
    }

    /**
     * Queue a runnable to be executed on the server thread.
     * <br>
     * This can be used from a Concurrent Task to execute some code, like the result of a calculation, back on the main
     * thread.
     * <br>
     * Note this is <b>Non-Blocking</b>
     * @param task The task to run on the main thread
     */
    public static void runOnMainThread(final Runnable task) {
        mainThreadQueue.add(task);
    }

    /**
     * Queue a concurrent task with a return value
     * @param task The task to queue
     * @return A Future representing the task
     * @param <T> The return type of the task
     */
    public static <T> Future<T> callConcurrentTask(final Callable<T> task) {
        if (!ConcurrentHandler.instance.initialised) {
            logger.warn(NOT_READY_WARNING);
            return null;
        }

        return ConcurrentHandler.instance.service.submit(task);
    }

    /**
     * Queue an concurrent task
     * @param task The task to queue
     */
    public static void runConcurrentTask(final Runnable task) {
        if (!ConcurrentHandler.instance.initialised) {
            logger.warn(NOT_READY_WARNING);
            return;
        }

        ConcurrentHandler.instance.service.submit(task);
    }

    /**
     * Schedule a task with a return value to run after the given delay
     * @param delay The delay before running the task
     * @param unit The units of the delay
     * @param task The task to queue
     * @return A ScheduledFuture representing the task
     * @param <T> The return type of the task
     */
    public static <T> ScheduledFuture<T> scheduleConcurrentTask(final long delay, final TimeUnit unit, final Callable<T> task) {
        if (!ConcurrentHandler.instance.initialised) {
            logger.warn(NOT_READY_WARNING);
            return null;
        }

        return ConcurrentHandler.instance.service.schedule(task, delay, unit);
    }

    /**
     * Schedule a concurrent task
     * @param delay The delay before running the task
     * @param unit The units of the delay
     * @param task The task to queue
     * @return A ScheduledFuture representing the task
     */
    public static ScheduledFuture<?> scheduleConcurrentTask(final long delay, final TimeUnit unit, final Runnable task) {
        if (!ConcurrentHandler.instance.initialised) {
            logger.warn(NOT_READY_WARNING);
            return null;
        }

        return ConcurrentHandler.instance.service.schedule(task, delay, unit);
    }

    /**
     * Schedule a task to run repeatedly with a fixed period after an initial delay
     * @param initialDelay The delay before starting the task
     * @param period the period between repeat executions
     * @param unit the units of the period and delay
     * @param task the task to queue
     * @return A scheduledFuture representing the task (This is used to stop the event, do not lose it)
     */
    public static ScheduledFuture<?> scheduleFixedRateConcurrentTask(final long initialDelay, final long period, final TimeUnit unit, final Runnable task) {
        if (!ConcurrentHandler.instance.initialised) {
            logger.warn(NOT_READY_WARNING);
            return null;
        }

        return ConcurrentHandler.instance.service.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
}
