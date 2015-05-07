package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatsStringMaker {

    private static String FORMAT = "dd.MM.yyyy";
    private static String TAG = "StatsStringMaker: ";

    public static String lastTrainingAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub) {

        String statsString;
        String mostRecentDate;
        ArrayList<Integer> roundsList;
        ArrayList<Integer> totalsList;

        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        mostRecentDate = extractDateStringFromDatetimeAtCursor(roundsCursor);
        statsString = mostRecentDate + newline() + newline();

        roundsList = getRoundsListAtDateFromCursor(mostRecentDate, roundsCursor);
        totalsList = getTotalsListFromRoundsList(roundsList);

        statsString += makeScoresStringFromScoresList(roundsList) + newline();
        statsString += makeScoresStringFromScoresList(totalsList) + newline() + newline();
        statsString += makeAverageStringFromScoresList(roundsList);

        return statsString;
    }

    public static String allRoundsAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub){
        String statsString = "";

        String currentDate;
        ArrayList<Integer> roundsList;
        ArrayList<Integer> totalsList;

        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        while(!roundsCursor.isAfterLast()){
            currentDate = extractDateStringFromDatetimeAtCursor(roundsCursor);
            statsString += currentDate + newline() + newline();

            roundsList = getRoundsListAtDateFromCursor(currentDate, roundsCursor);
            totalsList = getTotalsListFromRoundsList(roundsList);

            statsString += makeScoresStringFromScoresList(roundsList) + newline();
            statsString += makeScoresStringFromScoresList(totalsList) + newline() + newline();
            statsString += makeAverageStringFromScoresList(roundsList)+ newline() + newline();
        }
        return statsString;
    }

    public static String roundsAverageAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub){
        Double sumOfAllRounds = 0.0;
        Integer numberOfRounds = 0;
        Double average;
        String averageString;
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

        average = sumOfAllRounds/numberOfRounds;

        averageString = String.format("%.2f", average);

        return averageString.toString();
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

    private static ArrayList<Integer> getRoundsListAtDateFromCursor(String mostRecentDate, Cursor roundsCursor){
        Integer roundScore;
        ArrayList<Integer> roundsList = new ArrayList<>();

        do {
            roundScore = calculateRoundScoreAtCursor(roundsCursor);
            roundsList.add(0, roundScore);
        } while(isAnotherRoundWithSameDate(mostRecentDate, roundsCursor));

        return roundsList;
    }

    private static ArrayList<Integer> getRoundsListFromCursor(Cursor roundsCursor){
        Integer roundScore;
        ArrayList<Integer> roundsList = new ArrayList<>();

        do {
            roundScore = calculateRoundScoreAtCursor(roundsCursor);
            roundsList.add(0, roundScore);
            roundsCursor.moveToNext();
        } while(!roundsCursor.isAfterLast());

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

    private static boolean isAnotherRoundWithSameDate(String mostRecentDate, Cursor roundsCursor) {
        return roundsCursor.moveToNext() && extractDateStringFromDatetimeAtCursor(roundsCursor).equals(mostRecentDate);
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
