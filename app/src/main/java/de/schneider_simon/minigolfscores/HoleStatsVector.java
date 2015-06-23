package de.schneider_simon.minigolfscores;

import java.util.Vector;

public class HoleStatsVector {

    private Vector<HoleStats> vector;

    public Vector<HoleStats> getVector() {
        return vector;
    }

    public HoleStatsVector(){
        this.vector = new Vector<HoleStats>();
    }

    public void add(HoleStats holeStats){
        vector.add(holeStats);
    }

    public void sort(){
        boolean wasSwapped;

        do{
            wasSwapped = false;

            for(int i=0; i<(vector.size()-1); i++){
                if((HoleStats.compare(vector.elementAt(i), vector.elementAt(i+1)))>0){
                    swapNeighbors(i);
                    wasSwapped = true;
                }
            }
        }while(wasSwapped);
    }

    private void swapNeighbors(int pos){
        HoleStats temp = new HoleStats(vector.elementAt(pos));
        vector.setElementAt(vector.elementAt(pos+1), pos);
        vector.setElementAt(temp, pos+1);
    }
}
