package de.mirkosertic.timedcdi.quartzscheduler;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DummyBean {

    public void doSomething() {
        System.out.println("Doing something!");
    }
}
