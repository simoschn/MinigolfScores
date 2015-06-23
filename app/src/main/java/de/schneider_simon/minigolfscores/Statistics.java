package de.schneider_simon.minigolfscores;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class Statistics extends ActionBarActivity {

    String selectedClub;
    Spinner selectStatsSpinner;

    static SQLiteDatabase roundsDb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        selectedClub = getIntent().getStringExtra("club");
        setTitle(R.string.title_activity_statistics);

        initSelectStatsSpinner();

        RoundsDBHelper dbHelper = new RoundsDBHelper(this.getApplicationContext());

        roundsDb = dbHelper.getReadableDatabase();
    }

    private void initSelectStatsSpinner() {
        selectStatsSpinner = (Spinner) findViewById(R.id.select_stats_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.select_stats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStatsSpinner.setAdapter(adapter);

        selectStatsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayAllRoundsAtSelectedClub();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void displayAllRoundsAtSelectedClub() {
        String buffer ="";
        buffer += getString(R.string.all_rounds) + " " + selectedClub + "\n";

        buffer += getString(R.string.rounds_total) + " " + StatsStringMaker.totalNumberOfRoundsAtSelectedClub(roundsDb, selectedClub) + "\n";
        buffer += getString(R.string.average_total) + " " + StatsStringMaker.roundsAverageAtSelectedClub(roundsDb, selectedClub) + "\n\n";

        buffer += StatsStringMaker.allRoundsAtSelectedClub(roundsDb, selectedClub);

        TextView statsTextView = (TextView)findViewById(R.id.stats_text_view);
        statsTextView.setText(buffer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
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
