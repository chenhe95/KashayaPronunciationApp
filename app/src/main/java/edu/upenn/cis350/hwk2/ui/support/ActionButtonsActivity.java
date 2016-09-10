package edu.upenn.cis350.hwk2.ui.support;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by He on 2/27/2016.
 * This class is for Activities where you want to have the
 * 'search' 'play' 'info' button after clicking a sound
 */
public abstract class ActionButtonsActivity extends AppCompatActivity {

    public abstract boolean isRecordSelected();
    public abstract boolean isSearchSelected();
    public abstract boolean isClassSelected();

}
