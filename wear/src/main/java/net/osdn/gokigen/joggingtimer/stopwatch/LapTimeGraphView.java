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
        super.onDraw(canvas);

        // 背景の表示
        drawBackground(canvas);

        // 基準の値表示
        drawReferenceLap(canvas);

        // 現在の値表示
        drawCurrentLap(canvas);

        // メッセージの表示
        drawMessage(canvas);

        counter++;
    }

    /**
     *
     *
     */
    private void drawBackground(Canvas canvas)
    {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Rect rect = new Rect(0,0, width, height);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);

        canvas.drawText(width + "x" + height, width / 2 , height / 2, paint);
    }

    /**
     *
     *
     */
    private void drawReferenceLap(Canvas canvas)
    {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect rect = new Rect(0,0, width, height);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);
        canvas.drawRect(rect, paint);

    }


    /**
     *
     *
     */
    private void drawCurrentLap(Canvas canvas)
    {
    }


    /**
     *
     *
     */
    private void drawMessage(Canvas canvas)
    {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect rect = new Rect(0,0, width, height);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);
        canvas.drawRect(rect, paint);


        String message = width + "x" + height;
        float textWidth = paint.measureText(message);

        canvas.drawText(width + "x" + height, ((width / 2) - (textWidth / 2)) , height / 2, paint);
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
