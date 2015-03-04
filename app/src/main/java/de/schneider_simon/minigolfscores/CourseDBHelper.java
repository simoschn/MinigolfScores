package de.schneider_simon.minigolfscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 28.02.15.
 */

public class CourseDBHelper extends SQLiteOpenHelper {

    private final static String CREATE_CMD = "CREATE TABLE Courses ("
            + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "club" + " TEXT, "
            + "system" + " TEXT, "
            + "street" + " TEXT, "
            + "street_number" + " TEXT, "
            + "zipcode" + " TEXT, "
            + "city" + " TEXT)";

    public CourseDBHelper(Context context){
        super(context, "CourseDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);

        Log.d("CourseDBHelper", "execSQL executed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}