package de.schneider_simon.minigolfscores;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class CreateCourse extends ActionBarActivity {

    static SQLiteDatabase db = null;

    private static final String TAG = "CreateCourseTest1234";

    private Course course = new Course();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        TextWatcher watcherClub = new TextWatcher() {
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

        TextWatcher watcherSystem = new TextWatcher() {
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

        TextWatcher watcherStreet = new TextWatcher() {
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

        TextWatcher watcherStreetNumber = new TextWatcher() {
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

        TextWatcher watcherZipcode = new TextWatcher() {
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

        TextWatcher watcherCity = new TextWatcher() {
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

        EditText editTextClub = (EditText) findViewById(R.id.editText_club);
        EditText editTextSystem = (EditText) findViewById(R.id.editText_system);
        EditText editTextStreet = (EditText) findViewById(R.id.editText_street);
        EditText editTextStreetNumber = (EditText) findViewById(R.id.editText_streetNumber);
        EditText editTextZipCode = (EditText) findViewById(R.id.editText_zipcode);
        EditText editTextCity = (EditText) findViewById(R.id.editText_city);

        editTextClub.addTextChangedListener(watcherClub);
        editTextSystem.addTextChangedListener(watcherSystem);
        editTextStreet.addTextChangedListener(watcherStreet);
        editTextStreetNumber.addTextChangedListener(watcherStreetNumber);
        editTextZipCode.addTextChangedListener(watcherZipcode);
        editTextCity.addTextChangedListener(watcherCity);
    }

    private void LogCourse() {
        Log.d(TAG, course.getClub());
        Log.d(TAG, course.getSystem());
        Log.d(TAG, course.getStreet());
        Log.d(TAG, course.getStreetNumber());
        Log.d(TAG, course.getZipcode());
        Log.d(TAG, course.getCity());
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

}
