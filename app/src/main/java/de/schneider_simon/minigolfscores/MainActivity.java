package de.schneider_simon.minigolfscores;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "Main";

    static SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newCourseButton = (Button) findViewById(R.id.new_course_button);
        newCourseButton.setOnClickListener(new MyOnClickListener());

        TextView test = (TextView) findViewById(R.id.test_textView);

        CourseDBHelper dbHelper = new CourseDBHelper(getApplicationContext());

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("Courses", new String[] {"club"}, null, null, null, null, null);

        cursor.moveToFirst();
        cursor.moveToNext();

        test.setText(cursor.getString(0));


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

    class MyOnClickListener implements OnClickListener {

        public void onClick(View view) {

            Intent createNewCourse = new Intent(MainActivity.this, CreateCourse.class);
            startActivity(createNewCourse);
        }

    }
}