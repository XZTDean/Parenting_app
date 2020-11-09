package ca.cmpt276.project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
        CoinFlipStats resultStats = new CoinFlipStats(timedDate, childPlaying.getName(), childPlaying.getChoiceOfHeadsOrTails() ,result);
        addStats(resultStats);

        return resultStats;
    }

    //add the generated Stats to the list.
    public void addStats(CoinFlipStats c){
        FlipList.add(c);
    }

    //store all the records in list to a string array which can be displayed in UI later.
    public String[] StoreInList(){
        String[] tempList = new String[FlipList.size()];
        for(int i = 0; i<FlipList.size();i++){
            tempList[i]= "at "+ FlipList.get(i).getFlipTime()+" "+ FlipList.get(i).getChildName()+
                    " gets to pick "+interpretInt(FlipList.get(i).getChoice())+
                    " and the result turns out to be "+interpretInt(FlipList.get(i).getResult());
            if(FlipList.get(i).getChoice() == FlipList.get(i).getResult()){
                tempList[i] = tempList[i]+" wining!";
            }
            else{
                tempList[i]= tempList[i]+" losing!";
            }

        }
        return tempList;

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
