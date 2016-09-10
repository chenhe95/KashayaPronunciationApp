package edu.upenn.cis350.hwk2.ui.support;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data.Vowel;
import edu.upenn.cis350.hwk2.ui.CompareSoundsActivity;

public class CompareSoundsVowel extends Fragment {


    public CompareSoundsVowel() {
        // Required empty public constructor
    }

    private List<Vowel> vowelList;

    private GridView vowelGrid;
    private GridViewAdapter gridViewAdapter;

    private KashayaPlayer player = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        player = KashayaPlayer.getPlayerInstance();

        View fragmentView = inflater.inflate(R.layout.fragment_compare_sounds_consonant, container, false);

        vowelGrid = (GridView) fragmentView.findViewById(R.id.gridView_sound);
        vowelList = new ArrayList<>();

        for (Vowel c : Vowel.VOWELS) {
            vowelList.add(c);
        }

        vowelGrid.setNumColumns(4);

        gridViewAdapter = new GridViewAdapter<Vowel>(getActivity().getApplicationContext(), vowelList, "Vowels");
        vowelGrid.setAdapter(gridViewAdapter);

        vowelGrid.setOnItemClickListener(CompareSoundsActivity.comparatorListener);

        return fragmentView;
    }
}

