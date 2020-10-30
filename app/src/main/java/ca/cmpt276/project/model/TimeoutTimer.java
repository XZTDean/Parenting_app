package ca.cmpt276.project.model;

public class TimeoutTimer {
    private Thread thread;
    private Runnable runnable;
    /**
     * Countdown time in milliseconds
     */
    private long remainingTime;
    private long startTime;

    /**
     * Create a Timeout Timer
     * @param runnable Object to run after timer
     * @param time Time set for timer in minutes
     */
    public TimeoutTimer(Runnable runnable, int time) {
        this.remainingTime = time * 60 * 1000;
        this.runnable = runnable;
    }

    public void start() {
        thread = new Thread(runnable){
            @Override
            public void run() {
                try {
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    return;
                }
                super.run();
            }
        };
        thread.start();
        startTime = System.currentTimeMillis();
    }

    /**
     * Pause the timer
     * @return remaining time in seconds
     */
    public long pause() {
        long currentTime = System.currentTimeMillis();
        remainingTime = remainingTime - (currentTime - startTime);
        thread.interrupt();
        return remainingTime / 1000;
    }
}
