package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PlayRound extends ActionBarActivity {

    private static final String TAG = "PlayRound";

    private static final Integer TITLE_HEIGHT = 330;
    private static final Integer SIDE_MARGIN = 80;
    private static final Integer MARGIN = 0;
    private static final Integer RESERVED_COLUMNS = 2;

    static SQLiteDatabase db = null;

    GridLayout gridLayout;
    TextView[] holeNames;
    Button[] holeScores;
    TextView[][] playedRounds;

    String selectedClub;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);

        selectedClub = getIntent().getStringExtra("club");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.play_round_relative_layout);
        Integer relativeLayoutWidth = relativeLayout.getWidth();
        Integer relativeLayoutHeight = relativeLayout.getHeight();
        Log.d(TAG, "relativeLayoutWidth onWindowFocusChanged: " + relativeLayoutWidth.toString());
        Log.d(TAG, "relativeLayoutHeight onWindowFocusChanged: " + relativeLayoutHeight.toString());

        gridLayout = (GridLayout) findViewById(R.id.play_round_grid);
        Integer numberOfColumns = gridLayout.getColumnCount();
        Integer numberOfRows = gridLayout.getRowCount();

        Integer gridWidth = gridLayout.getWidth();
        Integer gridHeight = gridLayout.getHeight();

        Log.d(TAG, "gridWidth: " + gridWidth.toString());
        Log.d(TAG, "gridHeight: " + gridHeight.toString());

        holeNames = new TextView[numberOfRows];
        holeScores = new Button[numberOfRows];
        playedRounds = new TextView[numberOfColumns - RESERVED_COLUMNS][numberOfRows];

        /*
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x - SIDE_MARGIN;
        int screenHeight = size.y - TITLE_HEIGHT;
        */

        int column = 0;

        Integer elementWidth = gridWidth/numberOfColumns;
        Integer elementHeight = gridHeight/numberOfRows;

        Log.d(TAG, "numberOfColumns: " + numberOfColumns.toString());
        Log.d(TAG, "numberOfRows: " + numberOfRows.toString());
        Log.d(TAG, "elementWidth: " + elementWidth.toString());
        Log.d(TAG, "elementHeight: " + elementHeight.toString());

        for(Integer yPos=0; yPos<numberOfRows; yPos++) {
            holeNames[yPos] = new TextView(this);
            Integer holeNumber = yPos + 1;

            if(yPos < 18)
                holeNames[yPos].setText("Bahn " + (holeNumber.toString()));

            GridLayout.Spec col = GridLayout.spec(column);
            GridLayout.Spec row = GridLayout.spec(yPos);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = elementWidth;
            params.height = elementHeight;
            params.setMargins(MARGIN,0,MARGIN,0);
            holeNames[yPos].setLayoutParams(params);

            gridLayout.addView(holeNames[yPos]);
        }

        column++;

        for(Integer yPos=0; yPos<numberOfRows; yPos++) {
            holeScores[yPos] = new Button(this, null, android.R.attr.buttonStyleSmall);
            holeScores[yPos].setTextSize(16);
            holeScores[yPos].setPadding(0,0,0,0);

            if(yPos < 18){
                holeScores[yPos].setText("1");
                holeScores[yPos].setBackgroundColor(Color.argb(128, 255, 255, 0));
                holeScores[yPos].setOnClickListener(new MyHoleScoreOnClickListener(yPos));
            }

            if(yPos == 18) {
                holeScores[yPos].setText("18");
                holeScores[yPos].setBackgroundColor(Color.argb(255, 255, 255, 0));
            }

            if(yPos == 19) {
                holeScores[yPos].setText("\u2714");
                holeScores[yPos].setTextColor(Color.argb(255, 0, 255, 0));
                holeScores[yPos].setBackgroundColor(Color.argb(255, 0, 0, 0));
                holeScores[yPos].setOnClickListener(new MySaveRoundOnClickListener());
            }

            GridLayout.Spec col = GridLayout.spec(column);
            GridLayout.Spec row = GridLayout.spec(yPos);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = elementWidth;
            params.height = elementHeight;
            params.setMargins(MARGIN,0,MARGIN,0);
            holeScores[yPos].setLayoutParams(params);

            gridLayout.addView(holeScores[yPos]);
        }

        column++;

        for (Integer xPos=0; xPos<numberOfColumns-RESERVED_COLUMNS; xPos++) {
            for(Integer yPos=0; yPos<numberOfRows; yPos++) {

                playedRounds[xPos][yPos] = new TextView(this);
                playedRounds[xPos][yPos].setGravity(Gravity.CENTER);
                playedRounds[xPos][yPos].setText("0");
                playedRounds[xPos][yPos].setBackgroundColor(Color.argb(255, 255, 255, 255));

                GridLayout.Spec col = GridLayout.spec(xPos + column);
                GridLayout.Spec row = GridLayout.spec(yPos);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
                params.width = elementWidth;
                params.height = elementHeight;
                params.setMargins(MARGIN,0,MARGIN,0);

                playedRounds[xPos][yPos].setLayoutParams(params);

                gridLayout.addView(playedRounds[xPos][yPos]);
            }
        }

        RoundsDBHelper dbHelper = new RoundsDBHelper(getApplicationContext());

        db = dbHelper.getWritableDatabase();

    }

/*    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        this.savedInstanceState = savedInstanceState;
        super.onPostCreate(savedInstanceState);


    }
*/

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

            if(currentScore < 7)
                currentScore++;
            else
                currentScore = 1;

            holeScores[clickedButton].setText(currentScore.toString());

            for(int i=0; i<18; i++)
                roundScore += Integer.parseInt(holeScores[i].getText().toString());

            holeScores[18].setText(roundScore.toString());

        }
    }

    private class MySaveRoundOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

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

            db.insert("Rounds", null, values);

            int playedRoundsIndex = 0;

            while((Integer.parseInt(playedRounds[playedRoundsIndex][0].getText().toString())) > 0)
                playedRoundsIndex++;

            for(int hole=0; hole<18; hole++) {
                playedRounds[playedRoundsIndex][hole].setText(holeScores[hole].getText());
                holeScores[hole].setText("1");
            }

            playedRounds[playedRoundsIndex][18].setText(holeScores[18].getText());
            holeScores[18].setText("18");
        }
    }
}
