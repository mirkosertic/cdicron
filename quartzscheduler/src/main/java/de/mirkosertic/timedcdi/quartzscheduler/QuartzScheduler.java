package de.mirkosertic.timedcdi.quartzscheduler;

import de.mirkosertic.timedcdi.api.JobScheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.inject.Singleton;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

@Singleton
public class QuartzScheduler implements JobScheduler {

    public static class RunnableWrapperJob implements Job {

        public static final String KEY = "runnable";

        @Override
        public void execute(JobExecutionContext aContext) throws JobExecutionException {
            JobDataMap theDataMap = aContext.getJobDetail().getJobDataMap();
            Runnable theRunnable = (Runnable) theDataMap.get(KEY);
            theRunnable.run();
        }
    }

    private final Scheduler scheduler;

    public QuartzScheduler() throws SchedulerException {
        SchedulerFactory theFactory = new StdSchedulerFactory();
        scheduler = theFactory.getScheduler();
    }

    @Override
    public void schedule(String aCronExpression, Runnable aRunnable) {
        JobDataMap theJobDataMap = new JobDataMap();
        theJobDataMap.put(RunnableWrapperJob.KEY, aRunnable);
        JobDetail theDetails = newJob(RunnableWrapperJob.class).setJobData(theJobDataMap).build();
        Trigger theTrigger = newTrigger().withSchedule(cronSchedule(aCronExpression)).build();
        try {
            scheduler.scheduleJob(theDetails, theTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void afterInitialized(@Observes AfterDeploymentValidation aAfterDeploymentValidation) throws SchedulerException {
        scheduler.start();
    }

    @PreDestroy
    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}