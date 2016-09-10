package edu.upenn.cis350.hwk2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.ui.AboutActivity;
import edu.upenn.cis350.hwk2.ui.ClassesActivity;
import edu.upenn.cis350.hwk2.ui.CompareSoundsActivity;
import edu.upenn.cis350.hwk2.ui.HearSoundsActivity;
import edu.upenn.cis350.hwk2.ui.HearWordsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KashayaPlayer.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void hearWordsOnClick(View v) {
        Intent hwIntent = new Intent(this, HearWordsActivity.class);
        startActivity(hwIntent);
    }

    public void compareSoundsOnClick(View v) {
        Intent csIntent = new Intent(this, CompareSoundsActivity.class);
        startActivity(csIntent);
    }

    public void hearSoundsOnClick(View v) {
        Intent hsIntent = new Intent(this, HearSoundsActivity.class);
        startActivity(hsIntent);
    }

    public void classOfSoundsOnClick(View v) {
        Intent csIntent = new Intent(this, ClassesActivity.class);
        startActivity(csIntent);
    }

    public void aboutOnClick(View v) {
        Intent abIntent = new Intent(this, AboutActivity.class);
        startActivity(abIntent);
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
        if (id == R.id.action_update) {

            // if update clicked
            KashayaPlayer.getResourceLoader().downloadRequiredFiles();
            return true;
        } else if (id == R.id.action_quit) {

            // quit app
            finishAffinity();
            KashayaPlayer.getPlayerInstance().dispose();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
