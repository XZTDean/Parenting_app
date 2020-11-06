package ca.cmpt276.project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CoinFlip {

    private static CoinFlip instance;

    public static CoinFlip getInstance() {
        if (instance == null){
            instance = new CoinFlip();
        }
        return instance;
    }


    private List<CoinFlipStats> FlipList = new ArrayList<>();


    //flip the coin generating a new set of flipCoinStats with random result.
    public CoinFlipStats flipCoin(String childName, int choice){
        String timedate = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Random rand = new Random();
        int result = rand.nextInt(1);
        return new CoinFlipStats(timedate,childName,choice,result);

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
                    " gets to pick "+FlipList.get(i).getChoice()+" and the result turns out to be "+FlipList.get(i).getResult();
            if(FlipList.get(i).getChoice() == FlipList.get(i).getResult()){
                //To do: Add check mark icon if choice == result.
            }



        }
        return tempList;

    }

    public Iterator<CoinFlipStats> iterator(){
        return FlipList.iterator();
    }

}
