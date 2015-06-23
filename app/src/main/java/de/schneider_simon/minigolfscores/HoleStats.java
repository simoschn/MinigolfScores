package de.schneider_simon.minigolfscores;


public class HoleStats {

    private int runningNumber;
    private String name;
    private double average;
    private double acePercentage;

    public HoleStats(){
        this.runningNumber = 0;
        this.name = "";
        this.average = 0.0;
        this.acePercentage = 0.0;
    }

    public HoleStats(HoleStats stats){
        this.runningNumber = stats.runningNumber;
        this.name = stats.name;
        this.average = stats.average;
        this.acePercentage = stats.acePercentage;
    }

    public static int compare(HoleStats one, HoleStats two){
        return (one.getAverage()>two.getAverage())?1:(one.getAverage()<two.getAverage())?-1:0;
    }

    public String toString(){
        return String.format("%-3d %-18s %.2f %.0f%%",runningNumber, name, average, acePercentage);
    }

    public int getRunningNumber() {
        return runningNumber;
    }

    public void setRunningNumber(int runningNumber) {
        this.runningNumber = runningNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getAcePercentage() {
        return acePercentage;
    }

    public void setAcePercentage(double acePercentage) {
        this.acePercentage = acePercentage;
    }
}
