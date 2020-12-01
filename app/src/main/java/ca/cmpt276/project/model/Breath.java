package ca.cmpt276.project.model;

import android.os.CountDownTimer;

import java.util.Calendar;
import java.util.Date;

public class Breath {
    private int breathNum = 3;   // #breath for each breath activity(default is 3)
    private String breathPerson;  // the person who takes the deep breath(could be user or the children)

    private long breathTimeLeft;
    private CountDownTimer timer;
    private boolean ifInhale = true;
    private boolean ifBreathing = true;


    public Breath(int breathNum, String breathPerson) {
        this.breathNum = breathNum;
        this.breathPerson = breathPerson;
    }

    public void setBreathNum(int breathNum) {
        this.breathNum = breathNum;
    }

    public void setBreathTime(){
        breathTimeLeft = 8 * breathNum * 1000;
    }

    public void startBreath(){
        timer = new CountDownTimer(breathTimeLeft,4000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ifInhale = !ifInhale;
                breathTimeLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                ifBreathing = false;
            }
        }.start();

    }

    public int getBreathNum(){
        return breathNum;
    }

    public String getBreathPerson(){
        return breathPerson;
    }


}
