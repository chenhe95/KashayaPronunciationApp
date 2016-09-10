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
import edu.upenn.cis350.hwk2.data.Consonant;
import edu.upenn.cis350.hwk2.ui.CompareSoundsActivity;


public class CompareSoundsConsonant extends Fragment {


    public CompareSoundsConsonant() {
        // Required empty public constructor
    }

    private List<Consonant> consonantList;

    private GridView consonantsGrid;
    private GridViewAdapter gridViewAdapter;

    private KashayaPlayer player = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        player = KashayaPlayer.getPlayerInstance();

        View fragmentView = inflater.inflate(R.layout.fragment_compare_sounds_consonant, container, false);

        // vowels = (ListView) fragmentView.findViewById(R.id.listview_product);
        consonantsGrid = (GridView) fragmentView.findViewById(R.id.gridView_sound);
        consonantList = new ArrayList<>();

        for (Consonant c : Consonant.CONSONANTS) {
            consonantList.add(c);
        }
        //consonantList.add(new Consonant(999, ">>", "class", "form"));
//        listAdapter = new ProductListAdapter(getActivity().getApplicationContext(), vowelList);
//        vowels.setAdapter(listAdapter);

        consonantsGrid.setNumColumns(6);

        gridViewAdapter = new GridViewAdapter<Consonant>(getActivity().getApplicationContext(), consonantList, "Consonants");
        consonantsGrid.setAdapter(gridViewAdapter);

        consonantsGrid.setOnItemClickListener(CompareSoundsActivity.comparatorListener);

        return fragmentView;
    }
}

