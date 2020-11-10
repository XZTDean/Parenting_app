package ca.cmpt276.project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ca.cmpt276.project.R;

public class CoinFlip {

    private static CoinFlip instance;
    private String choiceMade;

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
                childPlaying.getChoiceOfHeadsOrTails() ,result, icon);
        addStats(resultStats);

        return resultStats;
    }

    //add the generated Stats to the list.
    public void addStats(CoinFlipStats c){
        FlipList.add(c);
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

}
