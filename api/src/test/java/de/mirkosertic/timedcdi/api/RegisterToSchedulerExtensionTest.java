package de.mirkosertic.timedcdi.api;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterToSchedulerExtensionTest {

    private WeldContainer weld;

    @Before
    public void setup() {
        weld = new Weld().initialize();
    }

    @After
    public void shutdown() {
        if (weld != null) {
            weld.shutdown();
        }
    }

    @Test
    public void testRegistrationAndDiscovery() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(DummyJobScheduler.COUNTER.get() > 0);
    }
}