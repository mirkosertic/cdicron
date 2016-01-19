package de.mirkosertic.timedcdi.quartzscheduler;

import de.mirkosertic.cdicron.api.Cron;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class DummyJob {

    public static final AtomicLong COUNTER = new AtomicLong(0);

    @Cron(cronExpression = "0/2 * * * * ?")
    public void testTimed() {
        COUNTER.incrementAndGet();
    }
}
