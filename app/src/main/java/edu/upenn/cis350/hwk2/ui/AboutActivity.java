package edu.upenn.cis350.hwk2.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.util.Linkify;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import edu.upenn.cis350.hwk2.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About");
        setSupportActionBar(toolbar);

        TextView aboutTextView1 = (TextView) findViewById(R.id.about_text_view1);
        aboutTextView1.setText(R.string.about_text1);

        aboutTextView1.setPadding(25, 25, 25, 25);

        TextView aboutTextView2 = (TextView) findViewById(R.id.about_text_view2);
        aboutTextView2.setText(R.string.about_text2);

        aboutTextView2.setPadding(25, 25, 25, 25);

        aboutTextView1.setMovementMethod(LinkMovementMethod.getInstance());
        aboutTextView2.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView iv = (ImageView) findViewById(R.id.neh_image);
        iv.setImageResource(R.drawable.neh);

        iv.setPadding(25, 25, 25, 25);
    }
}
