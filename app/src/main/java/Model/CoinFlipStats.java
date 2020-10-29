package Model;

public class CoinFlipStats {
    private int flipTime;
    private int flipDate;
    private boolean choice;
    private boolean result;
    //true stands for Head and false stands for Tail.

    public CoinFlipStats(int flipTime, int flipDate, boolean choice, boolean result) {
        this.flipTime = flipTime;
        this.flipDate = flipDate;
        this.choice = choice;
        this.result = result;
    }

    public int getFlipTime() {
        return flipTime;
    }

    public int getFlipDate() {
        return flipDate;
    }

    public boolean isChoice() {
        return choice;
    }

    public boolean isResult() {
        return result;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
