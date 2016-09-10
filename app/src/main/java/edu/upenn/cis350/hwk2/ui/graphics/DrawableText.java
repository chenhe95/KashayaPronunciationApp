package edu.upenn.cis350.hwk2.ui.graphics;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by He on 2/27/2016.
 * Credits to StackOverflow/convexHull for help with Drawable + FloatingActionButtons
 *
 * A class to create customized text on FloatingActionButtons
 */
public class DrawableText extends Drawable {

    private Paint textPaint = null;
    private String text = null;
    private Context context = null;
    private ColorStateList color = null;
    protected int iHeight;
    protected int iWidth;
    protected int measuredWidth, measuredHeight;
    private float ascent = 0;


    /**
     * Example usage:
     * FloatingActionButton middleDotThing = (FloatingActionButton) findViewById(R.id.middle_dot_thing);
     middleDotThing.setImageDrawable(new DrawableText(middleDotThing.getContext(), "·", ColorStateList
     .valueOf(Color.WHITE), 32.f));
     * @param ctx - context of the floating action button
     * @param text
     * @param color - has to be in the format of ColorStateList.valueOf(android.graphics.Color.COLOR_HERE)
     * @param textSize
     */
    public DrawableText(Context ctx, String text, ColorStateList color, float textSize) {
        textPaint = new Paint();
        context = ctx;
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        this.text = text;
        this.textPaint.setTextSize(textSize);
        measureSize();
        setBounds(0, 0, iWidth, iHeight);
        this.color = color;
        textPaint.setColor(color.getDefaultColor());
    }

    public final void setBoundsByMeasuredSize() {
        setBounds(0, 0, measuredWidth, measuredHeight);
        invalidateSelf();
    }

    protected void measureSize() {
        ascent = -textPaint.ascent();
        iWidth = (int) (0.5f + textPaint.measureText(text));
        iHeight = (int) (0.5f + textPaint.descent() + ascent);
        measuredWidth = iWidth;
        measuredHeight = iHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        if (text == null || text.isEmpty()) {
            return;
        }
        final Rect bounds = getBounds();
        int stack = canvas.save();
        canvas.translate(bounds.left, bounds.top);
        if (text != null && !text.isEmpty()) {
            final float x = bounds.width() >= iWidth ? bounds.centerX() : iWidth * 0.5f;
            float y = (bounds.height() - iHeight) * 0.5f + ascent;
            canvas.drawText(text, x, y, textPaint);
        }
        canvas.restoreToCount(stack);
    }

    @Override
    public void setAlpha(int alpha) {
        if (textPaint.getAlpha() != alpha) {
            textPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (textPaint.getColorFilter() == null || !textPaint.getColorFilter().equals(cf)) {
            textPaint.setColorFilter(cf);
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
