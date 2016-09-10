package edu.upenn.cis350.hwk2.ui.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class SoundTextView extends TextView {

    public SoundTextView(Context context) {
        super(context);
    }

    public SoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundColor(Color.YELLOW);
    }

}
