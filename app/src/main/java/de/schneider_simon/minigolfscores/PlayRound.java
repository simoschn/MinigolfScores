package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;


public class PlayRound extends ActionBarActivity {

    private static final String TAG = "PlayRound";

    private static final Integer ROW_MARGIN = 5;
    private static final Integer NUMBER_OF_HOLES = 18;
    private static final Integer INDEX_TOTAL_SCORE = 18;
    private static final Integer INDEX_SAVE_BUTTON = 19;
    private static final Integer INDEX_SUM_SCORE = 19;
    private static final Integer MAX_SHOTS = 7;
    private static final Integer NUMBER_OF_HOLE_NAMES_COLUMNS = 2;
    private static final Integer RESERVED_COLUMNS = 1 + NUMBER_OF_HOLE_NAMES_COLUMNS;

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    static SQLiteDatabase roundsDb = null;

    GridLayout gridLayout;
    TextView[] holeNames;
    Button[] holeScores;
    TextView[][] playedRounds;

    Integer numberOfColumns;
    Integer numberOfRows;
    Integer gridWidth;
    Integer gridHeight;
    Integer elementWidth;
    Integer elementHeight;

    String selectedClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);

        selectedClub = getIntent().getStringExtra("club");

        RoundsDBHelper dbHelper = new RoundsDBHelper(getApplicationContext());

        roundsDb = dbHelper.getWritableDatabase();

        gridLayout = (GridLayout) findViewById(R.id.play_round_grid);
        numberOfColumns = gridLayout.getColumnCount();
        numberOfRows = gridLayout.getRowCount();

        holeNames = new TextView[numberOfRows];
        holeScores = new Button[numberOfRows];
        playedRounds = new TextView[numberOfColumns - RESERVED_COLUMNS][numberOfRows];

        for (Integer yPos = 0; yPos < numberOfRows; yPos++) {
            holeNames[yPos] = new TextView(this);
            Integer holeNumber = yPos + 1;

            if (yPos < NUMBER_OF_HOLES)
                holeNames[yPos].setText("Bahn " + (holeNumber.toString()));
        }

        for (Integer yPos = 0; yPos < numberOfRows; yPos++) {
            holeScores[yPos] = new Button(this, null, android.R.attr.buttonStyleSmall);
            holeScores[yPos].setTextSize(16);
            holeScores[yPos].setPadding(0, 0, 0, 0);

            if (yPos < NUMBER_OF_HOLES) {
                holeScores[yPos].setText("1");
                holeScores[yPos].setBackgroundColor(Color.argb(255, 255, 255, 0));
                holeScores[yPos].setOnClickListener(new MyHoleScoreOnClickListener(yPos));
            }

            if (yPos == INDEX_TOTAL_SCORE) {
                holeScores[yPos].setText("18");
                holeScores[yPos].setBackgroundColor(Color.argb(255, 255, 255, 0));
            }

            if (yPos == INDEX_SAVE_BUTTON) {
                holeScores[yPos].setText("\u2714");
                holeScores[yPos].setTextColor(Color.argb(255, 0, 255, 0));
                holeScores[yPos].setBackgroundColor(Color.argb(255, 0, 0, 0));
                holeScores[yPos].setOnClickListener(new MySaveRoundOnClickListener());
            }
        }

        for (Integer xPos = 0; xPos < numberOfColumns - RESERVED_COLUMNS; xPos++) {
            for (Integer yPos = 0; yPos < numberOfRows; yPos++) {

                playedRounds[xPos][yPos] = new TextView(this);

                playedRounds[xPos][yPos].setGravity(Gravity.CENTER);
                playedRounds[xPos][yPos].setText("0");
                playedRounds[xPos][yPos].setBackgroundColor(Color.argb(255, 255, 255, 255));
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        setTitle(selectedClub);

        gridWidth = gridLayout.getWidth();
        gridHeight = ((gridLayout.getHeight()) - (numberOfRows * ROW_MARGIN));

        elementWidth = gridWidth / numberOfColumns;
        elementHeight = gridHeight / numberOfRows;

        int column = 0;

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

        column+= NUMBER_OF_HOLE_NAMES_COLUMNS;

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

        column++;

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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_round, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyHoleScoreOnClickListener implements View.OnClickListener {

        int clickedButton;

        MyHoleScoreOnClickListener(int clickedButton){
            this.clickedButton = clickedButton;
        }

        public void onClick(View view) {

            Integer roundScore = 0;
            Integer currentScore = Integer.parseInt(holeScores[clickedButton].getText().toString());

            if(currentScore < MAX_SHOTS)
                currentScore++;
            else
                currentScore = 1;

            holeScores[clickedButton].setText(currentScore.toString());

            for(int i=0; i<NUMBER_OF_HOLES; i++)
                roundScore += Integer.parseInt(holeScores[i].getText().toString());

            holeScores[INDEX_TOTAL_SCORE].setText(roundScore.toString());

        }
    }

    private class MySaveRoundOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Integer sum_score;

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

            int playedRoundsIndex = 0;

            while((Integer.parseInt(playedRounds[playedRoundsIndex][0].getText().toString())) > 0) {
                playedRoundsIndex++;
                if(playedRoundsIndex == (numberOfColumns - RESERVED_COLUMNS)) {
                    for(int round = 1; round < (numberOfColumns - RESERVED_COLUMNS); round++){
                        for(int hole = 0; hole <= INDEX_SUM_SCORE; hole++){
                            playedRounds[round-1][hole].setText(playedRounds[round][hole].getText());
                        }
                    }

                    playedRoundsIndex = numberOfColumns - RESERVED_COLUMNS - 1;
                    break;
                }
            }

            if(playedRoundsIndex == 0)
                sum_score = Integer.parseInt(holeScores[INDEX_TOTAL_SCORE].getText().toString());
            else
                sum_score = Integer.parseInt(holeScores[INDEX_TOTAL_SCORE].getText().toString()) +
                        Integer.parseInt(playedRounds[playedRoundsIndex-1][INDEX_SUM_SCORE].getText().toString());

            playedRounds[playedRoundsIndex][INDEX_SUM_SCORE].setText(sum_score.toString());

            for(int hole=0; hole<NUMBER_OF_HOLES; hole++) {
                playedRounds[playedRoundsIndex][hole].setText(holeScores[hole].getText());
                holeScores[hole].setText("1");
            }

            playedRounds[playedRoundsIndex][INDEX_TOTAL_SCORE].setText(holeScores[INDEX_TOTAL_SCORE].getText());

            holeScores[INDEX_TOTAL_SCORE].setText("18");

        }
    }
}
