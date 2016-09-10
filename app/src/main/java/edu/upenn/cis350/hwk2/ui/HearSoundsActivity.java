package edu.upenn.cis350.hwk2.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams;

import edu.upenn.cis350.hwk2.ui.support.ActionButtonsActivity;
import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.ui.support.TabFragmentAdapter;

public class HearSoundsActivity extends ActionButtonsActivity {

    // private fields
    private volatile String selectedSymbol;

    private volatile boolean recordSelected = false;
    private volatile boolean searchSelected = false;
    private volatile boolean classSelected = false;

    // create search sub button
    private ImageView searchIcon = null; // Create an icon
    // create info sub button
    private ImageView infoIcon = null; // Create an icon
    // create compare sub button
    private ImageView compareIcon = null; // Create an icon
    // build up sub buttons
    private SubActionButton.Builder itemBuilder = null;
    private SubActionButton searchButton = null;
    private SubActionButton infoButton = null;
    private SubActionButton compareButton = null;

    @Override
    public boolean isRecordSelected() {
        return recordSelected;
    }

    @Override
    public boolean isSearchSelected() {
        return searchSelected;
    }

    @Override
    public boolean isClassSelected() {
        return classSelected;
    }

    //set up tabs for page
    private void setUpTabs() {
        // Add three tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
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

        // Set up view for each tab
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabFragmentAdapter adapter = new TabFragmentAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getText().equals("GLOTTALS")) {
                    Toast.makeText(HearSoundsActivity.this, "Glottals currently have limited functionality",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //set up compare button event listener
    private void setUpCompareButton() {
        // toggle compare
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSelected = !recordSelected;
                searchSelected = false;
                classSelected = false;
                if (recordSelected) {
                    // highlight compare
                    compareIcon.setColorFilter(Color.parseColor("#FF9800"));
                    // turn off rest
                    searchIcon.setColorFilter(Color.parseColor("#737373"));
                    infoIcon.setColorFilter(Color.parseColor("#737373"));
                } else {
                    compareIcon.setColorFilter(Color.parseColor("#737373"));
                }
            }
        });
    }

    //set up search button event listener
    private void setUpSearchButton() {

        // toggle search
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSelected = false;
                searchSelected = !searchSelected;
                classSelected = false;
                if (searchSelected) {
                    // highlight search
                    searchIcon.setColorFilter(Color.parseColor("#FF9800"));
                    // turn off rest
                    compareIcon.setColorFilter(Color.parseColor("#737373"));
                    infoIcon.setColorFilter(Color.parseColor("#737373"));
                } else {
                    searchIcon.setColorFilter(Color.parseColor("#737373"));
                }
            }
        });
    }

    //set up info button event listener
    private void setUpInfoButton() {
        // toggle class
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSelected = false;
                searchSelected = false;
                classSelected = !classSelected;
                if (classSelected) {
                    // highlight info
                    infoIcon.setColorFilter(Color.parseColor("#FF9800"));
                    // turn off rest
                    compareIcon.setColorFilter(Color.parseColor("#737373"));
                    searchIcon.setColorFilter(Color.parseColor("#737373"));
                } else {
                    infoIcon.setColorFilter(Color.parseColor("#737373"));
                }
            }
        });
    }

    private void initializeElements() {
        searchIcon = new ImageView(this);
        infoIcon = new ImageView(this);
        compareIcon = new ImageView(this);


        itemBuilder = new SubActionButton.Builder(this);

        searchButton = itemBuilder.setContentView(searchIcon).build();
        infoButton = itemBuilder.setContentView(infoIcon).build();
        compareButton = itemBuilder.setContentView(compareIcon).build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear_sounds);

        initializeElements();

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Listen to sounds");
        setSupportActionBar(toolbar);

        setUpTabs();

        // create an icon
        final ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_add_white_24dp);

        // floating button
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        actionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));

        searchIcon.setImageResource(R.drawable.ic_search_black_24dp);
        searchIcon.setColorFilter(Color.parseColor("#737373"));

        infoIcon.setImageResource(R.drawable.ic_info_outline_black_24dp);
        infoIcon.setColorFilter(Color.parseColor("#737373"));

        compareIcon.setImageResource(R.drawable.ic_mic_black_24dp);
        compareIcon.setColorFilter(Color.parseColor("#737373"));

        LayoutParams params = new LayoutParams(125, 125);
        itemBuilder.setLayoutParams(params);

        setUpCompareButton();
        setUpSearchButton();
        setUpInfoButton();

        // add sub buttons to floating button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(infoButton)
                .addSubActionView(searchButton)
                .addSubActionView(compareButton)
                .setRadius(200)
                .attachTo(actionButton)
                .build();

    }
}