package de.mirkosertic.cdicron.api;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.reflect.Method;

class BeanMethodInvocationRunnable implements Runnable {
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
            firstInit = false;
        }
        try {
            method.invoke(instance, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
