package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsStringMaker {

    private static String TAG = "StatsStringMaker: ";
    private static String FORMAT = "dd.MM.yyyy";

    public static String lastTrainingAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub) {

        String statsString;
        String mostRecentDate;

        Cursor roundsCursor = setRoundsCursor(roundsDb, selectedClub);

        if(isNoRoundPlayedYet(roundsCursor))
            return "";

        roundsCursor.moveToFirst();
        mostRecentDate = extractDateStringFromDatetimeAtCursor(roundsCursor);
        statsString = mostRecentDate + newline();

        return statsString + getPlayedRoundsAtDateFromCursor(mostRecentDate, roundsCursor);
    }

    private static String getPlayedRoundsAtDateFromCursor(String mostRecentDate, Cursor roundsCursor) {
        Integer roundScore;
        String playedRoundsAtDate = "";
        do {
            roundScore = calculateRoundScoreAtCursor(roundsCursor);
            playedRoundsAtDate += roundScore + space();
            } while(isAnotherRoundWithSameDate(mostRecentDate, roundsCursor));
        return playedRoundsAtDate;
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

    private static String space(){
        return " ";
    }
}
