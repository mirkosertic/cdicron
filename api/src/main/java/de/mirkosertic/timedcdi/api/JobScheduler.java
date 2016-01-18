package de.mirkosertic.timedcdi.api;

/**
 * Base class for all Job Scheduler Implementations.
 *
 * The {@link RegisterToSchedulerExtension} will search for an implementinng Singleton in the
 * aktive CDI {@link javax.enterprise.inject.spi.BeanManager} and register it.
 */
public interface JobScheduler {

    /**
     * Schedule a Task.
     *
     * @param aCronExpression a cron expression to use
     * @param aRunnable the task to schedule
     */
    void schedule(String aCronExpression, Runnable aRunnable);
}
