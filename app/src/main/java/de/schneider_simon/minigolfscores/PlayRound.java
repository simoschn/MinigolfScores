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

public class PlayRound extends ActionBarActivity {

    private static final String TAG = "PlayRound";


    private static final Integer NUMBER_OF_HOLES = 18;
    private static final Integer INDEX_TOTAL_SCORE = 18;

    GridLayout gridLayout;

    String selectedClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);

        selectedClub = getIntent().getStringExtra("club");

        gridLayout=(GridLayout)findViewById(R.id.play_round_grid);

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

            }

            if (yPos == INDEX_TOTAL_SCORE) {
                holeScores[yPos].setText("18");
                holeScores[yPos].setBackgroundColor(Color.argb(255, 255, 255, 0));
            }

            if (yPos == INDEX_SAVE_BUTTON) {
                holeScores[yPos].setText("\u2714");
                holeScores[yPos].setTextColor(Color.argb(255, 0, 255, 0));
                holeScores[yPos].setBackgroundColor(Color.argb(255, 0, 0, 0));

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
}
