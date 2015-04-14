package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateCourse extends ActionBarActivity {

    static SQLiteDatabase db = null;

    private static final String TAG = "CreateCourse";

    private Course course = new Course();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new MyOnClickListener());

        initEditTextClub();
        initEditTextSystem();
        initEditTextStreet();
        initEditTextStreetNumber();
        initEditTextZipcode();
        initEditTextCity();

        CourseDBHelper dbHelper = new CourseDBHelper(getApplicationContext());

        db = dbHelper.getWritableDatabase();

    }

    private void initEditTextCity() {
        EditText editTextCity = (EditText) findViewById(R.id.editText_city);
        TextWatcher watcherCity = getTextWatcherCity();
        editTextCity.addTextChangedListener(watcherCity);
    }

    private TextWatcher getTextWatcherCity() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    course.setCity(s.toString());

                    LogCourse();
                }
            };
    }

    private void initEditTextZipcode() {
        EditText editTextZipCode = (EditText) findViewById(R.id.editText_zipcode);
        TextWatcher watcherZipcode = getTextWatcherZipcode();
        editTextZipCode.addTextChangedListener(watcherZipcode);
    }

    private TextWatcher getTextWatcherZipcode() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    course.setZipcode(s.toString());

                    LogCourse();
                }
            };
    }

    private void initEditTextStreetNumber() {
        EditText editTextStreetNumber = (EditText) findViewById(R.id.editText_streetNumber);
        TextWatcher watcherStreetNumber = getTextWatcherStreetNumber();
        editTextStreetNumber.addTextChangedListener(watcherStreetNumber);
    }

    private TextWatcher getTextWatcherStreetNumber() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    course.setStreetNumber(s.toString());

                    LogCourse();
                }
            };
    }

    private void initEditTextStreet() {
        EditText editTextStreet = (EditText) findViewById(R.id.editText_street);
        TextWatcher watcherStreet = getTextWatcherStreet();
        editTextStreet.addTextChangedListener(watcherStreet);
    }

    private TextWatcher getTextWatcherStreet() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    course.setStreet(s.toString());

                    LogCourse();
                }
            };
    }

    private void initEditTextSystem() {
        EditText editTextSystem = (EditText) findViewById(R.id.editText_system);
        TextWatcher watcherSystem = getTextWatcherSystem();
        editTextSystem.addTextChangedListener(watcherSystem);
    }

    private TextWatcher getTextWatcherSystem() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    course.setSystem(s.toString());

                    LogCourse();
                }
            };
    }

    private void initEditTextClub() {
        EditText editTextClub = (EditText) findViewById(R.id.editText_club);
        TextWatcher watcherClub = getTextWatcherClub();
        editTextClub.addTextChangedListener(watcherClub);
    }

    private TextWatcher getTextWatcherClub() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {

                    course.setClub(s.toString());

                    LogCourse();
                }
            };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_course, menu);
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

    private void LogCourse() {
        Log.d(TAG, course.getClub());
        Log.d(TAG, course.getSystem());
        Log.d(TAG, course.getStreet());
        Log.d(TAG, course.getStreetNumber());
        Log.d(TAG, course.getZipcode());
        Log.d(TAG, course.getCity());
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ContentValues values = new ContentValues();

            values.put("club", course.getClub());
            values.put("system", course.getSystem());
            values.put("street", course.getStreet());
            values.put("street_number", course.getStreetNumber());
            values.put("zipcode", course.getZipcode());
            values.put("city", course.getCity());

            Cursor clubCounter = db.query("Courses", new String[]{"club"}, "club='" + course.getClub() + "'", null, null, null, null, null);

            if(clubCounter.getCount() == 0)
                db.insert("Courses", null, values);
            else
                db.update("Courses", values, "club='"+course.getClub()+"'", null);

        }
    }
}
