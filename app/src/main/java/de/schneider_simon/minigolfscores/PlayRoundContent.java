package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class PlayRoundContent {

    private static CharSequence[] holeNames;
    private static CharSequence[] holeScores;
    private static CharSequence[] playedRounds;

    private static Integer numberOfColumns;
    private static Integer numberOfRows;
    private static Integer reservedColumns;

    private static String[] holeNamesFromDb = new String[18];

    public PlayRoundContent(Integer cols, Integer rows, Integer reservedCols){

        numberOfColumns = cols;
        numberOfRows = rows;
        reservedColumns = reservedCols;

        holeNames = new CharSequence[numberOfRows - 2];
        holeScores = new CharSequence[numberOfRows];
        playedRounds = new CharSequence[(numberOfColumns - reservedColumns) * numberOfRows];

        holeNamesFromDb = null;
    }

    public PlayRoundContent(Integer cols, Integer rows, Integer reservedCols, String[] holeNamesDb){

        numberOfColumns = cols;
        numberOfRows = rows;
        reservedColumns = reservedCols;

        holeNames = new CharSequence[numberOfRows - 2];
        holeScores = new CharSequence[numberOfRows];
        playedRounds = new CharSequence[(numberOfColumns - reservedColumns) * numberOfRows];

        holeNamesFromDb = holeNamesDb;
    }

    public void init(){
        initHoleNames();
        initHoleScores();
        initPlayedRounds();
    }

    private void initPlayedRounds() {
        for(int i = 0; i < (numberOfColumns-reservedColumns)*numberOfRows; i++ )
                playedRounds[i] = "0";
    }

    private void initHoleScores() {
        final String SAVE_ICON = "\u2714";

        for(int i=0; i < numberOfRows-2; i++)
            holeScores[i] = "1";

        holeScores[numberOfRows-2] = "18";
        holeScores[numberOfRows-1] = SAVE_ICON;
    }

    private void initHoleNames() {
        if(holeNamesFromDb == null)
            for(int i=0; i < numberOfRows-2; i++)
                holeNames[i] = "Bahn " + (i+1);
        else
            System.arraycopy(holeNamesFromDb, 0, holeNames, 0, holeNamesFromDb.length);
    }

    public void setContentFromBundle(Bundle bundle){
        holeNames = bundle.getCharSequenceArray("holeNames");
        holeScores = bundle.getCharSequenceArray("holeScores");
        playedRounds = bundle.getCharSequenceArray("playedRounds");
    }

    public void putContentToBundle(Bundle bundle){
        bundle.putCharSequenceArray("holeNames", holeNames);
        bundle.putCharSequenceArray("holeScores", holeScores);
        bundle.putCharSequenceArray("playedRounds", playedRounds);
    }

    public CharSequence[] getHoleNames(){
        return holeNames;
    }

    public CharSequence[] getHoleScores(){
        return holeScores;
    }

    public CharSequence[] getPlayedRounds(){
        return playedRounds;
    }

    public void setHoleNames(CharSequence[] names){
        holeNames = names;
    }

    public void setHoleScores(CharSequence[] scores){
        holeScores = scores;
    }

    public void setPlayedRounds(CharSequence[] rounds){
        playedRounds = rounds;
    }
}
