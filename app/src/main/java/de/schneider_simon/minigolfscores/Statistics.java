package de.schneider_simon.minigolfscores;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import static de.schneider_simon.minigolfscores.StatsStringMaker.*;


public class Statistics extends ActionBarActivity {

    private static final String TAG = "Statistics";

    String selectedClub;
    Spinner selectStatsSpinner;

    static SQLiteDatabase roundsDb = null;
    static SQLiteDatabase holeNamesDb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        selectedClub = getIntent().getStringExtra("club");
        setTitle(R.string.title_activity_statistics);

        initSelectStatsSpinner();

        RoundsDBHelper roundsDbHelper = new RoundsDBHelper(this.getApplicationContext());
        roundsDb = roundsDbHelper.getReadableDatabase();

        HoleNamesDBHelper holeNamesDbHelper = new HoleNamesDBHelper(this.getApplicationContext());
        holeNamesDb = holeNamesDbHelper.getReadableDatabase();
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

                SpannableStringBuilder buffer = new SpannableStringBuilder("");

                switch(position){
                    case 0:
                        buffer.append(makeHeaderString());
                        buffer.append(allRounds(roundsDb, selectedClub));
                        break;
                    case 1:
                        buffer.append(makeHeaderString());
                        buffer.append(allRoundsDetail(roundsDb, holeNamesDb, selectedClub));
                        break;
                    case 2:
                        buffer.append(averageAndAcePercentagePerHole(roundsDb, holeNamesDb, selectedClub, false));
                        break;
                    case 3:
                        buffer.append(averageAndAcePercentagePerHole(roundsDb, holeNamesDb, selectedClub, true));
                        break;
                }
                writeBufferToStatsTextView(buffer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void writeBufferToStatsTextView(SpannableStringBuilder buffer) {
        TextView statsTextView = (TextView)findViewById(R.id.stats_text_view);
        statsTextView.setText(buffer);
    }

    private SpannableString makeHeaderString() {
        return new SpannableString(getString(R.string.all_rounds)
                                + " "
                                + selectedClub + "\n"
                                + getString(R.string.rounds_total)
                                + " "
                                + totalNumberOfRounds(roundsDb, selectedClub) + "\n"
                                + getString(R.string.average_total)
                                + " "
                                + roundsAverage(roundsDb, selectedClub) + "\n\n");
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
