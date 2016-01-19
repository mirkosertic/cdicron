package de.mirkosertic.cdicron.api;

class JobInfo {
    private final Cron timed;
    private final BeanMethodInvocationRunnable runnable;

    public JobInfo(Cron aTimed, BeanMethodInvocationRunnable aRunnable) {
        timed = aTimed;
        runnable = aRunnable;
    }

    public Cron getTimed() {
        return timed;
    }

    public BeanMethodInvocationRunnable getRunnable() {
        return runnable;
    }
}
