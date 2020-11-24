package ca.cmpt276.project.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Class that represents each child in app.
 * Uses an integer that can be 0 - (child has not flipped yet), 1 - (child got heads)
 * and 2- (child got tails).
 */
public class Child implements Parcelable, Comparable<Child>{
    private String name;
    private int choiceOfHeadsOrTails;  // 1 - heads and 2 - tails
    private int timesToPick;
    private int recentlyPlayed;  // 1 - true and 0 - false

    private String encodedPhoto;

    public Child(String name, Bitmap photo) {
        this.name = name;
        timesToPick = 0;
        if(photo != null) {
            this.encodedPhoto = encodePhoto(photo);
        }
    }

    protected Child(Parcel in) {
        name = in.readString();
        choiceOfHeadsOrTails = in.readInt();
        timesToPick = in.readInt();
        encodedPhoto = in.readString();
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    private String encodePhoto(Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private Bitmap decodePhoto(){
        byte[] b = Base64.decode(encodedPhoto, Base64.DEFAULT);
        return  BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Bitmap getPhoto() { return decodePhoto(); }

    public void setPhoto(Bitmap photo) {
        if(photo != null) {
            this.encodedPhoto = encodePhoto(photo);
        }
    }

    public int getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(int recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

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
        dest.writeString(name);
        dest.writeInt(choiceOfHeadsOrTails);
        dest.writeInt(timesToPick);
        dest.writeString(encodedPhoto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Child child = (Child) o;
        return name.equals(child.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Child child) {
        int compareValue = child.getRecentlyPlayed();
        return this.recentlyPlayed - compareValue;
    }
}
