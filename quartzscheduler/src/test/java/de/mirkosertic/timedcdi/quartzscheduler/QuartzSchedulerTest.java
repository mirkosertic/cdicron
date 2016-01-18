package de.mirkosertic.timedcdi.quartzscheduler;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuartzSchedulerTest {

    private WeldContainer container;

    @Before
    public void start() {
        container = new Weld().initialize();
    }

    @After
    public void stop() {
        if (container != null) {
            container.shutdown();
            container = null;
        }
    }

    @Test
    public void testRegister() throws InterruptedException {
        Thread.sleep(5000);
        assertTrue(DummyJob.COUNTER.get() > 0);
    }
}