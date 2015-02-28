import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 28.02.15.
 */

public class CourseDBHelper extends SQLiteOpenHelper {

    private final static String CREATE_CMD = "CREATE TABLE Courses ("
            + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "club" + " TEXT, "
            + "system" + " TEXT, "
            + "street" + " TEXT, "
            + "streetNumber" + " TEXT, "
            + "zipCode" + " TEXT, "
            + "city" + " TEXT)";

    public CourseDBHelper(Context context){
        super(context, "CourseDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
