package ca.cmpt276.project.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CoinFlipStats {
    private String timeDate;
    private String childName;
    private int choice;
    private int result;
    //true stands for Head and false stands for Tail.


    public CoinFlipStats(String timeDate, String childName, int choice, int result) {
        this.timeDate = timeDate;
        this.childName = childName;
        this.choice = choice;
        this.result = result;
    }



    public String getFlipTime() {
        return timeDate;
    }

    public String getChildName(){
        return childName;
    }

    public int getChoice() {
        return choice;
    }

    public int getResult() {
        return result;
    }










}
