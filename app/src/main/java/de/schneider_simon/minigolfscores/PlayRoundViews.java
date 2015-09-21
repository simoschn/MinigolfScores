package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class PlayRoundViews {

    private static final Integer NUMBER_OF_HOLES = 18;
    private static final Integer INDEX_TOTAL_SCORE = 18;
    private static final Integer INDEX_SAVE_BUTTON = INDEX_TOTAL_SCORE + 1;
    private static final Integer INDEX_SUM_SCORE = 19;
    private static final Integer MAX_SHOTS = 7;
    private static final Integer ROW_MARGIN = 5;
    private static final Integer NUMBER_OF_HOLE_NAMES_COLUMNS = 2;
    private static final Integer RESERVED_COLUMNS = 1 + NUMBER_OF_HOLE_NAMES_COLUMNS;

    private static GridLayout gridLayout;
    private static TextView[] holeNames;
    private static Button[] holeScores;
    private static TextView[][] playedRounds;

    static SQLiteDatabase roundsDb = null;

    private static Integer numberOfColumns;
    private static Integer numberOfRows;

    private static String selectedClub;

    PlayRoundViews(GridLayout grid, String club, Context context) {
        gridLayout = grid;
        selectedClub = club;

        RoundsDBHelper dbHelper = new RoundsDBHelper(context.getApplicationContext());

        roundsDb = dbHelper.getWritableDatabase();

        numberOfColumns=gridLayout.getColumnCount();
        numberOfRows=gridLayout.getRowCount();

        holeNames=new TextView[numberOfRows];
        holeScores=new Button[numberOfRows];
        playedRounds=new TextView[numberOfColumns-RESERVED_COLUMNS][numberOfRows];

        createHoleNamesTextViews(context);
        createHoleScoresButtons(context);
        createPlayedRoundsTextViews(context);
    }

    private void createPlayedRoundsTextViews(Context context) {
        for (Integer xPos = 0; xPos < numberOfColumns - RESERVED_COLUMNS; xPos++) {
            for (Integer yPos = 0; yPos < numberOfRows; yPos++) {
                playedRounds[xPos][yPos] = new TextView(context);
                playedRounds[xPos][yPos].setPadding(0,0,0,0);
                playedRounds[xPos][yPos].setGravity(Gravity.CENTER);
            }
        }
    }

    private void createHoleScoresButtons(Context context) {
        for (Integer yPos = 0; yPos < numberOfRows; yPos++) {
            holeScores[yPos] = new Button(context, null, android.R.attr.buttonStyleSmall);
            holeScores[yPos].setPadding(0,0,0,0);
            holeScores[yPos].setGravity(Gravity.CENTER);
            holeScores[yPos].setBackgroundColor(Color.argb(255, 255, 255, 0));

            if(yPos <= NUMBER_OF_HOLES) {
                holeScores[yPos].setOnClickListener(new HoleScoreOnClickListener(yPos));
                holeScores[yPos].setOnLongClickListener(new HoleScoreOnLongClickListener(yPos));
            }

            if(yPos.equals(INDEX_SAVE_BUTTON)){
                holeScores[yPos].setOnClickListener(new SaveRoundOnClickListener());
                holeScores[yPos].setTextColor(Color.argb(255, 0, 255, 0));
                holeScores[yPos].setBackgroundColor(Color.argb(255, 0, 0, 0));
            }
        }
    }

    private void createHoleNamesTextViews(Context context) {
        for (Integer yPos = 0; yPos < numberOfRows; yPos++) {
            holeNames[yPos] = new TextView(context);
            holeNames[yPos].setPadding(0,0,0,0);
            holeNames[yPos].setGravity(Gravity.CENTER);
        }
    }

    public void putViewsIntoGridlayout(){
        Integer gridWidth = gridLayout.getWidth();
        Integer gridHeight = ((gridLayout.getHeight()) - (numberOfRows * ROW_MARGIN));

        Integer elementWidth = gridWidth / numberOfColumns;
        Integer elementHeight = gridHeight / numberOfRows;

        int column = 0;

        putHoleNamesIntoGridlayout(elementWidth, elementHeight, column);

        column+= NUMBER_OF_HOLE_NAMES_COLUMNS;

        putHoleScoresIntoGridlayout(elementWidth, elementHeight, column);

        column++;

        putPlayedRoundsIntoGridlayout(elementWidth, elementHeight, column);
    }

    private void putPlayedRoundsIntoGridlayout(Integer elementWidth, Integer elementHeight, int column) {
        for (Integer xPos = 0; xPos < numberOfColumns - RESERVED_COLUMNS; xPos++) {
            for (Integer yPos = 0; yPos < numberOfRows; yPos++) {

                GridLayout.Spec col = GridLayout.spec(xPos + column);
                GridLayout.Spec row = GridLayout.spec(yPos);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
                params.width = elementWidth;
                params.height = elementHeight;
                params.setMargins(0, 0, 0, 0);

                playedRounds[xPos][yPos].setLayoutParams(params);

                gridLayout.addView(playedRounds[xPos][yPos]);
            }
        }
    }

    private void putHoleScoresIntoGridlayout(Integer elementWidth, Integer elementHeight, int column) {
        for (Integer yPos = 0; yPos < numberOfRows; yPos++) {

            GridLayout.Spec col = GridLayout.spec(column);
            GridLayout.Spec row = GridLayout.spec(yPos);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = elementWidth;
            params.height = elementHeight;
            params.setMargins(0, 0, 0, 0);
            holeScores[yPos].setLayoutParams(params);

            gridLayout.addView(holeScores[yPos]);
        }
    }

    private void putHoleNamesIntoGridlayout(Integer elementWidth, Integer elementHeight, int column) {
        for (Integer yPos = 0; yPos < numberOfRows; yPos++) {
            GridLayout.Spec col = GridLayout.spec(column, NUMBER_OF_HOLE_NAMES_COLUMNS);
            GridLayout.Spec row = GridLayout.spec(yPos);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = elementWidth * NUMBER_OF_HOLE_NAMES_COLUMNS;
            params.height = elementHeight;
            params.setMargins(0, 0, 0, 0);
            holeNames[yPos].setLayoutParams(params);

            gridLayout.addView(holeNames[yPos]);
        }
    }

    private class SaveRoundOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int playedRoundsIndex = 0;

            saveRoundIntoRoundsDB();
            playedRoundsIndex = searchFreePlayedRoundsColumnOrShiftLeft(playedRoundsIndex);
            writePlayedRound(playedRoundsIndex);
            resetHoleScores();
        }
    }

    private void resetHoleScores() {
        for(int hole=0; hole<NUMBER_OF_HOLES; hole++)
            holeScores[hole].setText("1");

        holeScores[INDEX_TOTAL_SCORE].setText("18");
    }

    private void writePlayedRound(int playedRoundsIndex) {
        for(int hole=0; hole<NUMBER_OF_HOLES; hole++)
            playedRounds[playedRoundsIndex][hole].setText(holeScores[hole].getText());

        playedRounds[playedRoundsIndex][INDEX_TOTAL_SCORE].setText(holeScores[INDEX_TOTAL_SCORE].getText());

        writeSumScore(playedRoundsIndex);
    }

    private void writeSumScore(int playedRoundsIndex) {
        Integer sum_score;
        if(playedRoundsIndex == 0)
            sum_score = Integer.parseInt(holeScores[INDEX_TOTAL_SCORE].getText().toString());
        else
            sum_score = Integer.parseInt(holeScores[INDEX_TOTAL_SCORE].getText().toString()) +
                    Integer.parseInt(playedRounds[playedRoundsIndex-1][INDEX_SUM_SCORE].getText().toString());

        playedRounds[playedRoundsIndex][INDEX_SUM_SCORE].setText(sum_score.toString());
    }

    private int searchFreePlayedRoundsColumnOrShiftLeft(int playedRoundsIndex) {
        while((Integer.parseInt(playedRounds[playedRoundsIndex][0].getText().toString())) > 0) {
            playedRoundsIndex++;
            if(playedRoundsIndex == (numberOfColumns - RESERVED_COLUMNS)) {
                copyAllPlayedRoundsToPreviousRound();

                playedRoundsIndex = numberOfColumns - RESERVED_COLUMNS - 1;
                break;
            }
        }
        return playedRoundsIndex;
    }

    private void copyAllPlayedRoundsToPreviousRound() {
        for(int round = 1; round < (numberOfColumns - RESERVED_COLUMNS); round++)
            copyRoundToPreviousRound(round);
    }

    private void copyRoundToPreviousRound(int round) {
        for(int hole = 0; hole <= INDEX_SUM_SCORE; hole++){
            playedRounds[round-1][hole].setText(playedRounds[round][hole].getText());
        }
    }

    private void saveRoundIntoRoundsDB() {
        ContentValues values = new ContentValues();

        values.put("club", selectedClub);
        values.put("datetime", (System.currentTimeMillis()/1000));
        values.put("hole1", Integer.parseInt(holeScores[0].getText().toString()));
        values.put("hole2", Integer.parseInt(holeScores[1].getText().toString()));
        values.put("hole3", Integer.parseInt(holeScores[2].getText().toString()));
        values.put("hole4", Integer.parseInt(holeScores[3].getText().toString()));
        values.put("hole5", Integer.parseInt(holeScores[4].getText().toString()));
        values.put("hole6", Integer.parseInt(holeScores[5].getText().toString()));
        values.put("hole7", Integer.parseInt(holeScores[6].getText().toString()));
        values.put("hole8", Integer.parseInt(holeScores[7].getText().toString()));
        values.put("hole9", Integer.parseInt(holeScores[8].getText().toString()));
        values.put("hole10", Integer.parseInt(holeScores[9].getText().toString()));
        values.put("hole11", Integer.parseInt(holeScores[10].getText().toString()));
        values.put("hole12", Integer.parseInt(holeScores[11].getText().toString()));
        values.put("hole13", Integer.parseInt(holeScores[12].getText().toString()));
        values.put("hole14", Integer.parseInt(holeScores[13].getText().toString()));
        values.put("hole15", Integer.parseInt(holeScores[14].getText().toString()));
        values.put("hole16", Integer.parseInt(holeScores[15].getText().toString()));
        values.put("hole17", Integer.parseInt(holeScores[16].getText().toString()));
        values.put("hole18", Integer.parseInt(holeScores[17].getText().toString()));

        roundsDb.insert("Rounds", null, values);
    }

    class HoleScoreOnClickListener implements View.OnClickListener {
        int clickedButton;

        HoleScoreOnClickListener(int clickedButton){
            this.clickedButton = clickedButton;
        }

        public void onClick(View view) {
            Integer currentScore = Integer.parseInt(holeScores[clickedButton].getText().toString());

            updateHoleScore(currentScore);
            updateRoundScore();
        }

        private void updateHoleScore(Integer currentScore) {
            if(currentScore < MAX_SHOTS)
                currentScore++;
            else
                currentScore = 1;

            holeScores[clickedButton].setText(currentScore.toString());
        }
    }

    public void writeContentToViews(PlayRoundContent content){
        writeHoleNamesToViews(content);
        writeHoleScoresToViews(content);
        writePlayedRoundToViews(content);
    }

    private void writePlayedRoundToViews(PlayRoundContent content) {
        for(Integer xPos=0; xPos<(numberOfColumns-RESERVED_COLUMNS); xPos++)
            for(Integer yPos=0; yPos<numberOfRows; yPos++)
                playedRounds[xPos][yPos].setText(content.getPlayedRounds()[yPos + (xPos*numberOfRows)]);
    }

    private void writeHoleScoresToViews(PlayRoundContent content) {
        for(int i=0; i<numberOfRows; i++)
            holeScores[i].setText(content.getHoleScores()[i]);
    }

    private void writeHoleNamesToViews(PlayRoundContent content) {
        for(int i=0; i<NUMBER_OF_HOLES; i++)
            holeNames[i].setText(content.getHoleNames()[i]);
    }

    public void fillContentFromViews(PlayRoundContent content){
        fillHoleNamesContentFromViews(content);
        fillHoleScoresContentFromViews(content);
        fillPlayedRoundsContentFromViews(content);
    }

    private void fillPlayedRoundsContentFromViews(PlayRoundContent content) {
        CharSequence[] playedRoundsContent = new CharSequence[numberOfRows * (numberOfColumns-RESERVED_COLUMNS)];
        for(int xPos=0; xPos<(numberOfColumns-RESERVED_COLUMNS); xPos++)
            for(int yPos=0; yPos<numberOfRows; yPos++)
                playedRoundsContent[yPos + (xPos*numberOfRows)] = playedRounds[xPos][yPos].getText();
        content.setPlayedRounds(playedRoundsContent);
    }

    private void fillHoleScoresContentFromViews(PlayRoundContent content) {
        CharSequence[] holeScoresContent = new CharSequence[numberOfRows];
        for(int i=0; i<numberOfRows; i++)
            holeScoresContent[i] = holeScores[i].getText();
        content.setHoleScores(holeScoresContent);
    }

    private void fillHoleNamesContentFromViews(PlayRoundContent content) {
        CharSequence[] holeNamesContent = new CharSequence[NUMBER_OF_HOLES];
        for(int i=0; i<NUMBER_OF_HOLES; i++)
            holeNamesContent[i] = holeNames[i].getText();
        content.setHoleNames(holeNamesContent);
    }

    class HoleScoreOnLongClickListener implements View.OnLongClickListener {
        int clickedButton;

        HoleScoreOnLongClickListener(int clickedButton){
            this.clickedButton = clickedButton;
        }

        public boolean onLongClick(View view) {
            resetHoleScore();
            updateRoundScore();
            return true;
        }

        private void resetHoleScore() {
            holeScores[clickedButton].setText("1");
        }
    }

    private void updateRoundScore() {
        Integer roundScore = 0;
        for(int i=0; i<NUMBER_OF_HOLES; i++)
            roundScore += Integer.parseInt(holeScores[i].getText().toString());

        holeScores[INDEX_TOTAL_SCORE].setText(roundScore.toString());
    }
}
