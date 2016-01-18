package de.mirkosertic.timedcdi.quartzscheduler;

import de.mirkosertic.timedcdi.api.JobScheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class QuartzScheduler implements JobScheduler {

    private final Scheduler scheduler;

    public QuartzScheduler() throws SchedulerException {
        SchedulerFactory theFactory = new StdSchedulerFactory();
        scheduler = theFactory.getScheduler();
    }

    @PreDestroy
    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}