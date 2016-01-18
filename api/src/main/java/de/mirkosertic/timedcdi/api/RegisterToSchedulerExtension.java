package de.mirkosertic.timedcdi.api;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
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
        boolean firstInit;

        private Object instance;

        public BeanMethodInvocationRunnable(Bean aBean, BeanManager aBeanManager, Method aMethod) {
            bean = aBean;
            beanManager = aBeanManager;
            method = aMethod;
            firstInit = true;
        }

        @Override
        public void run() {
            if (firstInit) {
                Context theContext = beanManager.getContext(bean.getScope());
                instance = theContext.get(bean);
                if (instance == null) {
                    CreationalContext theCreational = beanManager.createCreationalContext(bean);
                    instance = beanManager.getReference(bean, bean.getBeanClass(), theCreational);
                }
            }
            try {
                method.invoke(instance, new Object[0]);
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
        CreationalContext theContext = aBeanManager.createCreationalContext(jobScheduler);
        try {
            JobScheduler theScheduler = (JobScheduler) aBeanManager.getReference(jobScheduler, jobScheduler.getBeanClass(), theContext);
            jobsToRegister.stream().forEach(t -> theScheduler.schedule(t.timed.cronExpression(), t.runnable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
