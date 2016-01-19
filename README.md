# CDI Cron

A CDI extension to schedule jobs as easy as Unix cron

## Usage

Java Maven Dependency

```xml
            <dependency>
                <groupId>de.mirkosertic.cdicron</groupId>
                <artifactId>cdi-cron-quartz-scheduler</artifactId>
                <version>1.0-SNAPSHOT</version>
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
