package net.osdn.gokigen.joggingtimer.stopwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 *
 *
 */
public class LapTimeGraphView extends View
{
    private final String TAG = toString();
    private ITimerCounter timerCounter = null;
    private long counter = 0;

    /**
     *   コンストラクタ
     *
     */
    public LapTimeGraphView(Context context)
    {
        super(context);
        initComponent(context);
    }

    /**
     *   コンストラクタ
     *
     */
    public LapTimeGraphView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initComponent(context);
    }

    public void setITimerCounter(ITimerCounter counter)
    {
        timerCounter = counter;
    }

    /**
     *   コンストラクタ
     *
     */
    public LapTimeGraphView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    /**
     *   初期化ロジック
     */
    private void initComponent(Context context)
    {
        Log.v(TAG, "initComponent()");

        setWillNotDraw(false);
    }

    /**
     *
     *
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.v(TAG, "onDraw()");
        super.onDraw(canvas);

        drawBackground(canvas);

        counter++;
    }

    private void drawBackground(Canvas canvas)
    {
        Log.v(TAG, "drawBackground() : " + canvas.getWidth() + "x" + canvas.getHeight() + " " + counter);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Rect rect = new Rect(0,0, width, height);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(rect, paint);

        paint.setColor(Color.WHITE);
        canvas.drawText(width + "x" + height + " " + counter, 0, 0, paint);

    }

    /**
     *
     *
     */
    @Override
    public boolean performClick()
    {
        return (super.performClick());
    }
}
