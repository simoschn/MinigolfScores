package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExporter {

    public static void export(SQLiteDatabase courseDb, SQLiteDatabase holeNamesDb, SQLiteDatabase roundsDb){
        exportCourses(courseDb);
        exportHoleNames(holeNamesDb);
        exportRounds(roundsDb);
    }

    private static void exportCourses(SQLiteDatabase courseDb){
        String buffer = makeExportCoursesString(courseDb);

        try {
            FileWriter out = new FileWriter(new File("C:/courses.txt"));
            out.write(buffer);
            out.close();
            Log.d("FileExporter: ", "Courses exported to courses.txt.");
        }
        catch(IOException e){
            Log.d("FileExporter: ", "Can't open courses.txt for writing!");
        }
    }

    private static String makeExportCoursesString(SQLiteDatabase courseDb) {
        String buffer = "";

        Cursor courseCursor = courseDb.query("Courses", new String[] {"club", "system", "street", "street_number", "zipcode", "city"}, null, null, null, null, null);

        courseCursor.moveToFirst();

        while(!courseCursor.isAfterLast()){
            for(int i=0; i<6; i++)
                buffer += courseCursor.getString(i) + " ";
            buffer += "\n";
            courseCursor.moveToNext();
        }

        return buffer;
    }

    private static void exportHoleNames(SQLiteDatabase holeNamesDb){

    }

    private static void exportRounds(SQLiteDatabase roundsDb){

    }

}
