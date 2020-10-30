package ca.cmpt276.project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CoinFlip {
    private List<CoinFlipStats> FlipList = new ArrayList<>();

    public CoinFlipStats flipCoin(String childName, int choice){
        String timedate = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Random rand = new Random();
        int result = rand.nextInt(2);
        return new CoinFlipStats(timedate,childName,choice,result);

    }
    public void addStats(CoinFlipStats c){
        FlipList.add(c);
    }



    public String[] displayList(){
        String[] tempList = new String[FlipList.size()];
        for(int i = 0; i<FlipList.size();i++){
            tempList[i]= FlipList.get(i).getChildName();
            

        }
        return tempList;

    }

    public Iterator<CoinFlipStats> iterator(){
        return FlipList.iterator();
    }

}
