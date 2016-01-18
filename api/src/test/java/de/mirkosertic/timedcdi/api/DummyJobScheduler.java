package de.mirkosertic.timedcdi.api;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class DummyJobScheduler implements JobScheduler {

    public static final AtomicLong COUNTER = new AtomicLong(0);

    @Override
    public void schedule(String aCronExpression, Runnable aRunnable) {
    }

    @Timed(cronExpression = "* * * * *")
    public void testTimed() {
        COUNTER.incrementAndGet();
    }
}
