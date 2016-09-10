package edu.upenn.cis350.hwk2.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import edu.upenn.cis350.hwk2.R;

public class ClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sound Classes");
        setSupportActionBar(toolbar);

    }

    public void onButtonClick(View view) {

        // create new intent
        Intent i = new Intent(this, ClassSelectedActivity.class);

        // get text
        Button b = (Button) view;
        String name = b.getText().toString();

        // send key-value pair
        i.putExtra("class_name", name);

        // start activity
        startActivity(i);
    }
}
