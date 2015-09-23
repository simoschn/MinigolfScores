package de.schneider_simon.minigolfscores;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EnterHoleNames extends ActionBarActivity {

    String selectedClub;
    static SQLiteDatabase holeNamesDb = null;
    private EditText[] holeNamesEditTexts = new EditText[18];
    Cursor holeNamesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_hole_names);

        Button saveHoleNamesButton = (Button) findViewById(R.id.save_holenames_button);
        saveHoleNamesButton.setOnClickListener(new MyOnClickListener());

        setTitleToSelectedClub();

        initHoleNamesDb();
        holeNamesCursor = HoleNamesDB.setHoleNamesCursor(holeNamesDb, selectedClub);
        holeNamesCursor.moveToFirst();
        initHoleNamesEditTexts();
    }

    private void initHoleNamesEditTexts() {
        holeNamesEditTexts[0] = (EditText) findViewById(R.id.hole1_textView);
        holeNamesEditTexts[1] = (EditText) findViewById(R.id.hole2_textView);
        holeNamesEditTexts[2] = (EditText) findViewById(R.id.hole3_textView);
        holeNamesEditTexts[3] = (EditText) findViewById(R.id.hole4_textView);
        holeNamesEditTexts[4] = (EditText) findViewById(R.id.hole5_textView);
        holeNamesEditTexts[5] = (EditText) findViewById(R.id.hole6_textView);
        holeNamesEditTexts[6] = (EditText) findViewById(R.id.hole7_textView);
        holeNamesEditTexts[7] = (EditText) findViewById(R.id.hole8_textView);
        holeNamesEditTexts[8] = (EditText) findViewById(R.id.hole9_textView);
        holeNamesEditTexts[9] = (EditText) findViewById(R.id.hole10_textView);
        holeNamesEditTexts[10] = (EditText) findViewById(R.id.hole11_textView);
        holeNamesEditTexts[11] = (EditText) findViewById(R.id.hole12_textView);
        holeNamesEditTexts[12] = (EditText) findViewById(R.id.hole13_textView);
        holeNamesEditTexts[13] = (EditText) findViewById(R.id.hole14_textView);
        holeNamesEditTexts[14] = (EditText) findViewById(R.id.hole15_textView);
        holeNamesEditTexts[15] = (EditText) findViewById(R.id.hole16_textView);
        holeNamesEditTexts[16] = (EditText) findViewById(R.id.hole17_textView);
        holeNamesEditTexts[17] = (EditText) findViewById(R.id.hole18_textView);

        if(holeNamesCursor.getCount() > 0)
            initHoleNamesEditTextsContents();
    }

    private void initHoleNamesEditTextsContents() {
        for(int i=0; i<18; i++)
            holeNamesEditTexts[i].setText(holeNamesCursor.getString(i));
    }

    private void initHoleNamesDb() {
        HoleNamesDBHelper dbHelper = new HoleNamesDBHelper(getApplicationContext());
        holeNamesDb = dbHelper.getWritableDatabase();
    }

    private void setTitleToSelectedClub() {
        selectedClub = getIntent().getStringExtra("club");
        setTitle(selectedClub);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_hole_names, menu);
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

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ContentValues values = new ContentValues();

            values.put("club", selectedClub);
            values.put("holeName1", holeNamesEditTexts[0].getText().toString());
            values.put("holeName2", holeNamesEditTexts[1].getText().toString());
            values.put("holeName3", holeNamesEditTexts[2].getText().toString());
            values.put("holeName4", holeNamesEditTexts[3].getText().toString());
            values.put("holeName5", holeNamesEditTexts[4].getText().toString());
            values.put("holeName6", holeNamesEditTexts[5].getText().toString());
            values.put("holeName7", holeNamesEditTexts[6].getText().toString());
            values.put("holeName8", holeNamesEditTexts[7].getText().toString());
            values.put("holeName9", holeNamesEditTexts[8].getText().toString());
            values.put("holeName10", holeNamesEditTexts[9].getText().toString());
            values.put("holeName11", holeNamesEditTexts[10].getText().toString());
            values.put("holeName12", holeNamesEditTexts[11].getText().toString());
            values.put("holeName13", holeNamesEditTexts[12].getText().toString());
            values.put("holeName14", holeNamesEditTexts[13].getText().toString());
            values.put("holeName15", holeNamesEditTexts[14].getText().toString());
            values.put("holeName16", holeNamesEditTexts[15].getText().toString());
            values.put("holeName17", holeNamesEditTexts[16].getText().toString());
            values.put("holeName18", holeNamesEditTexts[17].getText().toString());

            Cursor clubCounter = holeNamesDb.query("HoleNames", new String[]{"club"}, "club='" + selectedClub + "'", null, null, null, null, null);

            if(clubCounter.getCount() == 0)
                holeNamesDb.insert("HoleNames", null, values);
            else
                holeNamesDb.update("HoleNames", values, "club='"+ selectedClub +"'", null);

            confirmHoleNamesSave();
        }
    }

    private void confirmHoleNamesSave(){
        Toast toast = Toast.makeText(this.getApplicationContext(), getString(R.string.save_hole_names_confirm), Toast.LENGTH_SHORT);
        toast.show();
    }
}
