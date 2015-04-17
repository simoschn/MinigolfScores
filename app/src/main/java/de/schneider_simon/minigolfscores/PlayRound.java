package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class PlayRound extends ActionBarActivity {

    private static final String TAG = "PlayRound";

    private static final Integer NUMBER_OF_HOLE_NAMES_COLUMNS = 2;
    private static final Integer RESERVED_COLUMNS = 1 + NUMBER_OF_HOLE_NAMES_COLUMNS;

    PlayRoundViews playRoundViews;
    PlayRoundContent playRoundContent;

    GridLayout gridLayout;

    String selectedClub;

    private static Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_round);

        bundle = savedInstanceState;

        selectedClub = getIntent().getStringExtra("club");

        gridLayout=(GridLayout)findViewById(R.id.play_round_grid);

        playRoundViews = new PlayRoundViews(gridLayout, selectedClub, this);
        playRoundContent = new PlayRoundContent(gridLayout.getColumnCount(), gridLayout.getRowCount(), RESERVED_COLUMNS);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        Log.d(TAG, "onWindowFocusChanged");

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            setTitle(selectedClub);

            if(bundle == null)
                playRoundContent.init();
            else
                playRoundContent.setContentFromBundle(bundle);

            playRoundViews.writeContentToViews(playRoundContent);
            playRoundViews.putViewsIntoGridlayout();
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

        Log.d(TAG, "onSaveInstanceState");

        playRoundContent.putContentToBundle(savedInstanceState);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.d(TAG, "onRestoreInstanceState");

        super.onRestoreInstanceState(savedInstanceState);

        playRoundContent.setContentFromBundle(savedInstanceState);
    }

}
