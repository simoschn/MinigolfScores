package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HoleNamesDB {

    public static Cursor setHoleNamesCursor(SQLiteDatabase holeNamesDb, String selectedClub) {
        return holeNamesDb.query("HoleNames", new String[]{
                "holeName1",
                "holeName2",
                "holeName3",
                "holeName4",
                "holeName5",
                "holeName6",
                "holeName7",
                "holeName8",
                "holeName9",
                "holeName10",
                "holeName11",
                "holeName12",
                "holeName13",
                "holeName14",
                "holeName15",
                "holeName16",
                "holeName17",
                "holeName18"
        }, "club='" + selectedClub + "'", null, null, null, null, null);
    }

}
