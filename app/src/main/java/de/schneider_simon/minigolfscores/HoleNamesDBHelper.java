package de.schneider_simon.minigolfscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HoleNamesDBHelper extends SQLiteOpenHelper {
    private final static String CREATE_CMD = "CREATE TABLE HoleNames ("
            + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "club" + " TEXT, "
            + "holeName1" + " TEXT, "
            + "holeName2" + " TEXT, "
            + "holeName3" + " TEXT, "
            + "holeName4" + " TEXT, "
            + "holeName5" + " TEXT, "
            + "holeName6" + " TEXT, "
            + "holeName7" + " TEXT, "
            + "holeName8" + " TEXT, "
            + "holeName9" + " TEXT, "
            + "holeName10" + " TEXT, "
            + "holeName11" + " TEXT, "
            + "holeName12" + " TEXT, "
            + "holeName13" + " TEXT, "
            + "holeName14" + " TEXT, "
            + "holeName15" + " TEXT, "
            + "holeName16" + " TEXT, "
            + "holeName17" + " TEXT, "
            + "holeName18" + " TEXT)";

    public HoleNamesDBHelper(Context context){
        super(context, "HoleNamesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
