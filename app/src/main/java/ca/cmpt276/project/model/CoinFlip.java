package ca.cmpt276.project.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ca.cmpt276.project.R;

/**
 * Class that represents a singular Coin Flip
 * Randomizes and gives a result of head - 1, or tails - 2.
 * Stores data of each coin flip in an arrayList of class CoinFlipStats.
 * Uses Json to save data.
 */
public class CoinFlip {

    private static CoinFlip instance;
    public final String Flip_KEY = "FlipList";
    public static CoinFlip getInstance() {
        if (instance == null){
            instance = new CoinFlip();
        }
        return instance;
    }


    private List<CoinFlipStats> FlipList = new ArrayList<>();
    public List<CoinFlipStats> getList(){
        return FlipList;
    }


    //flip the coin generating a new set of flipCoinStats with random result.
    public CoinFlipStats flipCoin(Child childPlaying){

        String timedDate = java.text.DateFormat.getDateTimeInstance().format(new Date());
        int result = (Math.random()<=0.5)? 1 : 2;
        int icon = (result == childPlaying.getChoiceOfHeadsOrTails())? R.drawable.check_icon : R.drawable.cross_icon;

        CoinFlipStats resultStats = new CoinFlipStats(timedDate, childPlaying.getName(),
                childPlaying.getEncodedPhoto(), childPlaying.getChoiceOfHeadsOrTails() ,result, icon);
        addStats(resultStats);

        return resultStats;
    }

    //add the generated Stats to the list.
    public void addStats(CoinFlipStats c){
        FlipList.add(c);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(FlipList);
    }

    public void loadFromJson(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<CoinFlipStats>>(){}.getType();
        FlipList = gson.fromJson(json, collectionType);
    }



    /*interpret the integers in choice and result. If the integer is 1, it presents Head.
    otherwise it represents tail.
     */
    public String interpretInt(int choiceOrresult){
        if(choiceOrresult ==1){
            return "head";
        }
        else{
            return "tail";
        }
    }

    public Iterator<CoinFlipStats> iterator(){
        return FlipList.iterator();
    }

    public List<CoinFlipStats> getSpecificChild(String childName){
        List<CoinFlipStats> myList = new ArrayList<>();
        for(CoinFlipStats c:FlipList){
            if(c.getChildName().equals(childName)){
                myList.add(c);
            }
        }
        return myList;
    }

}
