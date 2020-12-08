package ca.cmpt276.project.model.COIN_FLIP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ca.cmpt276.project.R;

/**
 * Stores data of coin flips.
 * Used to display in History screen.
 * Has functionality of determining if coin toss was a win or loss
 */
public class CoinFlipStats {
    private String timeDate;
    private String childName;
    private String childPortrait;
    private int choice;
    private int result;
    private int iconID;


    public CoinFlipStats(String timeDate, String childName, String childPortrait, int choice, int result, int iconID) {
        this.timeDate = timeDate;
        this.childName = childName;
        this.childPortrait = childPortrait;
        this.choice = choice;
        this.result = result; //for choice and result, 1---head, 2---tail.
        this.iconID = iconID;
    }

    // 1 - win, 2 - lose
    public int winOrLose() {
        return (choice == result) ? 1 : 0;
    }

    public String getFlipTime() {
        return timeDate;
    }

    public String getChildName() {
        return childName;
    }

    private String encodePhoto(Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Bitmap decodePhoto(){
        byte[] b = Base64.decode(childPortrait, Base64.DEFAULT);
        return  BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public Bitmap getChildPortrait() {
        return decodePhoto();
    }

    public int getChoice() {
        return choice;
    }

    public int getResult() {
        return result;
    }

    public int getIconID() {
        return iconID;
    }

    //flip the coin generating a new set of flipCoinStats with random result.
    public void flipCoin(String childname, int choice1) {
        String timedate = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Random rand = new Random();
        int result1 = rand.nextInt(2) + 1;
        timeDate = timedate;
        childName = childname;
        choice = choice1;
        result = result1;
        if (choice == result) {
            iconID = R.drawable.check_icon;
        } else {
            iconID = R.drawable.cross_icon;
        }


    }

}