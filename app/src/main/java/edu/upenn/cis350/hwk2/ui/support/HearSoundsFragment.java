package edu.upenn.cis350.hwk2.ui.support;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis350.hwk2.data.Consonant;
import edu.upenn.cis350.hwk2.data.Glottal;
import edu.upenn.cis350.hwk2.data.Vowel;
import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data.Sound;
import edu.upenn.cis350.hwk2.ui.ClassSelectedActivity;
import edu.upenn.cis350.hwk2.ui.CompareSingleSoundActivity;
import edu.upenn.cis350.hwk2.ui.HearWordsActivity;

/**
 * Created by He on 26/2/2016.
 */

public class HearSoundsFragment<T extends Sound> extends Fragment {

    private List<T> itemList;

    private T[] listToAdd = null;

    private GridView itemGrid;
    private GridViewAdapter gridViewAdapter;

    private KashayaPlayer player = null;

    private ActionButtonsActivity parent = null;

    private String tabName;

    public static <T extends Sound> HearSoundsFragment getInstance(ActionButtonsActivity parent, T[] list, String tabName) {
        HearSoundsFragment<T> hsf = new HearSoundsFragment<>();
        hsf.parent = parent;
        hsf.listToAdd = list;
        hsf.tabName = tabName;
        return hsf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        player = KashayaPlayer.getPlayerInstance();

        View fragmentView = inflater.inflate(R.layout.fragment_sounds, container, false);

        // set up grid
        itemGrid = (GridView) fragmentView.findViewById(R.id.gridView);
        itemList = new ArrayList<>();

        for (T t : listToAdd) {
            itemList.add(t);
        }

        // check which tab
        if (tabName.equals("Vowels")) {
            itemGrid.setNumColumns(4);
        } else if (tabName.equals("Consonants")) {
            itemGrid.setNumColumns(6);
        } else if (tabName.equals("Glottals")) {
            itemGrid.setNumColumns(6);
        }

        gridViewAdapter = new GridViewAdapter<T>(getActivity().getApplicationContext(), itemList, tabName);
        itemGrid.setAdapter(gridViewAdapter);

        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                TextView vowel_clicked = (TextView) view.findViewById(R.id.sound_symbol);
                String name = vowel_clicked.getText().toString().toLowerCase();

                if (parent.isRecordSelected()) {

                    // create intent to compare sound
                    Intent compareSingleSoundActivityIntent = new Intent(view.getContext(), CompareSingleSoundActivity.class);
                    compareSingleSoundActivityIntent.putExtra("word", name);
                    parent.startActivity(compareSingleSoundActivityIntent);

                } else if (parent.isSearchSelected()) {

                    // create intent to search words with sound
                    Intent hearWordsActivityIntent = new Intent(parent, HearWordsActivity.class);
                    hearWordsActivityIntent.putExtra(HearWordsActivity.INTENT_ID_SEARCH_TERM, name);
                    hearWordsActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    parent.startActivity(hearWordsActivityIntent);

                } else if (parent.isClassSelected()) {

                    // create intent for sound class
                    Intent i = new Intent(parent, ClassSelectedActivity.class);
                    String soundClass = "";

                    // find out which class the sound belongs to
                    if (tabName.equals("Vowels")) {
                        for (int j = 0; j < Vowel.VOWELS.length; j++) {
                            if (name.equals(Vowel.VOWELS[j].getName())) {
                                if (Vowel.VOWELS[j].getForm() == "short")
                                    soundClass = "sounds";
                                else
                                    soundClass = "length";
                            }
                        }
                    } else if (tabName.equals("Consonants")) {
                        for (int j = 0; j < Consonant.CONSONANTS.length; j++) {
                            if (Consonant.CONSONANTS[j].getName().equals(name)) {
                                soundClass = Consonant.CONSONANTS[j].getClassName();
                            }
                        }
                    } else if (tabName.equals("Glottals")) {
                        for (int j = 0; j < Glottal.GLOTTALS.length; j++) {
                            if (Glottal.GLOTTALS[j].getName().equals(name)) {
                                soundClass = "glottals";
                            }
                        }
                    }

                    if (!soundClass.equals("")) {
                        i.putExtra("class_name", soundClass);
                        parent.startActivity(i);
                    }

                } else if (!KashayaPlayer.isPlaying()) {

                    // play sound, add "a" as suffix if consonant
                    if (tabName.equals("Consonants")) {
                        player.playMedia(name + "a");
                    } else {
                        player.playMedia(name);
                    }

                }
            }
        });

        return fragmentView;
    }
}