package edu.upenn.cis350.hwk2.ui.support;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.upenn.cis350.hwk2.data.Consonant;
import edu.upenn.cis350.hwk2.data.Glottal;
import edu.upenn.cis350.hwk2.data.Vowel;
import edu.upenn.cis350.hwk2.ui.HearSoundsActivity;

public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private Activity parent;

    public TabFragmentAdapter(Activity parent, FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.parent = parent;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment vowelTag = HearSoundsFragment.getInstance((HearSoundsActivity) parent, Vowel.VOWELS, "Vowels");
                return vowelTag;
            case 1:
                Fragment consonantTag = HearSoundsFragment.getInstance((HearSoundsActivity) parent, Consonant.CONSONANTS, "Consonants");
                return consonantTag;
            case 2:
                Fragment glottalTag = HearSoundsFragment.getInstance((HearSoundsActivity) parent, Glottal.GLOTTALS, "Glottals");
                return glottalTag ;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}