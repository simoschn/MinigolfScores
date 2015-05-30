package de.schneider_simon.minigolfscores;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_round);

        bundle = savedInstanceState;

        selectedClub = getIntent().getStringExtra("club");

        gridLayout=(GridLayout)findViewById(R.id.play_round_grid);

        playRoundViews = new PlayRoundViews(gridLayout, selectedClub, this);
        playRoundContent = new PlayRoundContent(gridLayout.getColumnCount(), gridLayout.getRowCount(), RESERVED_COLUMNS);

        if(bundle == null)
            playRoundContent.init();
        else
            playRoundContent.setContentFromBundle(bundle);

        isPutViewsDone = false;
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
