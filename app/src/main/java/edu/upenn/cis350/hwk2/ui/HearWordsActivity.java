package edu.upenn.cis350.hwk2.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.ui.graphics.DrawableText;

public class HearWordsActivity extends ListActivity {

    public static final String INTENT_ID_SEARCH_TERM = "search_term";

    private FloatingActionButton[] letterAddButtons = null;
    private boolean letterButtonVisibility = false;
    private volatile boolean compareSelected = false;

    private List<Map<String, String>> results = new ArrayList<>();
    private SimpleAdapter adapter = null;

    private String searchTerm = "";


    @Override
    public void onNewIntent(Intent intent) {
        if (intent != null) {
            searchTerm = intent.getStringExtra(INTENT_ID_SEARCH_TERM);
        }
        super.onNewIntent(intent);
    }

    public boolean isCompareSelected() {
        return compareSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear_words);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Listen to words");

        setUpSearchBar();

        setUpSpecialCharacterButtons();

        if (searchTerm != null) {
            appendTextEditText(searchTerm);
        }

        setUpFloatingActionButtons();

    }

    /**
     * Sets up the search bar
     */
    private void setUpSearchBar() {
        final EditText queryText = (EditText) findViewById(R.id.search_query_text);
        adapter = new SimpleAdapter(this, results,
                android.R.layout.simple_list_item_2,
                new String[]{"kashaya", "definition"},
                new int[]{android.R.id.text1, android.R.id.text2});
        setListAdapter(adapter);

        // set up checkboxes
        final CheckBox kashayaCheck = (CheckBox) findViewById(R.id.kashayaCheck);
        final CheckBox englishCheck = (CheckBox) findViewById(R.id.englishCheck);
        kashayaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("kashaya changed");
                search(queryText.getText().toString(),
                        kashayaCheck.isChecked(), englishCheck.isChecked());
                adapter.notifyDataSetChanged();
            }
        });
        englishCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("english changed");
                search(queryText.getText().toString(),
                        kashayaCheck.isChecked(), englishCheck.isChecked());
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            searchTerm = intent.getStringExtra("search_term");
        }

        // live search
        queryText.addTextChangedListener(new TextWatcher() {
                                             String onTextChanged;
                                             String beforeTextChanged;

                                             @Override
                                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                 beforeTextChanged = queryText.getText().toString();
                                             }

                                             @Override
                                             public void onTextChanged(CharSequence s, int start, int before, int count) {
                                             }

                                             @Override
                                             public void afterTextChanged(Editable s) {
                                                 // get the newly added character
                                                 onTextChanged = s.toString();
                                                 int diffIndex = indexOfDifference(beforeTextChanged, onTextChanged);

                                                 // weird single quote
                                                 if (onTextChanged.length() > beforeTextChanged.length() &&
                                                         onTextChanged.charAt(diffIndex) == '\'') {
                                                     // split string into two
                                                     String prefix = s.toString().substring(0, diffIndex);
                                                     String suffix = s.toString().substring(diffIndex + 1, onTextChanged.length());

                                                     // set new string
                                                     onTextChanged = prefix + "’" + suffix;
                                                     System.out.println("new text: " + onTextChanged);
                                                     queryText.setText(onTextChanged);
                                                     queryText.setSelection(diffIndex + 1);
                                                 }

//                                                 HearWordsActivity.this.clearList();

//                                                 if (!s.toString().equals("")) { // if the user is searching
                                                     search(onTextChanged,
                                                             kashayaCheck.isChecked(),
                                                             englishCheck.isChecked());
//                                                 }
                                             }
                                         }

        );

        // Set up list view for the return list
        final ListView listView = ((ListView) findViewById(android.R.id.list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                if (position >= 0) {
                    String kashaya = ((TextView) (view.findViewById(android.R.id.text1))).getText().toString();
                    String english = ((TextView) (view.findViewById(android.R.id.text2))).getText().toString();

                    String item = english + " - " + kashaya;
                    //listView.setSelection(position);
                    if (!isCompareSelected()) {
                        KashayaPlayer.getPlayerInstance().playMedia(item);
                    } else {
                        Intent i = new Intent(view.getContext(),
                                CompareSingleSoundActivity.class);
                        i.putExtra("word", item);
                        view.getContext().startActivity(i);
                    }
                }
            }
        });
    }

    /**
     * Set up the floating action buttons at the bottom right
     *
     */
    private void setUpFloatingActionButtons() {
        // create an icon
        final ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_mic_off_white_24dp);

        // floating button
        final com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        actionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));

        // toggle compare
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compareSelected) {
                    compareSelected = false;
                    actionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));
                    icon.setImageResource(R.drawable.ic_mic_off_white_24dp);
                } else {
                    compareSelected = true;
                    actionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    icon.setImageResource(R.drawable.ic_mic_white_24dp);
                }
            }
        });
    }

    /**
     * Sets up the special character buttons which insert special characters into the search bar
     */
    private void setUpSpecialCharacterButtons() {

        // H Apostrophe
        FloatingActionButton thatHApostropheThing = (FloatingActionButton) findViewById(R.id.that_h_apostrophe_thing);
        thatHApostropheThing.setImageDrawable(new DrawableText(thatHApostropheThing
                .getContext(), "ʰ", ColorStateList.valueOf(Color.WHITE), 45.f));

        // Hat on s
        FloatingActionButton thatHatThing = (FloatingActionButton) findViewById(R.id.that_hat_thing);
        thatHatThing.setImageDrawable(new DrawableText(thatHatThing
                .getContext(), "š", ColorStateList.valueOf(Color.WHITE), 45.f));

        // Dot under t
        FloatingActionButton underDotThing = (FloatingActionButton) findViewById(R.id.under_dot_thing);
        underDotThing.setImageDrawable(new DrawableText(underDotThing
                .getContext(), "ṭ", ColorStateList.valueOf(Color.WHITE), 45.f));

        // Apostrophe
        FloatingActionButton strangeApostrope = (FloatingActionButton) findViewById(R.id.strange_apostrophe);
        strangeApostrope.setImageDrawable(new DrawableText(strangeApostrope
                .getContext(), "́", ColorStateList.valueOf(Color.WHITE), 45.f));

        // Middle dot
        FloatingActionButton middleDotThing = (FloatingActionButton) findViewById(R.id.middle_dot_thing);
        middleDotThing.setImageDrawable(new DrawableText(middleDotThing
                .getContext(), "·", ColorStateList.valueOf(Color.WHITE), 45.f));

        // Glottal
        FloatingActionButton whateverThatQuestionMarkThingIs = (FloatingActionButton) findViewById(R.id.whatever_that_question_mark_thing_is);
        whateverThatQuestionMarkThingIs.setImageDrawable(new DrawableText(whateverThatQuestionMarkThingIs
                .getContext(), "ʔ", ColorStateList.valueOf(Color.WHITE), 45.f));

        // Set up buttons for the special characters
        letterAddButtons = new FloatingActionButton[]{
                middleDotThing, underDotThing, thatHApostropheThing, thatHatThing, whateverThatQuestionMarkThingIs, strangeApostrope
        };
        setLetterButtons(true);
    }

    /**
     * to set up the special characters button
     *
     * @param visibility true=visible, false=invisible
     */
    private void setLetterButtons(boolean visibility) {
        letterButtonVisibility = visibility;
        if (visibility) {
            for (FloatingActionButton fab : letterAddButtons) {
                fab.setVisibility(View.VISIBLE);
            }
        } else {
            for (FloatingActionButton fab : letterAddButtons) {
                fab.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * run search function
     *
     * @param query the search term
     * @param kashayaChecked whether the Kashaya checkbox is checked
     * @param englishChecked whether the English checkbox is checked
     */
    private void search(String query, boolean kashayaChecked, boolean englishChecked) {
        results.clear();
        for (Map<String, String> entry : searchResults(query, kashayaChecked, englishChecked)) {
            for (Map.Entry<String, String> word : entry.entrySet()) {
                HearWordsActivity.this.addPair(word.getKey(), word.getValue());
            }
        }
    }

    /**
     * get list of results based on the search terms
     *
     * @param query
     */
    private List<Map<String, String>> searchResults(String query,
                                                    boolean kashayaChecked,
                                                    boolean englishChecked ) {
        if (query != null) {
            query = query.toLowerCase();
        }

        List<Map<String, String>> results = new ArrayList<>();

        String delimeter = " - ";
        String[] words = null;

        for (String s : KashayaPlayer.getPlayerInstance().getResourceLoader().getWordMap()
                .keySet()) {
            String[] tokens = s.replaceAll("\\s", "").split("-");
            try {
                if (query != null && s != null && ((kashayaChecked && tokens[1].contains(query)) ||
                        (englishChecked && tokens[0].contains(query)))) {
                    words = s.split(delimeter);

                    Map<String, String> result = new HashMap<>();
                    result.put(words[1], words[0]);
                    results.add(result);
                }
            } catch (Exception e) {
                System.out.println("incorrectly formatted source file word " + s);
                e.printStackTrace();
            }
        }

        return results;
    }

    /**
     * for adding special characters in the middle of the string
     *
     * @param s
     */
    private void appendTextEditText(String s) {
        // get text
        EditText et = (EditText) findViewById(R.id.search_query_text);
        // get cursor position
        int pos = et.getSelectionStart();

        // split string into two
        String prefix = et.getText().toString().substring(0, pos);
        String suffix = et.getText().toString().substring(pos, et.getText().toString().length());

        // insert new string
        String newText = prefix + s + suffix;

        // set new text
        et.setText(newText);
        // set cursor position to after
        et.setSelection(pos + s.length());
    }

    /**
     * add a pair of word and definition into the list of results
     *
     * @param str kashaya word
     * @param def definition of the kashaya word
     */
    private void addPair(String str, String def) {
        Map<String, String> word = new HashMap<String, String>();
        word.put("kashaya", str);
        word.put("definition", def);
        results.add(word);
        adapter.notifyDataSetChanged();
    }

    /**
     * find the index of difference character between two words
     *
     * @param str1 the first word
     * @param str2 the second word
     */
    private int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }
        if (str1 == null || str2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < str1.length() && i < str2.length(); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }
        if (i < str2.length() || i < str1.length()) {
            return i;
        }
        return -1;
    }

    // special character buttons actions
    public void addCharButtonClicked(View v) {
        setLetterButtons(!letterButtonVisibility);
    }

    public void middleDotClick(View v) {
        appendTextEditText("·");
    }

    public void underDotClick(View v) {
        appendTextEditText("ṭ");
    }

    public void thatHApostropheClick(View v) {
        appendTextEditText("ʰ");
    }

    public void thatHatClick(View v) {
        appendTextEditText("š");
    }

    public void questionMarkThingClick(View v) {
        appendTextEditText("ʔ");
    }

    public void weirdApostropheClick(View v) {
        appendTextEditText("́");
    }
}
