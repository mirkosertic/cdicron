package de.mirkosertic.timedcdi.api;

class JobInfo {
    private final Timed timed;
    private final BeanMethodInvocationRunnable runnable;

    public JobInfo(Timed aTimed, BeanMethodInvocationRunnable aRunnable) {
        timed = aTimed;
        runnable = aRunnable;
    }

    public Timed getTimed() {
        return timed;
    }

    public BeanMethodInvocationRunnable getRunnable() {
        return runnable;
    }
}
