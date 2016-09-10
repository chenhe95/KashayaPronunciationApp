package edu.upenn.cis350.hwk2.ui.support;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data.Glottal;
import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.ui.CompareSoundsActivity;


public class CompareSoundsGlottal extends Fragment {


    public CompareSoundsGlottal() {
        // Required empty public constructor
    }

    private List<Glottal> glottalList;

    private GridView glottalsGrid;
    private GridViewAdapter gridViewAdapter;

    private KashayaPlayer player = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        player = KashayaPlayer.getPlayerInstance();

        View fragmentView = inflater.inflate(R.layout.fragment_compare_sounds_consonant, container, false);

        // vowels = (ListView) fragmentView.findViewById(R.id.listview_product);
        glottalsGrid = (GridView) fragmentView.findViewById(R.id.gridView_sound);
        glottalList = new ArrayList<>();

        for (Glottal c : Glottal.GLOTTALS) {
            glottalList.add(c);
        }

        glottalsGrid.setNumColumns(6);
        
        gridViewAdapter = new GridViewAdapter<Glottal>(getActivity().getApplicationContext(), glottalList, "Glottals");
        glottalsGrid.setAdapter(gridViewAdapter);

        glottalsGrid.setOnItemClickListener(CompareSoundsActivity.comparatorListener);

        return fragmentView;
    }
}

