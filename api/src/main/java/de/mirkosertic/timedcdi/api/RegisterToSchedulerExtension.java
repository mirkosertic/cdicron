package de.mirkosertic.timedcdi.api;

import javax.enterprise.context.spi.Context;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RegisterToSchedulerExtension implements Extension {

    static class BeanMethodInvocationRunnable implements Runnable {
        final Bean bean;
        final BeanManager beanManager;
        final Method method;

        public BeanMethodInvocationRunnable(Bean aBean, BeanManager aBeanManager, Method aMethod) {
            bean = aBean;
            beanManager = aBeanManager;
            method = aMethod;
        }

        @Override
        public void run() {
            Context theContext = beanManager.getContext(bean.getScope());
            Object theInstance = theContext.get(bean);
            try {
                method.invoke(theInstance, new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class JobInfo {
        private final Timed timed;
        private final BeanMethodInvocationRunnable runnable;

        public JobInfo(Timed aTimed, BeanMethodInvocationRunnable aRunnable) {
            timed = aTimed;
            runnable = aRunnable;
        }
    }


    private final List<JobInfo> jobsToRegister;
    private Bean jobScheduler;

    public RegisterToSchedulerExtension() {
        jobsToRegister = new ArrayList<>();
    }

    public void onProcessBean(@Observes ProcessManagedBean aEvent, BeanManager aBeanManager) {
        Class theBeanType = aEvent.getBean().getBeanClass();
        if (JobScheduler.class.isAssignableFrom(theBeanType)) {
            jobScheduler = aEvent.getBean();
        }
        for (Method theMethod : theBeanType.getDeclaredMethods()) {
            if (theMethod.isAnnotationPresent(Timed.class)) {
                Timed theTimed = theMethod.getAnnotation(Timed.class);
                BeanMethodInvocationRunnable theRunnable = new BeanMethodInvocationRunnable(aEvent.getBean(), aBeanManager, theMethod);
                jobsToRegister.add(new JobInfo(theTimed, theRunnable));
            }
        }
    }

    public void onAfterDeploymentValidation(@Observes AfterDeploymentValidation aEvent, BeanManager aBeanManager) {
        if (jobScheduler == null) {
            throw new IllegalStateException("No JobScheduler Implementation found in managed scope!");
        }
        Context theContext = aBeanManager.getContext(jobScheduler.getScope());
        try {
            JobScheduler theScheduler = (JobScheduler) theContext.get(jobScheduler);
            jobsToRegister.stream().forEach(t -> theScheduler.schedule(t.timed.cronExpression(), t.runnable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
