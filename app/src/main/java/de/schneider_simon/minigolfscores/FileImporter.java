package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileImporter {

    private static File path;

    private static final String TAG = "FileImporter: ";

    public static void importData(SQLiteDatabase courseDb, SQLiteDatabase holeNamesDb, SQLiteDatabase roundsDb){

        path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);

        importCourses(courseDb);
        importHoleNames(holeNamesDb);
        importRounds(roundsDb);

        holeNamesDb.close();
    }

    private static void importCourses(SQLiteDatabase courseDb){
        String filename = "courses.txt";

        if(isFileUsable(filename)) {
            courseDb.delete("Courses", null, null);

            try {
                FileReader in = new FileReader(new File(path, filename));
                BufferedReader br = new BufferedReader(in);

                while(br.ready())
                    writeLineToCourseDb(courseDb, br.readLine());

                br.close();
                in.close();

            }
            catch(IOException e){
                Log.d(TAG, "Import failed, can't open " + filename + " for reading!");
            }
        }
    }

    private static void importHoleNames(SQLiteDatabase holeNamesDb){

        String filename = "holenames.txt";

        if(isFileUsable(filename)) {
            holeNamesDb.delete("HoleNames", null, null);

            try {
                FileReader in = new FileReader(new File(path, filename));
                BufferedReader br = new BufferedReader(in);

                while(br.ready())
                    writeLineToHoleNamesDb(holeNamesDb, br.readLine());

                br.close();
                in.close();

            }
            catch(IOException e){
                Log.d(TAG, "Import failed, can't open " + filename + " for reading!");
            }
        }
    }

    private static void importRounds(SQLiteDatabase roundsDb){
        String filename = "rounds.txt";

        if(isFileUsable(filename)) {
            roundsDb.delete("Rounds", null, null);

            try {
                FileReader in = new FileReader(new File(path, filename));
                BufferedReader br = new BufferedReader(in);

                while(br.ready())
                    writeLineToRoundsDb(roundsDb, br.readLine());

                br.close();
                in.close();

            }
            catch(IOException e){
                Log.d(TAG, "Import failed, can't open " + filename + " for reading!");
            }
        }
    }

    private static void writeLineToHoleNamesDb(SQLiteDatabase holeNamesDb, String line){
        String[] tokens = line.split("#");

        if(tokens.length != 19){
            Log.d(TAG, "Write line to HoleNamesDB failed, line corrupt!");
            return;
        }

        ContentValues values = new ContentValues();

        values.put("club", tokens[0]);
        values.put("holeName1", tokens[1]);
        values.put("holeName2", tokens[2]);
        values.put("holeName3", tokens[3]);
        values.put("holeName4", tokens[4]);
        values.put("holeName5", tokens[5]);
        values.put("holeName6", tokens[6]);
        values.put("holeName7", tokens[7]);
        values.put("holeName8", tokens[8]);
        values.put("holeName9", tokens[9]);
        values.put("holeName10", tokens[10]);
        values.put("holeName11", tokens[11]);
        values.put("holeName12", tokens[12]);
        values.put("holeName13", tokens[13]);
        values.put("holeName14", tokens[14]);
        values.put("holeName15", tokens[15]);
        values.put("holeName16", tokens[16]);
        values.put("holeName17", tokens[17]);
        values.put("holeName18", tokens[18]);

        holeNamesDb.insert("HoleNames", null, values);

    }

    private static void writeLineToCourseDb(SQLiteDatabase courseDb, String line){
        String[] tokens = line.split("#");

        if(tokens.length != 6){
            Log.d(TAG, "Write line to CourseDB failed, line corrupt!");
            return;
        }

        ContentValues values = new ContentValues();

        values.put("club", tokens[0]);
        values.put("system", tokens[1]);
        values.put("street", tokens[2]);
        values.put("street_number", tokens[3]);
        values.put("zipcode", tokens[4]);
        values.put("city", tokens[5]);

        courseDb.insert("Courses", null, values);

    }

    private static void writeLineToRoundsDb(SQLiteDatabase roundsDb, String line){
        String[] tokens = line.split("#");

        if(tokens.length != 20){
            Log.d(TAG, "Write line to RoundsDB failed, line corrupt!");
            return;
        }

        ContentValues values = new ContentValues();

        values.put("club", tokens[0]);
        try{
            values.put("datetime", Integer.parseInt(tokens[1]));
            values.put("hole1", Integer.parseInt(tokens[2]));
            values.put("hole2", Integer.parseInt(tokens[3]));
            values.put("hole3", Integer.parseInt(tokens[4]));
            values.put("hole4", Integer.parseInt(tokens[5]));
            values.put("hole5", Integer.parseInt(tokens[6]));
            values.put("hole6", Integer.parseInt(tokens[7]));
            values.put("hole7", Integer.parseInt(tokens[8]));
            values.put("hole8", Integer.parseInt(tokens[9]));
            values.put("hole9", Integer.parseInt(tokens[10]));
            values.put("hole10", Integer.parseInt(tokens[11]));
            values.put("hole11", Integer.parseInt(tokens[12]));
            values.put("hole12", Integer.parseInt(tokens[13]));
            values.put("hole13", Integer.parseInt(tokens[14]));
            values.put("hole14", Integer.parseInt(tokens[15]));
            values.put("hole15", Integer.parseInt(tokens[16]));
            values.put("hole16", Integer.parseInt(tokens[17]));
            values.put("hole17", Integer.parseInt(tokens[18]));
            values.put("hole18", Integer.parseInt(tokens[19]));
        }catch(NumberFormatException e){
            Log.d(TAG, "Write line to RoundsDB skipped, token is not Integer.");
        }

        roundsDb.insert("Rounds", null, values);
    }

    private static boolean isFileUsable(String filename){
        String buffer = "";

        try {
            FileReader in = new FileReader(new File(path, filename));
            BufferedReader br = new BufferedReader(in);
            buffer = br.readLine();
            br.close();
            in.close();
            if(buffer.equals("")){
                Log.d(TAG, "Import stopped, " + filename + " is empty!");
                return false;
            }
            else {
                Log.d(TAG, "Import possible from " + filename);
                return true;
            }
        }
        catch(IOException e){
            Log.d(TAG, "Import failed, can't open " + filename + " for reading!");
            return false;
        }
    }
}
