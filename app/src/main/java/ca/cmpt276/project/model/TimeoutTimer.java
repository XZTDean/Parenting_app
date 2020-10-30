package ca.cmpt276.project.model;

public class TimeoutTimer {
    private Thread thread;
    private Runnable runnable;
    /**
     * Countdown time in milliseconds
     */
    private long remainingTime;
    private long startTime;
    private Status status;
    private final int option;

    private enum Status {
        ready, running, paused
    }

    /**
     * Create a Timeout Timer
     * @param runnable Object to run after timer
     * @param time Time set for timer in minutes
     */
    public TimeoutTimer(Runnable runnable, int time) {
        this.option = time;
        this.remainingTime = time * 60 * 1000;
        this.runnable = runnable;
        status = Status.ready;
    }

    public void start() {
        if (status != Status.ready) {
            throw new IllegalStateException("Timer is not ready");
        }
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
        status = Status.running;
    }

    /**
     * Pause the timer
     * @return remaining time in seconds
     */
    public long pause() {
        if (status != Status.running) {
            throw new IllegalStateException("Timer is not running");
        }
        long currentTime = System.currentTimeMillis();
        remainingTime = remainingTime - (currentTime - startTime);
        thread.interrupt();
        status = Status.paused;
        return remainingTime / 1000;
    }

    public void resume() {
        if (status != Status.paused) {
            throw new IllegalStateException("Timer is not paused");
        }
        status = Status.ready;
        start();
    }

    public void reset() {
        if (status != Status.paused && status != Status.running) {
            throw new IllegalStateException("Timer cannot be reset");
        }
        if (status == Status.running) {
            pause();
        }
        remainingTime = option * 60 * 1000;
        status = Status.ready;
    }
}
