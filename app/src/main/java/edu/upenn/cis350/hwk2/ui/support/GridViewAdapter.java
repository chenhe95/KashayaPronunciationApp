package edu.upenn.cis350.hwk2.ui.support;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data.Sound;

public class GridViewAdapter<T extends Sound> extends BaseAdapter {

    // private fields
    private Context c;
    private List<T> symbolList;

    // constructor
    public GridViewAdapter(Context c, List<T> vowelList, String tabName) {
        this.c = c;
        this.symbolList = vowelList;
    }

    @Override
    public int getCount() {
        return symbolList.size();
    }

    @Override
    public Object getItem(int position) {
        return symbolList.get(position);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = View.inflate(c, R.layout.view_symbol, null);

        // set text
        TextView soundSymbol = (TextView) v.findViewById(R.id.sound_symbol);
        soundSymbol.setText(symbolList.get(position).getName());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.FILL_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        soundSymbol.setLayoutParams(lp);
        v.setTag(symbolList.get(position).getId());

        // set text size
        soundSymbol.setTextSize(20);
        v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 135));

        return v;
    }
}
