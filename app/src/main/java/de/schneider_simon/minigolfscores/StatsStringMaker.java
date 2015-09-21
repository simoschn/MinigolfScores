package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class StatsStringMaker {
    private final static String TAG = "StatsStringMaker: ";

    public static SpannableString averageAndAcePercentagePerHole(SQLiteDatabase roundsDb, SQLiteDatabase holeNamesDb, String clubName, boolean sorted){

        String buffer = "";
        Vector<HoleStats> holesStats = getHoleStatsVector(roundsDb, holeNamesDb, clubName, sorted);

        for(HoleStats holeStats : holesStats)
            buffer += (holeStats.toString() + newline());

        return new SpannableString(buffer);
    }

    private static Vector<HoleStats> getHoleStatsVector(SQLiteDatabase roundsDb, SQLiteDatabase holeNamesDb, String clubName, boolean sorted){
        HoleStatsVector holesStats = new HoleStatsVector();
        Cursor holeNamesCursor = HoleNamesDB.setHoleNamesCursor(holeNamesDb, clubName);
        Cursor roundsCursor = RoundsDB.setRoundsCursor(roundsDb, clubName);

        holeNamesCursor.moveToFirst();

        for(int i=0; i<18; i++){
            HoleStats element = new HoleStats();
            element.setRunningNumber(i+1);

            if(holeNamesCursor.isAfterLast())
                element.setName("Bahn " + (i+1));
            else
                element.setName(holeNamesCursor.getString(i));

            element.setAcePercentage(calculateAcePercentage(roundsCursor, i));
            element.setAverage(calculateAverage(roundsCursor, i));
            holesStats.add(element);

        }

        if(sorted)
            holesStats.sort();

        return holesStats.getVector();
    }

    private static double calculateAcePercentage(Cursor roundsCursor, int holeIndex){
        int position = roundsCursor.getPosition();
        roundsCursor.moveToFirst();
        double aces=0.0;

        do{
            if(roundsCursor.getInt(holeIndex+1)==1)
                aces++;
           roundsCursor.moveToNext();
        }while(!roundsCursor.isAfterLast());

        roundsCursor.moveToPosition(position);

        if(aces>0)
            return (aces/roundsCursor.getCount())*100;
        else
            return 0.0;
    }

    private static double calculateAverage(Cursor roundsCursor, int holeIndex){
        int position = roundsCursor.getPosition();
        roundsCursor.moveToFirst();
        double sumShots = 0.0;

        do{
            sumShots += roundsCursor.getInt(holeIndex+1);
            roundsCursor.moveToNext();
        }while(!roundsCursor.isAfterLast());

        roundsCursor.moveToPosition(position);

        return sumShots/roundsCursor.getCount();
    }

    public static String lastTrainingAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub) {
        Cursor roundsCursor = RoundsDB.setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";
        else
            return makeStatsStringForCurrentDate(roundsCursor);
    }

    public static SpannableString allRounds(SQLiteDatabase roundsDb, String selectedClub){
        String statsString="";
        Cursor roundsCursor = RoundsDB.setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            statsString = "";
        else{
            while(!roundsCursor.isAfterLast()){
                statsString += (makeStatsStringForCurrentDate(roundsCursor) + newline() + newline());
                moveRoundsCursorToNextDate(roundsCursor);
            }
        }
        return new SpannableString(statsString);
    }

    public static SpannableString allRoundsDetail(SQLiteDatabase roundsDb, SQLiteDatabase holeNamesDb, String selectedClub){
        String statsString="";
        Cursor roundsCursor = RoundsDB.setRoundsCursor(roundsDb, selectedClub);
        Cursor holeNamesCursor = HoleNamesDB.setHoleNamesCursor(holeNamesDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            statsString = "";
        else{
            while(!roundsCursor.isAfterLast()){
                statsString += (makeStatsStringDetailForCurrentDate(roundsCursor, holeNamesCursor) +newline() + newline());
                moveRoundsCursorToNextDate(roundsCursor);
            }
        }
        return new SpannableString(statsString);
    }

    public static String roundsAverage(SQLiteDatabase roundsDb, String selectedClub){
        Double sumOfAllRounds = 0.0;
        Integer numberOfRounds = 0;
        ArrayList<Integer> allRoundsList;
        Cursor roundsCursor = RoundsDB.setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        allRoundsList = getAllRoundsList(roundsCursor);

        for(Integer round : allRoundsList){
            sumOfAllRounds += round;
            numberOfRounds++;
        }

        return String.format("%.2f", sumOfAllRounds/numberOfRounds);
    }

    public static String totalNumberOfRounds(SQLiteDatabase roundsDb, String selectedClub){
        ArrayList<Integer> allRoundsList;
        Cursor roundsCursor = RoundsDB.setRoundsCursor(roundsDb, selectedClub);
        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        allRoundsList = getAllRoundsList(roundsCursor);

        return ((Integer)allRoundsList.size()).toString();
    }

    private static String makeStatsStringForCurrentDate(Cursor roundsCursor) {
        ArrayList<Integer> roundsList = getCurrentDateRoundsList(roundsCursor);
        ArrayList<Integer> totalsList = getTotalsList(roundsList);

        return extractDateString(roundsCursor)
                + newline() + newline()
                + makeScoresString(roundsList)
                + newline()
                + makeScoresString(totalsList)
                + newline() + newline()
                + makeAverageString(roundsList)
                + separator();
    }

    private static String makeStatsStringDetailForCurrentDate(Cursor roundsCursor, Cursor holeNamesCursor) {
        ArrayList<Integer> roundsList = getCurrentDateRoundsList(roundsCursor);
        ArrayList<Round> detailedRoundsList = getCurrentDateDetailedRoundsList(roundsCursor);

        ArrayList<Integer> totalsList = getTotalsList(roundsList);
        ArrayList<String> holeNamesList = getHoleNamesList(holeNamesCursor);

        return extractDateString(roundsCursor)
                + newline() + newline()
                + makeDetailedRoundsString(detailedRoundsList, holeNamesList)
//                + placeholder()
                + makeScoresString(roundsList)
                + newline()
//                + placeholder()
                + makeScoresString(totalsList)
                + newline() + newline()
                + makeAverageString(roundsList)
                + separator();
    }

    private static String makeDetailedRoundsString(ArrayList<Round> detailedRoundsList, ArrayList<String> holeNamesList) {
        String buffer = "";
        for(int hole=0; hole<18; hole++) {
            buffer += String.format("%-15s", holeNamesList.get(hole));

            for (Round round : detailedRoundsList) {
                buffer += String.format("%d", round.getHole(hole));
            }

            buffer += newline();
        }
        return buffer;
    }

    private static void moveRoundsCursorToNextDate(Cursor roundsCursor){
        String date = extractDateString(roundsCursor);

        do
        {
            roundsCursor.moveToNext();
        }while(!roundsCursor.isAfterLast() && date.equals(extractDateString(roundsCursor)));
    }

    private static ArrayList<Integer> getAllRoundsList(Cursor roundsCursor){
        ArrayList<Integer> roundsList = new ArrayList<>();
        int position = roundsCursor.getPosition();

        do {
            roundsList.add(0, calculateRoundScoreAtCursor(roundsCursor));
            roundsCursor.moveToNext();

        } while(!roundsCursor.isAfterLast());

        roundsCursor.moveToPosition(position);

        return roundsList;
    }

    private static ArrayList<Integer> getCurrentDateRoundsList(Cursor roundsCursor){
        ArrayList<Integer> roundsList = new ArrayList<>();
        String date = extractDateString(roundsCursor);
        int position = roundsCursor.getPosition();

        do {
            roundsList.add(0, calculateRoundScoreAtCursor(roundsCursor));
            roundsCursor.moveToNext();

        } while(isMoreRoundsWithSameDate(date, roundsCursor));

        roundsCursor.moveToPosition(position);

        return roundsList;
    }

    private static ArrayList<Round> getCurrentDateDetailedRoundsList(Cursor roundsCursor){
        ArrayList<Round> detailedRoundsList = new ArrayList<>();
        String date = extractDateString(roundsCursor);
        int position = roundsCursor.getPosition();

        do {
            detailedRoundsList.add(0, fillOneRound(roundsCursor));
            roundsCursor.moveToNext();

        } while(isMoreRoundsWithSameDate(date, roundsCursor));

        roundsCursor.moveToPosition(position);

        return detailedRoundsList;
    }

    private static Round fillOneRound(Cursor roundsCursor){
        Round round = new Round();

        for (Integer i = 1; i <= 18; i++)
            round.setHole(roundsCursor.getInt(i), i-1);

        return round;
    }

    private static ArrayList<Integer> getTotalsList(ArrayList<Integer> roundsList){
        ArrayList<Integer> totalsList = new ArrayList<>();

        totalsList.add(roundsList.get(0));

        for(Integer i=1; i<roundsList.size(); i++){
            totalsList.add(totalsList.get(i-1) + roundsList.get(i));
        }

        return totalsList;
    }

    private static ArrayList<String> getHoleNamesList(Cursor holeNamesCursor){
        ArrayList<String> holeNames = new ArrayList<>();

        if(holeNamesCursor.getCount() == 0) {
            for (Integer i = 1; i <= 18; i++) {
                holeNames.add("Bahn " + i.toString() + " ");
            }
        }
        else{
            holeNamesCursor.moveToFirst();

            for(int i=0; i<18; i++){
                String holeName = holeNamesCursor.getString(i);

                if(holeName.length() > 15)
                    holeNames.add(holeName.substring(0, 15));
                else
                    holeNames.add(holeName);
            }
        }

        return holeNames;
    }

    private static String makeScoresString(ArrayList<Integer> scoresList) {
        String scoresString = "";

        for(Integer round : scoresList)
                scoresString += String.format("%4d", round);

        return scoresString;
    }

    private static String makeAverageString(ArrayList<Integer> scoresList){
        Double totalScore = 0.0;

        for(Integer round : scoresList){
            totalScore += round;
        }
        return String.format("%.2f", totalScore / scoresList.size());
    }



    private static Integer calculateRoundScoreAtCursor(Cursor roundsCursor) {
        Integer roundScore;
        roundScore = 0;
        for (int i = 1; i <= 18; i++)
            roundScore += roundsCursor.getInt(i);
        return roundScore;
    }

    private static boolean isMoreRoundsWithSameDate(String date, Cursor roundsCursor) {
        if(roundsCursor.isAfterLast())
            return false;
        else
            return extractDateString(roundsCursor).equals(date);
    }

    private static boolean isNoRoundPlayedYet(Cursor roundsCursor) {
        return roundsCursor.getCount() == 0;
    }

    private static String extractDateString(Cursor roundsCursor) {
        String FORMAT = "dd.MM.yyyy";
        Long lastDate = roundsCursor.getLong(0);
        Date date = new Date(lastDate * 1000);
        SimpleDateFormat simpleDate = new SimpleDateFormat(FORMAT);
        return simpleDate.format(date);
    }

    private static String newline(){
        return "\n";
    }

    private static String separator(){
         return"\n\n________________________________\n";
    }

    private static String placeholder(){
        return String.format("%-15s", "");
    }

}
