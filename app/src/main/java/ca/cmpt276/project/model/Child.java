package ca.cmpt276.project.model;
/*
 * Class that represents each child in app.
 * Uses an integer that can be 0 - (child has not flipped yet), 1 - (child got heads)
 * and 2- (child got tails).
 */
public class Child {
    private String name;
    private int choiceOfHeadsOrTails;  // 1 - heads and 2 - tails

    private int timesToPick;

    public Child(String name) {
        this.name = name;
        timesToPick = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
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
}

/*
 * Class that represents each child in app.
 * Uses an integer that can be 0 - (child has not flipped yet), 1 - (child got heads)
 * and 2- (child got tails).
 */
