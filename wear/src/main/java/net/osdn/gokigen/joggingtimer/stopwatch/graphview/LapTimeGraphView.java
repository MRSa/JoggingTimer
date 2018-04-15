package net.osdn.gokigen.joggingtimer.stopwatch.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private Context context = null;
    private ITimerCounter timerCounter = null;
    private boolean isStarted = false;
    private long maxLaptime = 0;
    private long lastSystemLaptime = 0;
    private int totalLaptimeCount = 0;

    private List<Long> refTimeList = null;
    private List<Long> refLapTimeList = null;

    private List<Long> curTimeList = null;
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
        this.context = context;
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

        try
        {
            // 背景の表示
            drawBackground(canvas);

            // 基準の値表示
            drawReferenceLap(canvas);

            // 現在の値表示
            drawCurrentLap(canvas);

            // メッセージの表示
            drawMessage(canvas);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

        // Log.v("Canvas", "("+ width + "," + height + ")");
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

        float boxWidthUnit = width / (totalLaptimeCount);
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
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);

        int minimumLapTime = 15;
        float boxWidthUnit = width / (totalLaptimeCount);
        float boxHeightUnit = height / (maxLaptime * 1.2f);
        float circleRadius = (totalLaptimeCount > minimumLapTime) ? (boxWidthUnit / 4.0f) : (width / minimumLapTime / 4.0f);

        float startX = 0.0f;
        if ((curLapTimeList.size() <= 0)&&(isStarted))
        {
            long currentLapTime = System.currentTimeMillis() - lastSystemLaptime;
            canvas.drawCircle((startX + (boxWidthUnit / 2.0f)), (height - boxHeightUnit * currentLapTime), circleRadius, paint);
            return;
        }

        Path linePath = new Path();
        linePath.moveTo(0.0f, height);  // ０原点から線を引く
        for (Long time : curLapTimeList)
        {
            float cX = (startX + (boxWidthUnit / 2.0f));
            float cY = (height - boxHeightUnit * time);
            linePath.lineTo(cX, cY);
            canvas.drawCircle(cX, cY, circleRadius, paint);
            startX = startX + boxWidthUnit;
        }
        if (isStarted)
        {
            long currentLapTime = System.currentTimeMillis() - lastSystemLaptime;
            float cX = (startX + (boxWidthUnit / 2.0f));
            float cY = (height - boxHeightUnit * currentLapTime);
            linePath.lineTo(cX, cY);
            canvas.drawCircle(cX, cY, circleRadius, paint);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawPath(linePath, paint);
    }

    /**
     *
     *
     */
    private void drawMessage(Canvas canvas)
    {
        boolean drawTextLeft = false;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.0f);
        paint.setAntiAlias(true);
        //canvas.drawRect(rect, paint);

        int lapCount = 0;
        int refCount = 0;
        long diffTime = 0;
        long lapTime = 0;
        long currTime = 0;
        try
        {
            if (curTimeList != null)
            {
                lapCount = curTimeList.size();
            }

            if (refTimeList != null)
            {
                refCount = refTimeList.size();
            }

            if (lapCount > 1)
            {
                long totalTime = curTimeList.get(lapCount - 1) - curTimeList.get(0);
                currTime =  curTimeList.get(lapCount - 1) - curTimeList.get(lapCount - 2);
                //long currTime =  (lapCount > 2) ? curTimeList.get(lapCount - 1) - curTimeList.get(lapCount - 2) : 0;
                if ((lapCount <= refCount)&&(refTimeList != null))
                {
                    diffTime = totalTime - (refTimeList.get(lapCount - 1) - refTimeList.get(0));
                    lapTime = currTime - (refTimeList.get(lapCount - 1) - refTimeList.get(lapCount - 2));
                    //lapTime = currTime - ((lapCount > 2) ? refTimeList.get(lapCount - 1) - refTimeList.get(lapCount - 2) : 0);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String textToDraw = "";
        if ((lapTime == 0)&&(diffTime == 0)&&(lapCount > 1))
        {
            textToDraw = "[" + (lapCount - 1) + "] " + TimeStringConvert.getTimeString(currTime).toString();
            drawTextLeft = true;
        }
        else
        {
            if (lapTime != 0)
            {
                textToDraw = TimeStringConvert.getDiffTimeString(lapTime).toString();
            }
            if (diffTime != 0)
            {
                //  「前回ラップ時間の遅れ・進み / 全体時間の遅れ・進み」
                textToDraw = textToDraw + " / " + TimeStringConvert.getDiffTimeString(diffTime).toString();
            }
        }

        // 表示する文字の大きさの決定
        try
        {
            float density = context.getResources().getDisplayMetrics().density;
            paint.setTextSize(density * 20.0f + 0.5f);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            paint.setTextSize(32.0f);
        }
        paint.setAntiAlias(true);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float textWidth = paint.measureText(textToDraw);

        // 表示位置の調整...
        float x = (width < textWidth) ? 0.0f : (width - textWidth - 8.0f);
        float y = (height - textHeight - 2) - fm.ascent;
        if (drawTextLeft)
        {
            x = 0.0f;
        }
        canvas.drawText(textToDraw, x , y, paint);
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

        refTimeList = timerCounter.getReferenceLapTimeList();
        if (refTimeList == null)
        {
            return;
        }
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
        curTimeList = timerCounter.getLapTimeList();
        int lapTimeCount = curTimeList.size();
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
            lastSystemLaptime = curTimeList.get(0);
            return;
        }

        curLapTimeList.clear();
        long prevTime = curTimeList.get(0);
        for (Long time : curTimeList)
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
