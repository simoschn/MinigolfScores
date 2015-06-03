package de.schneider_simon.minigolfscores;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;

public class PlayRound extends ActionBarActivity {

    private static final String TAG = "PlayRound";

    private static final Integer NUMBER_OF_HOLE_NAMES_COLUMNS = 2;
    private static final Integer RESERVED_COLUMNS = NUMBER_OF_HOLE_NAMES_COLUMNS + 1;

    PlayRoundViews playRoundViews;
    PlayRoundContent playRoundContent;

    GridLayout gridLayout;

    String selectedClub;

    private static Bundle bundle;

    private static boolean isPutViewsDone;

    static SQLiteDatabase holeNamesDb = null;

    Cursor holeNamesCursor;

    String[] holeNames = new String[18];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_round);

        bundle = savedInstanceState;

        selectedClub = getIntent().getStringExtra("club");

        gridLayout=(GridLayout)findViewById(R.id.play_round_grid);

        HoleNamesDBHelper dbHelper = new HoleNamesDBHelper(this.getApplicationContext());

        holeNamesDb = dbHelper.getReadableDatabase();

        holeNamesCursor = setHoleNamesCursor(holeNamesDb, selectedClub);

        holeNamesCursor.moveToFirst();

        if(holeNamesCursor.getCount() > 0){
            for(int i=0; i<18; i++)
                holeNames[i] = holeNamesCursor.getString(i);
            playRoundContent = new PlayRoundContent(gridLayout.getColumnCount(), gridLayout.getRowCount(), RESERVED_COLUMNS, holeNames);
        }
        else
            playRoundContent = new PlayRoundContent(gridLayout.getColumnCount(), gridLayout.getRowCount(), RESERVED_COLUMNS);

        playRoundViews = new PlayRoundViews(gridLayout, selectedClub, this);


        if(bundle == null)
            playRoundContent.init();
        else
            playRoundContent.setContentFromBundle(bundle);

        isPutViewsDone = false;
    }

    private static Cursor setHoleNamesCursor(SQLiteDatabase holeNamesDb, String selectedClub) {
        return holeNamesDb.query("HoleNames", new String[]{
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
        }, "club='" + selectedClub + "'", null, null, null, null, null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            setTitle(selectedClub);

            if(!isPutViewsDone){
                playRoundViews.putViewsIntoGridlayout();
                playRoundViews.writeContentToViews(playRoundContent);
                isPutViewsDone = true;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        playRoundViews.fillContentFromViews(playRoundContent);
        playRoundContent.putContentToBundle(savedInstanceState);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        playRoundContent.setContentFromBundle(savedInstanceState);

    }

    @Override
    public void onPause(){

        playRoundViews.fillContentFromViews(playRoundContent);

        super.onPause();
    }

    @Override
    public void onResume(){

        if(isPutViewsDone)
            playRoundViews.writeContentToViews(playRoundContent);

        super.onResume();
    }
}
