package ca.cmpt276.project.model;

public class Child {
    private String name;
    private int choiceOfHeadsOrTails;

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getChoiceOfHeadsOrTails() {
        return choiceOfHeadsOrTails;
    }

    public void setChoiceOfHeadsOrTails(int choiceOfHeadsOrTails) {
        this.choiceOfHeadsOrTails = choiceOfHeadsOrTails;
    }
}
