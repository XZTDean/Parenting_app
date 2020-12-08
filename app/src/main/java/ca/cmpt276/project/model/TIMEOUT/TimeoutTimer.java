package ca.cmpt276.project.model.TIMEOUT;

/**
 * This model for timer, it will only hold a timer each time.
 * It can start, pause, resume and reset a timer. It can return
 * the remaining time of the timer. It can change the timer speed.
 */
public class TimeoutTimer {
    private static TimeoutTimer instance;

    private Thread thread;
    private final Runnable runnable;
    /** Countdown time in milliseconds (actual time) */
    private long remainingTime;
    /** Display or set time (will not changed by speed) */
    private long displayTime;
    private long finishTime;
    private Status status;
    private final int option;
    private double speed;

    public enum Status {
        ready, running, paused, stop
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    /**
     * Create a Timeout Timer
     * @param runnable Object to run after timer
     * @param time Time set for timer in minutes
     */
    public static TimeoutTimer getNewInstance(Runnable runnable, int time) {
        if (hasInstance()) {
            throw new IllegalStateException("A timer exist");
        }
        instance = new TimeoutTimer(runnable, time);
        return instance;
    }

    public static TimeoutTimer getExistInstance() {
        if (!hasInstance()){
            throw new IllegalStateException("Currently no timer");
        }
        TimeoutTimer tmp = instance;
        if (instance.status == Status.stop) {
            deleteInstance();
        }
        return tmp;
    }

    public static void deleteInstance() {
        instance = null;
    }

    /**
     * Create a Timeout Timer
     * @param runnable Object to run after timer
     * @param time Time set for timer in minutes
     */
    private TimeoutTimer(Runnable runnable, int time) {
        this.speed = 1;
        this.option = time;
        this.displayTime = minToMillisecond(time);
        this.remainingTime = (long) (displayTime / speed);
        this.runnable = runnable;
        status = Status.ready;
    }

    public int getOption() {
        return option;
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
        finishTime = System.currentTimeMillis() + remainingTime;
        status = Status.running;
    }

    public void pause() {
        System.out.println("paused");
        if (status != Status.running) {
            throw new IllegalStateException("Timer is not running");
        }
        updateRemainingTime();
        thread.interrupt();
        status = Status.paused;
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
        displayTime = minToMillisecond(option);
        remainingTime = (long) (displayTime / speed);
        status = Status.ready;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * @return displayed remaining time in seconds
     */
    public long getRemainingTime() {
        if (status == Status.running) {
            updateRemainingTime();
        }
        return displayTime / 1000;
    }

    private void updateRemainingTime() {
        long currentTime = System.currentTimeMillis();
        remainingTime = finishTime - currentTime;
        displayTime = (long) (remainingTime * speed);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed should be positive");
        }
        boolean running = status == Status.running;
        if (running) {
            pause(); // cannot change remaining time during running
        }

        this.speed = speed;
        remainingTime = (long) (displayTime / speed);

        if (running) {
            resume();
        }
    }

    private long minToMillisecond(int minute) {
        return minute * 60 * 1000;
    }

    public void endTimer() {
        status = Status.stop;
        remainingTime = displayTime = 0;
    }

}
