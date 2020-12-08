package ca.cmpt276.project.model.BREATH;

import java.util.ArrayList;
import java.util.List;

public class BreathManager {
    private List<Breath> breathList;
    private Breath currentBreath;

    private static BreathManager instance;

    private BreathManager(){
        breathList = new ArrayList<Breath>();
    }

    private void addBreath(Breath breath){
        breathList.add(breath);
    }

    private List<Breath> getList(){
        return breathList;
    }

}
