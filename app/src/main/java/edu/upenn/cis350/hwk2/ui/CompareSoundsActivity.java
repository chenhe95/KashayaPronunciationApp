package edu.upenn.cis350.hwk2.ui;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data.Consonant;
import edu.upenn.cis350.hwk2.data.Sound;
import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.ui.support.ActionButtonsActivity;
import edu.upenn.cis350.hwk2.ui.support.CompareSoundsConsonant;
import edu.upenn.cis350.hwk2.ui.support.CompareSoundsGlottal;
import edu.upenn.cis350.hwk2.ui.support.CompareSoundsVowel;
import edu.upenn.cis350.hwk2.ui.support.TabFragmentAdapter;

public class CompareSoundsActivity extends ActionButtonsActivity {

    @Override
    public boolean isRecordSelected() {
        return false;
    }

    @Override
    public boolean isSearchSelected() {
        return false;
    }

    @Override
    public boolean isClassSelected() {
        return false;
    }

    public static List<String> soundsSelected = new ArrayList<String>();

    public static AdapterView.OnItemClickListener comparatorListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!KashayaPlayer.isPlaying()) {
                TextView sound_clicked = (TextView) view.findViewById(R.id.sound_symbol);
                String name = sound_clicked.getText().toString().toLowerCase();

                // highlight selected sound
                if (!name.equals("")) {

                    // check whether it's a consonant
                    for (int i = 0; i < Consonant.CONSONANTS.length; i++) {
                        if (Consonant.CONSONANTS[i].getName().equals(name)) {
                            name = name + "a";
                        }
                    }

                    if (CompareSoundsActivity.soundsSelected.contains(name)) {
                        CompareSoundsActivity.soundsSelected.remove(name);
                        view.setBackgroundResource(R.color.colorOffWhite);
                    } else {
                        CompareSoundsActivity.soundsSelected.add(name);
                        view.setBackgroundResource(R.color.colorHighlight);
                    }
                }
            }
        }
    };

    public void setUpTabs(TabLayout tabLayout) {
        TabLayout.Tab vowelsTab = tabLayout.newTab();
        vowelsTab.setText("VOWELS");
        TabLayout.Tab consonantsTab = tabLayout.newTab();
        consonantsTab.setText("CONSONANTS");
        TabLayout.Tab glottalsTab = tabLayout.newTab();
        glottalsTab.setText("GLOTTALS");

        tabLayout.addTab(vowelsTab);
        tabLayout.addTab(consonantsTab);
        tabLayout.addTab(glottalsTab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    public ViewPager setUpTabListener(TabLayout tabLayout) {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                soundsSelected.clear();
                GridView gv = (GridView) findViewById(R.id.gridView_sound);
                int child = gv.getChildCount();
                for (int i = 0; i < child; i++) {
                    gv.getChildAt(i).setBackgroundResource(R.color.colorOffWhite);
                }

                if (tab.getText().equals("GLOTTALS")) {
                    Toast.makeText(CompareSoundsActivity.this, "Glottals currently have limited functionality",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                GridView gv = (GridView) findViewById(R.id.gridView_sound);
                int child = gv.getChildCount();
                for (int i = 0; i < child; i++) {
                    gv.getChildAt(i).setBackgroundResource(R.color.colorOffWhite);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return viewPager;
    }

    //sounds for list items
    ArrayList<Sound> sounds = new ArrayList<Sound>();

    //data of listview
    ArrayAdapter<Sound> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_sounds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Compare sounds");
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        setUpTabs(tabLayout);

        adapter = new ArrayAdapter<Sound>(this,
                android.R.layout.simple_list_item_1,
                sounds);

        ViewPager viewPager = setUpTabListener(tabLayout);

        final TabFragmentAdapter adapter = new TabFragmentAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        CompareSoundsVowel vwl = new CompareSoundsVowel();
                        return vwl;
                    case 1:
                        CompareSoundsConsonant csc = new CompareSoundsConsonant();
                        return csc;
                    case 2:
                        CompareSoundsGlottal csg = new CompareSoundsGlottal();
                        return csg;
                    default:
                        return null;
                }
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        ////////////////////////////////
        //                            //
        //   FLOATING ACTION BUTTON   //
        //                            //
        ////////////////////////////////

        // create an icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_play_arrow_white_24dp);

        // floating button
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        actionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));

        // sub buttons
        ImageView playOnce = new ImageView(this); // Create an icon
        playOnce.setImageResource(R.drawable.ic_replay_black_one_24dp);
        playOnce.setColorFilter(Color.parseColor("#737373"));
        ImageView playTwice = new ImageView(this); // Create an icon
        playTwice.setImageResource(R.drawable.ic_replay_black_two_24dp);
        playTwice.setColorFilter(Color.parseColor("#737373"));
        ImageView playThree = new ImageView(this); // Create an icon
        playThree.setImageResource(R.drawable.ic_replay_black_three_24dp);
        playThree.setColorFilter(Color.parseColor("#737373"));

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        FloatingActionButton.LayoutParams params = new FloatingActionButton.LayoutParams(125, 125);
        itemBuilder.setLayoutParams(params);

        SubActionButton buttonOne = itemBuilder.setContentView(playOnce).build();
        SubActionButton buttonTwo = itemBuilder.setContentView(playTwice).build();
        SubActionButton buttonThree = itemBuilder.setContentView(playThree).build();

        // add sub buttons to floating button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonOne)
                .addSubActionView(buttonTwo)
                .addSubActionView(buttonThree)
                .setRadius(200)
                .attachTo(actionButton)
                .build();

        // play once
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String sound : soundsSelected) {
                    KashayaPlayer.getPlayerInstance().playMedia(sound);
                }
            }
        });

        // play once
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String sound : soundsSelected) {
                    KashayaPlayer.getPlayerInstance().playMedia(sound);
                }
                for (String sound : soundsSelected) {
                    KashayaPlayer.getPlayerInstance().playMedia(sound);
                }
            }
        });

        // play once
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String sound : soundsSelected) {
                    KashayaPlayer.getPlayerInstance().playMedia(sound);
                }
                for (String sound : soundsSelected) {
                    KashayaPlayer.getPlayerInstance().playMedia(sound);
                }
                for (String sound : soundsSelected) {
                    KashayaPlayer.getPlayerInstance().playMedia(sound);
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        soundsSelected.clear();
    }

    public void addItems(View v) {
        adapter.notifyDataSetChanged();
    }

}
