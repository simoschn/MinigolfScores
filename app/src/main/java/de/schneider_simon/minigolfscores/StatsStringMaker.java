package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatsStringMaker {

    private static String FORMAT = "dd.MM.yyyy";
    private static String TAG = "StatsStringMaker: ";

    public static String lastTrainingAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub) {

        String statsString;
        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        statsString = makeStatsStringForOneDate(roundsCursor);

        return statsString;
    }

    public static String allRoundsAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub){

        String statsString="";
        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        while(!roundsCursor.isAfterLast())
            statsString += (makeStatsStringForOneDate(roundsCursor) +newline() + newline());

        return statsString;
    }

    public static String roundsAverageAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub){
        Double sumOfAllRounds = 0.0;
        Integer numberOfRounds = 0;
        ArrayList<Integer> allRoundsList;
        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        allRoundsList = getRoundsListFromCursor(roundsCursor);

        for(Integer round : allRoundsList){
            sumOfAllRounds += round;
            numberOfRounds++;
        }

        return String.format("%.2f", sumOfAllRounds/numberOfRounds);
    }

    public static String totalNumberOfRoundsAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub){
        ArrayList<Integer> allRoundsList;
        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);
        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        allRoundsList = getRoundsListFromCursor(roundsCursor);

        return ((Integer)allRoundsList.size()).toString();
    }

    private static String makeStatsStringForOneDate(Cursor roundsCursor) {
        String date = extractDateStringFromDatetimeAtCursor(roundsCursor);
        ArrayList<Integer> roundsList = getRoundsListFromCursor(roundsCursor, date);
        ArrayList<Integer> totalsList = getTotalsListFromRoundsList(roundsList);
        String statsString = date + newline() + newline();

        statsString += makeScoresStringFromScoresList(roundsList) + newline();
        statsString += makeScoresStringFromScoresList(totalsList) + newline() + newline();
        statsString += makeAverageStringFromScoresList(roundsList);
        return statsString;
    }

    private static ArrayList<Integer> getRoundsListFromCursor(Cursor roundsCursor, String ... date){
        ArrayList<Integer> roundsList = new ArrayList<>();
        boolean breakCondition;

        do {
            roundsList.add(0, calculateRoundScoreAtCursor(roundsCursor));
            roundsCursor.moveToNext();

            if(date.length==0)
                breakCondition = !roundsCursor.isAfterLast();
            else
                breakCondition = isAnotherRoundWithSameDate(date[0], roundsCursor);
        } while(breakCondition);

        return roundsList;
    }

    private static ArrayList<Integer> getTotalsListFromRoundsList(ArrayList<Integer> roundsList){
        ArrayList<Integer> totalsList = new ArrayList<>();

        totalsList.add(0, roundsList.get(0));

        for(Integer index=1; index<roundsList.size(); index++){
            totalsList.add(index, totalsList.get(index-1) + roundsList.get(index));
        }

        return totalsList;
    }

    private static String makeScoresStringFromScoresList(ArrayList<Integer> scoresList) {
        String scoresString = "";
        boolean isFirst = true;

        for(Integer round : scoresList){
            if(isFirst) {
                scoresString += round.toString();
                isFirst = false;
            }
            else{
                scoresString += String.format("%5d", round);
            }
        }
        return scoresString;
    }

    private static String makeAverageStringFromScoresList(ArrayList<Integer> scoresList){
        Double totalScore = 0.0;

        for(Integer round : scoresList){
            totalScore += round;
        }
        return String.format("%.2f", totalScore / scoresList.size());
    }

    private static Cursor setRoundsCursor(SQLiteDatabase roundsDb, String selectedClub) {
        return roundsDb.query("Rounds", new String[]{
                    "datetime",
                    "hole1",
                    "hole2",
                    "hole3",
                    "hole4",
                    "hole5",
                    "hole6",
                    "hole7",
                    "hole8",
                    "hole9",
                    "hole10",
                    "hole11",
                    "hole12",
                    "hole13",
                    "hole14",
                    "hole15",
                    "hole16",
                    "hole17",
                    "hole18"
            }, "club='" + selectedClub + "'", null, null, null, "datetime DESC", null);
    }

    private static Integer calculateRoundScoreAtCursor(Cursor roundsCursor) {
        Integer roundScore;
        roundScore = 0;
        for (int i = 1; i <= 18; i++)
            roundScore += roundsCursor.getInt(i);
        return roundScore;
    }

    private static boolean isAnotherRoundWithSameDate(String date, Cursor roundsCursor) {
        if(roundsCursor.isAfterLast())
            return false;
        else
            return extractDateStringFromDatetimeAtCursor(roundsCursor).equals(date);
    }

    private static boolean isNoRoundPlayedYet(Cursor roundsCursor) {
        return roundsCursor.getCount() == 0;
    }

    private static String extractDateStringFromDatetimeAtCursor(Cursor roundsCursor) {
        Long lastDate = roundsCursor.getLong(0);
        Date date = new Date(lastDate * 1000);
        SimpleDateFormat simpleDate = new SimpleDateFormat(FORMAT);
        return simpleDate.format(date);
    }

    private static String newline(){
        return "\n";
    }
}
