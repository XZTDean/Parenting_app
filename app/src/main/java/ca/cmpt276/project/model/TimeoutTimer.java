package ca.cmpt276.project.model;

public class TimeoutTimer {
    private Thread thread;
    private final Runnable runnable;
    /**
     * Countdown time in milliseconds
     */
    private long remainingTime;
    private long startTime;
    private Status status;
    private final int option;

    public enum Status {
        ready, running, paused, stop
    }

    /**
     * Create a Timeout Timer
     * @param runnable Object to run after timer
     * @param time Time set for timer in minutes
     */
    public TimeoutTimer(Runnable runnable, int time) {
        this.option = time;
        this.remainingTime = minToMillisecond(time);
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
                endTimer();
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
        updateRemainingTime();
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
        remainingTime = minToMillisecond(option);
        status = Status.ready;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * @return remaining time in seconds
     */
    public long getRemainingTime() {
        if (status == Status.running) {
            updateRemainingTime();
        }
        return remainingTime / 1000;
    }

    private void updateRemainingTime() {
        long currentTime = System.currentTimeMillis();
        remainingTime = remainingTime - (currentTime - startTime);
    }

    private long minToMillisecond(int minute) {
        return minute * 60 * 1000;
    }

    private void endTimer() {
        status = Status.stop;
        remainingTime = 0;
    }
}
