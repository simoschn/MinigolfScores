package de.schneider_simon.minigolfscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RoundsDBHelper extends SQLiteOpenHelper {

    private final static String CREATE_CMD = "CREATE TABLE Rounds ("
            + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "club" + " TEXT, "
            + "datetime" + " INT, "
            + "hole1" + " INT, "
            + "hole2" + " INT, "
            + "hole3" + " INT, "
            + "hole4" + " INT, "
            + "hole5" + " INT, "
            + "hole6" + " INT, "
            + "hole7" + " INT, "
            + "hole8" + " INT, "
            + "hole9" + " INT, "
            + "hole10" + " INT, "
            + "hole11" + " INT, "
            + "hole12" + " INT, "
            + "hole13" + " INT, "
            + "hole14" + " INT, "
            + "hole15" + " INT, "
            + "hole16" + " INT, "
            + "hole17" + " INT, "
            + "hole18" + " INT)";

    public RoundsDBHelper(Context context){
        super(context, "RoundsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
