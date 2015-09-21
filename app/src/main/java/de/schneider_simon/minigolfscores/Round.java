package de.schneider_simon.minigolfscores;

public class Round {
    private Integer[] holes;

    public Round(){
        holes = new Integer[18];
    }

    public Integer getHole(int index) {
        if((index >= 0) && (index<18))
            return holes[index];
        else
            return -1;
    }

    public void setHole(Integer score, Integer index) {
        if((index >= 0) && (index<18))
            holes[index] = score;
    }
}
