package net.osdn.gokigen.joggingtimer.stopwatch.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import net.osdn.gokigen.joggingtimer.stopwatch.ITimerCounter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class LapTimeGraphView extends View
{
    private ITimerCounter timerCounter = null;
    private long counter = 0;
    private long maxReferenceTime = 0;
    private int referenceCount = 0;
    private List<Long> refLapTimeList = null;

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
        setWillNotDraw(false);
    }

    /**
     *
     *
     */
    public void setITimerCounter(ITimerCounter counter)
    {
        timerCounter = counter;
        parseReferenceTimeList();
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
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
    }

    /**
     *
     *
     */
    private void drawReferenceLap(Canvas canvas)
    {
        if ((refLapTimeList == null)||(refLapTimeList.size() <= 0))
        {
            return;
        }

        float width = canvas.getWidth();
        float height = canvas.getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);

        float boxWidthUnit = width / refLapTimeList.size();
        float boxHeightUnit = height / (maxReferenceTime * 1.2f);

        float startX = 0.0f;
        for (Long time : refLapTimeList)
        {
            RectF barRect = new RectF(startX, (height - boxHeightUnit * time), (startX + boxWidthUnit), height);
            canvas.drawRect(barRect, paint);
            startX = startX + boxWidthUnit;
        }
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

        String message = width + "x" + height + "  [" + referenceCount + "] " + maxReferenceTime;
        float textWidth = paint.measureText(message);

        canvas.drawText(message, ((width / 2) - (textWidth / 2)) , height / 2, paint);
    }

    /**
     *
     *
     */
    private void parseReferenceTimeList()
    {
        if (timerCounter == null)
        {
            return;
        }
        refLapTimeList = null;

        List<Long> refTimeList = timerCounter.getReferenceLapTimeList();
        referenceCount = refTimeList.size();
        maxReferenceTime = 0;
        if (referenceCount <= 1)
        {
            return;
        }
        refLapTimeList = new ArrayList<>();
        long prevTime = refTimeList.get(0);
        for (Long time : refTimeList)
        {
            long currTime = time - prevTime;
            if (currTime > 0)
            {
                refLapTimeList.add(currTime);
            }
            if (currTime > maxReferenceTime)
            {
                maxReferenceTime = currTime;
            }
            prevTime = time;
        }
    }

    @Override
    public boolean performClick()
    {
        return (super.performClick());
    }
}
