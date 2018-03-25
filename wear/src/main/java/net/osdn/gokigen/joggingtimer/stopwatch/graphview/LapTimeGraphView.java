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
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class LapTimeGraphView extends View
{
    private ITimerCounter timerCounter = null;
    private boolean isStarted = false;
    private long maxLaptime = 0;
    private long lastSystemLaptime = 0;
    private long currentLapTime = 0;
    private int totalLaptimeCount = 0;
    private List<Long> refLapTimeList = null;
    private List<Long> curLapTimeList = null;

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
        curLapTimeList = new ArrayList<>();
    }

    /**
     *
     *
     */
    public void setITimerCounter(ITimerCounter counter)
    {
        timerCounter = counter;
        parseReferenceTimeList();
        parseLapTime();
    }

    /**
     *
     *
     */
    public void notifyStarted(long startTime)
    {
        curLapTimeList.clear();
        lastSystemLaptime = startTime;
        isStarted = true;
    }

    /**
     *
     *
     */
    public void notifyLapTime()
    {
        // ラップタイムの取得
        parseLapTime();
        isStarted = true;
    }

    /**
     *
     *
     */
    public void notifyStopped()
    {
        // ラップタイムの取得
        parseLapTime();
        isStarted = false;
    }

    /**
     *
     *
     */
    public void notifyReset()
    {
        curLapTimeList.clear();
        isStarted = false;
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

        float boxWidthUnit = width / totalLaptimeCount;
        float boxHeightUnit = height / (maxLaptime * 1.2f);

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
        if (curLapTimeList == null)
        {
            return;
        }

        float width = canvas.getWidth();
        float height = canvas.getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);

        float boxWidthUnit = width / totalLaptimeCount;
        float boxHeightUnit = height / (maxLaptime * 1.2f);
        float circleRadius = boxWidthUnit / 4.0f;

        float startX = 0.0f;
        if ((curLapTimeList.size() <= 0)&&(isStarted))
        {
            currentLapTime = System.currentTimeMillis() - lastSystemLaptime;
            canvas.drawCircle((startX + (boxWidthUnit / 2.0f)), (height - boxHeightUnit * currentLapTime), circleRadius, paint);
            return;
        }

        for (Long time : curLapTimeList)
        {
            canvas.drawCircle((startX + (boxWidthUnit / 2.0f)), (height - boxHeightUnit * time), circleRadius, paint);
            startX = startX + boxWidthUnit;
        }

        if (isStarted)
        {
            currentLapTime = System.currentTimeMillis() - lastSystemLaptime;
            canvas.drawCircle((startX + (boxWidthUnit / 2.0f)), (height - boxHeightUnit * currentLapTime), circleRadius, paint);
        }
    }

    /**
     *
     *
     */
    private void drawMessage(Canvas canvas)
    {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);
        //canvas.drawRect(rect, paint);

        int lapCount = 0;
        if (curLapTimeList != null)
        {
            lapCount = curLapTimeList.size();
        }

        String lap = "";
        if (currentLapTime == 0)
        {
            lap = TimeStringConvert.getDiffTimeString(currentLapTime).toString();
        }
        String message = " [" + lapCount +"/" + totalLaptimeCount + "] " + lap;
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
        totalLaptimeCount = refTimeList.size();
        maxLaptime = 0;
        if (totalLaptimeCount <= 1)
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
            if (currTime > maxLaptime)
            {
                maxLaptime = currTime;
            }
            prevTime = time;
        }
    }

    /**
     *
     *
     */
    private void parseLapTime()
    {
        if (timerCounter == null)
        {
            return;
        }
        List<Long> lapTimeList = timerCounter.getLapTimeList();
        int lapTimeCount = lapTimeList.size();
        if (lapTimeCount > totalLaptimeCount)
        {
            totalLaptimeCount = lapTimeCount;
        }
        if (lapTimeCount < 1)
        {
            // 開始前の場合...
            return;
        }
        if (lapTimeCount == 1)
        {
            lastSystemLaptime = lapTimeList.get(0);
            return;
        }

        curLapTimeList.clear();
        long prevTime = lapTimeList.get(0);
        for (Long time : lapTimeList)
        {
            long currTime = time - prevTime;
            if (currTime > 0)
            {
                curLapTimeList.add(currTime);
            }
            if (currTime > maxLaptime)
            {
                maxLaptime = currTime;
            }
            prevTime = time;
        }
        lastSystemLaptime = prevTime;
    }

    @Override
    public boolean performClick()
    {
        return (super.performClick());
    }
}
