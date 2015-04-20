package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsStringMaker {

    private static String TAG = "StatsStringMaker: ";

    public static String lastTrainingAtSelectedClub(SQLiteDatabase roundsDb, String selectedClub) {

        Long lastDate;
        String buffer = "";
        Integer roundScore = 0;
        String mostRecentDate = "";

        Cursor roundsCursor = roundsDb.query("Rounds", new String[]{
                "datetime",
                "hole1",
                "hole2",
                "hole3",
                "hole4",
                "hole5",
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

        roundsCursor.moveToFirst();

        if (roundsCursor.getCount() > 0) {
            lastDate = roundsCursor.getLong(0);
            Date date = new Date(lastDate * 1000);
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy");
            mostRecentDate = simpleDate.format(date);
            buffer = simpleDate.format(date) + newline();

            for(;;) {
                roundScore = 0;

                for (int i = 1; i <= 18; i++)
                    roundScore += roundsCursor.getInt(i);

                buffer += roundScore + space();

                roundsCursor.moveToNext();

                if(roundsCursor.isAfterLast())
                    break;

                lastDate = roundsCursor.getLong(0);
                date = new Date(lastDate * 1000);
                simpleDate = new SimpleDateFormat("dd.MM.yyyy");

  /*            Log.d(TAG, mostRecentDate);
                Log.d(TAG, simpleDate.format(date));

               if(!mostRecentDate.equals(simpleDate.format(date)))
                    break;*/
            }
        }
        return buffer;
    }

    private static String newline(){
        return "\n";
    }

    private static String space(){
        return " ";
    }
}
