package edu.upenn.cis350.hwk2.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data.Consonant;
import edu.upenn.cis350.hwk2.data.Sound;
import edu.upenn.cis350.hwk2.data.Vowel;
import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.ui.support.ClassesGridView;
import edu.upenn.cis350.hwk2.ui.support.GridViewAdapter;

public class ClassSelectedActivity extends AppCompatActivity {

    private KashayaPlayer player = null;
    private ClassesGridView itemGrid;
    private GridViewAdapter gridViewAdapter;
    private List<Sound> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get argument passed to this activity
        String classSelected = getIntent().getStringExtra("class_name");

        // set image
        String className = classSelected.toLowerCase();
        String imageName = "";

        for (int i = 0; i < className.length(); i++) {
            if (className.charAt(i) != ' ') {
                imageName += className.charAt(i);
            }
        }

        if (className.equals("sounds") || className.equals("length"))
            imageName = "vowels.png";
        else if (className.equals("labial"))
            imageName = "bilabial.gif";
        else
            imageName += ".gif";
        ImageView iv = (ImageView) findViewById(R.id.class_image);
        Bitmap class_image = KashayaPlayer.getResourceLoader().getImage(imageName);
        if (class_image != null)
            iv.setImageBitmap(class_image);
        else {
            iv.getLayoutParams().height = 5;
        }

        // set text
        TextView tv = (TextView) findViewById(R.id.class_name);
        String text = classSelected.substring(0, 1).toUpperCase() + classSelected.substring(1);
        if (className.equals("sounds") || className.equals("length"));
        else if (className.equals("glottals") || className.equals("stops") ||
                className.equals("consonants"))
            text = classSelected + " Class";
        else if (className.equals("palatal"))
            text = classSelected + " Affricates";
        else
            text = classSelected + " Stops";
        tv.setText(text);

        // set sounds
        player = KashayaPlayer.getPlayerInstance();
        itemGrid = (ClassesGridView) findViewById(R.id.gridView);
        String className_ = TextUtils.join("_", className.split(" "));
        System.out.print("class name: " + className_);
        if (className.equals("sounds")) {
            itemList = Vowel.getSoundsByForm("short");
            System.out.println(itemList);
        } else if (className.equals("length")) {
            itemList = Vowel.getSoundsByForm("short");
            itemList.addAll(Vowel.getSoundsByForm("long"));
        } else {
            itemList = Consonant.getSoundsByClass(className_);
            System.out.println(itemList);
        }

        if (itemList.isEmpty()) {
            itemList = Consonant.getSoundsByForm(className_);
        }

        // set grid
        if (itemList.size() == 2) {
            itemGrid.setNumColumns(2);
        } else if (itemList.size() % 5 == 0) {
            itemGrid.setNumColumns(5);
        }
        gridViewAdapter = new GridViewAdapter<Sound>(getApplicationContext(), itemList, "Class");
        itemGrid.setAdapter(gridViewAdapter);

        // set grid playing sound
        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                TextView vowel_clicked = (TextView) view.findViewById(R.id.sound_symbol);
                String name = vowel_clicked.getText().toString().toLowerCase();
                // if consonant, add "a" as suffix
                if (Consonant.contains(name))
                    name += "a";
                if (!KashayaPlayer.isPlaying()) {
                    player.playMedia(name);
                }
            }
        });
//        itemGrid.setOnItemClickListener(HearSoundsActivity.soundListener);

        // Description
        TextView description = (TextView) findViewById(R.id.description);
        String[] descriptions = getResources().getStringArray(R.array.descriptions);
        Map<String, String> nameToDescription = new HashMap<>();
        for (String descrip : descriptions) {
            String[] parts = descrip.split("\\|");
            nameToDescription.put(parts[0], parts[1]);
        }
        System.out.println(nameToDescription.get(className));
        description.setText(nameToDescription.get(className));

        if (className.equals("stops")) {
            setContentView(R.layout.activity_class_stop);
            TextView stopDescription = (TextView) findViewById(R.id.stops_description);
            stopDescription.setText(nameToDescription.get(className));
            stopDescription.setTextSize(15);
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_class_vowels_short, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
