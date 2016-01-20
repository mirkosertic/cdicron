package de.mirkosertic.cdicron.api;

import java.lang.annotation.*;

/**
 * This marker annotation can be placed at any cdi managed beans instance method.
 *
 * The method will be called as described by the cronExpression.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cron {

    String cronExpression();
}