package de.schneider_simon.minigolfscores;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    static Cursor cursorForClubSpinner;
    static Cursor cursorForClubDisplay;

    Spinner selectClubSpinner;

    String selectedClub = "";

    static SQLiteDatabase roundsDb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNewCourseButton();
        initPlayRoundButton();
        initStatsButton();
        initHoleNamesButton();

        initSelectClubSpinner();

        RoundsDBHelper dbHelper = new RoundsDBHelper(this.getApplicationContext());

        roundsDb = dbHelper.getReadableDatabase();
    }

    private void initSelectClubSpinner() {

        fillSpinnerFromCourseDb();

        selectClubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String buffer;

                displaySelectedCourseDetails();

                buffer = StatsStringMaker.lastTrainingAtSelectedClub(roundsDb, selectedClub);

                TextView lastTrainingText = (TextView)findViewById(R.id.text_view_last_training);

                lastTrainingText.setText(getString(R.string.last_training) + "\n" + buffer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initNewCourseButton() {
        Button newCourseButton = (Button) findViewById(R.id.new_course_button);
        newCourseButton.setOnClickListener(new MyNewCourseOnClickListener());
    }

    private void initPlayRoundButton() {
        Button playRoundButton = (Button) findViewById(R.id.play_round_button);
        playRoundButton.setOnClickListener(new MyPlayRoundOnClickListener());
    }

    private void initStatsButton() {
        Button statsButton = (Button) findViewById(R.id.stats_button);
        statsButton.setOnClickListener(new MyStatsOnClickListener());
    }

    private void initHoleNamesButton() {
        Button holeNamesButton = (Button) findViewById(R.id.holenames_button);
        holeNamesButton.setOnClickListener(new MyHoleNamesOnClickListener());
    }

    private void displaySelectedCourseDetails() {
        TextView viewSystem = (TextView) findViewById(R.id.text_view_system);
        TextView viewStreet = (TextView) findViewById(R.id.text_view_street);
        TextView viewStreetNumber = (TextView) findViewById(R.id.text_view_street_number);
        TextView viewZipcode = (TextView) findViewById(R.id.text_view_zipcode);
        TextView viewCity = (TextView) findViewById(R.id.text_view_city);

        initCursorForClubDisplay();

        TextView selectedView = (TextView) selectClubSpinner.getSelectedView();

        if(selectedView == null)
           return;

        selectedClub = selectedView.getText().toString();

        moveCursorToSelectedClub(selectedClub);

        viewSystem.setText(cursorForClubDisplay.getString(1));
        viewStreet.setText(cursorForClubDisplay.getString(2));
        viewStreetNumber.setText(cursorForClubDisplay.getString(3));
        viewZipcode.setText(cursorForClubDisplay.getString(4));
        viewCity.setText(cursorForClubDisplay.getString(5));

        cursorForClubDisplay.close();
    }

    private void moveCursorToSelectedClub(String selectedClub) {
        while(!(cursorForClubDisplay.getString(0)).equals(selectedClub))
            cursorForClubDisplay.moveToNext();
    }

    @Override
    protected void onDestroy() {

        cursorForClubSpinner.close();

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void fillSpinnerFromCourseDb() {

        selectClubSpinner = (Spinner) findViewById(R.id.select_course_spinner);

        String[] clubColumn = new String[] {"club"};

        int[] toSpinner = new int[] {android.R.id.text1};

        initCursorForClubSpinner();

        SimpleCursorAdapter clubAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                cursorForClubSpinner,
                clubColumn,
                toSpinner,
                0);

        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectClubSpinner.setAdapter(clubAdapter);

        selectClubSpinner.setSelection(0);

    }

    private void initCursorForClubSpinner() {

        SQLiteDatabase courseDb;

        CourseDBHelper courseDbHelper = new CourseDBHelper(getApplicationContext());

        courseDb = courseDbHelper.getReadableDatabase();

        cursorForClubSpinner = courseDb.query("Courses", new String[] {"_id", "club"}, null, null, null, null, null);

        cursorForClubSpinner.moveToFirst();

        courseDb.close();
    }

    private void initCursorForClubDisplay() {

        SQLiteDatabase courseDb;

        CourseDBHelper courseDbHelper = new CourseDBHelper(getApplicationContext());

        courseDb = courseDbHelper.getReadableDatabase();

        cursorForClubDisplay = courseDb.query("Courses", new String[] {"club", "system", "street", "street_number", "zipcode", "city"}, null, null, null, null, null);

        cursorForClubDisplay.moveToFirst();

        courseDb.close();
    }

    class MyNewCourseOnClickListener implements OnClickListener {

        public void onClick(View view) {

            Intent createNewCourse = new Intent(MainActivity.this, CreateCourse.class);
            startActivity(createNewCourse);
        }

    }

    class MyPlayRoundOnClickListener implements OnClickListener {

        public void onClick(View view) {

            Intent playRound = new Intent(MainActivity.this, PlayRound.class);
            playRound.putExtra("club", selectedClub);
            startActivity(playRound);
        }
    }

    class MyStatsOnClickListener implements OnClickListener {

        public void onClick(View view) {

            Intent statistics = new Intent(MainActivity.this, Statistics.class);
            statistics.putExtra("club", selectedClub);
            startActivity(statistics);
        }
    }

    class MyHoleNamesOnClickListener implements OnClickListener {

        public void onClick(View view) {

            Intent holeNames = new Intent(MainActivity.this, EnterHoleNames.class);
            holeNames.putExtra("club", selectedClub);
            startActivity(holeNames);
        }
    }
    private static SpannableStringBuilder separator() {
        SpannableStringBuilder sb = new SpannableStringBuilder("\n\n-------------------------------------------\n");

        sb.setSpan(new ForegroundColorSpan(Color.RED), 0, sb.length(), 0);

        return sb;
    }
}