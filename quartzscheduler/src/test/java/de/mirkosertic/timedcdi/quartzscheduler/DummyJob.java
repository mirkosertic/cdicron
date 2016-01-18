package de.mirkosertic.timedcdi.quartzscheduler;

import de.mirkosertic.timedcdi.api.JobScheduler;
import de.mirkosertic.timedcdi.api.Timed;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class DummyJob {

    public static final AtomicLong COUNTER = new AtomicLong(0);

    @Timed(cronExpression = "0/2 * * * * ?")
    public void testTimed() {
        COUNTER.incrementAndGet();
    }
}
