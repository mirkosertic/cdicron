# CDI Cron

A CDI extension to schedule jobs as easy as Unix cron

[![Build Status](https://travis-ci.org/mirkosertic/GameComposer.svg?branch=master)](https://travis-ci.org/mirkosertic/GameComposer)

## Usage

Java Maven Dependency available from Central Repository:

```xml
            <dependency>
                <groupId>de.mirkosertic.cdicron</groupId>
                <artifactId>cdi-cron-quartz-scheduler</artifactId>
                <version>1.0</version>
            </dependency>
```

Example Java Code

```java

@Singleton
public class DummyJob {

    public static final AtomicLong COUNTER = new AtomicLong(0);

    @Cron(cronExpression = "0/2 * * * * ?")
    public void scheduledMethod() {
        COUNTER.incrementAndGet();
    }
}
```
