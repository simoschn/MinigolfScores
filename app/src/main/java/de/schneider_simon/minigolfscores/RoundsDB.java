package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RoundsDB {

    public static Cursor setRoundsCursor(SQLiteDatabase roundsDb, String selectedClub) {
        Cursor cursor = roundsDb.query("Rounds", new String[]{
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

        cursor.moveToFirst();

        return cursor;
    }
}
