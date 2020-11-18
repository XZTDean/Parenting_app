package ca.cmpt276.project.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents each child in app.
 * Uses an integer that can be 0 - (child has not flipped yet), 1 - (child got heads)
 * and 2- (child got tails).
 */
public class Child implements Parcelable {
    private String name;
    private int choiceOfHeadsOrTails;  // 1 - heads and 2 - tails

    private int timesToPick;

    private Bitmap photo;

    public Child(String name, Bitmap photo) {
        this.name = name;
        timesToPick = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Bitmap getPhoto() { return photo; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public int getChoiceOfHeadsOrTails() {
        return choiceOfHeadsOrTails;
    }

    public void setChoiceOfHeadsOrTails(int choiceOfHeadsOrTails) {
        this.choiceOfHeadsOrTails = choiceOfHeadsOrTails;
    }

    public int getTimesToPick() {
        return timesToPick;
    }
    public void updateTimesToPick(){
        timesToPick++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
