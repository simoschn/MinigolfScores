package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExporter {

    private static final String TAG = "FileExporter: ";

    public static void exportData(SQLiteDatabase courseDb, SQLiteDatabase holeNamesDb, SQLiteDatabase roundsDb){

        if(isExternalStorageWritable()) {
            writeStringToFile(makeExportCoursesString(courseDb), "courses.txt");
            writeStringToFile(makeExportHoleNamesString(holeNamesDb), "holenames.txt");
            writeStringToFile(makeExportRoundsString(roundsDb), "rounds.txt");
        }
        else
            Log.d(TAG, "Export failed, external storage is not writable.");
    }

    private static String makeExportCoursesString(SQLiteDatabase courseDb) {
        String buffer = "";

        Cursor courseCursor = courseDb.query("Courses", new String[] {"club", "system", "street", "street_number", "zipcode", "city"}, null, null, null, null, null);

        courseCursor.moveToFirst();

        while(!courseCursor.isAfterLast()){
            for(int i=0; i<6; i++)
                buffer += courseCursor.getString(i) + "#";
            buffer += "\n";
            courseCursor.moveToNext();
        }

        return buffer;
    }

    private static String makeExportHoleNamesString(SQLiteDatabase holeNamesDb) {
        String buffer = "";

    Cursor holeNamesCursor = holeNamesDb.query("HoleNames", new String[]{
            "club",
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
    }, null, null, null, null, null);

    holeNamesCursor.moveToFirst();

    while(!holeNamesCursor.isAfterLast()){
        for(int i=0; i<19; i++)
            buffer += holeNamesCursor.getString(i) + "#";
        buffer += "\n";
        holeNamesCursor.moveToNext();
    }

    return buffer;
}

    private static String makeExportRoundsString(SQLiteDatabase roundsDb) {
        String buffer = "";

        Cursor roundsCursor = roundsDb.query("Rounds", new String[]{
                "club",
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
        }, null, null, null, null, null);

        roundsCursor.moveToFirst();

        while(!roundsCursor.isAfterLast()){
            buffer += roundsCursor.getString(0) + "#";
            for(int i=1; i<20; i++)
                buffer += roundsCursor.getInt(i) + "#";
            buffer += "\n";
            roundsCursor.moveToNext();
        }

        return buffer;
    }

    private static void writeStringToFile(String buffer, String filename) {
        try {
            FileWriter out = new FileWriter(new File(getPath(), filename));
            out.write(buffer);
            out.close();
        }
        catch(IOException e){
            Log.d(TAG, "Export failed, can't open " + filename + " for writing!");
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static File getPath() {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        path.mkdirs();
        return path;
    }

}
