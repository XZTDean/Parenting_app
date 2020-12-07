package ca.cmpt276.project.model;

import android.os.CountDownTimer;

import java.util.Calendar;
import java.util.Date;

public class Breath {

    private String breathPerson;  // the person who takes the deep breath(could be user or the children)
    private int breathNumLeft;


    private boolean ifInhale = true;
    private boolean ifBreathing = true;


    public Breath(int breathNum, String breathPerson) {
        this.breathNumLeft = breathNum;
        this.breathPerson = breathPerson;
    }

    public void setBreathNum(int breathNum) {
        this.breathNumLeft = breathNum;
    }


    public int getBreathNum(){
        return breathNumLeft;
    }

    public String getBreathPerson(){
        return breathPerson;
    }

    public void updateBreathLeft(){
        breathNumLeft--;
    }

    public void moreBreath(int n){
        breathNumLeft = breathNumLeft + n;
    }

    public Boolean isInhaling(){ return ifInhale; }

    public void changeBreath(){this.ifInhale = !this.ifInhale;}


}
