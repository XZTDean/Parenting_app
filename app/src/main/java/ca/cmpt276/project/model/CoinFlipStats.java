package ca.cmpt276.project.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CoinFlipStats {
    private String timeDate;
    private String childName;
    private boolean choice;
    private boolean result;
    //true stands for Head and false stands for Tail.

    public CoinFlipStats(String childName, boolean choice) {
        this.childName = childName;
        this.choice = choice;

    }


    public void setFlipTime() {
        this.timeDate = java.text.DateFormat.getDateTimeInstance().format(new Date());
    }
    public void setResult() {
        Random rand = new Random();
        this.result = rand.nextBoolean();
    }

    public String getFlipTime() {
        return timeDate;
    }

    public String getChildName(){
        return childName;
    }

    public boolean getChoice() {
        return choice;
    }

    public boolean getResult() {
        return result;
    }










}
