package net.osdn.gokigen.joggingtimer.stopwatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.recordlist.ListActivity;
import net.osdn.gokigen.joggingtimer.stopwatch.graphview.LapTimeGraphView;
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 *
 */
public class MainActivity extends WearableActivity implements IClickCallback, MyTimerTrigger.ITimeoutReceiver, MyTimerCounter.ICounterStatusNotify
{
    private final String TAG = toString();
    private final IWearableActivityControl controller = new WearableActivityController();
    private MyTimerCounter counter = new MyTimerCounter();
    private boolean isCounterLapTime = true;
    private boolean isLaptimeView = true;
    private boolean pendingStart = false;
    private int currentLapCount = 0;
    private ITimerStopTrigger stopTrigger = null;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        controller.setup(this, this, counter);

        // Enables Always-on
        setAmbientEnabled();
    }

    /**
     *
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        // インテントを取得する
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.v(TAG, "onResume() : " + action);

        boolean isStartTimer = false;
        if (action != null)
        {
            if (action.equals("com.google.android.wearable.action.STOPWATCH"))
            {
                isStartTimer = true;
            }
            else if (action.equals("vnd.google.fitness.TRACK"))
            {
                String activity = intent.getStringExtra("actionStatus");
                if ((activity != null)&&(activity.equals("ActiveActionStatus")))
                {
                    isStartTimer = true;
                }
            }
        }
        isLaptimeView = controller.getDisplayMode();
        controller.setupReferenceData();

        if (isStartTimer)
        {
            // start a timer!
            //startTimer();
            pendingStart = true;
        }
    }

    /**
     *
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    /**
     *
     *
     */
    @Override
    public void onStart()
    {
        super.onStart();
        Log.v(TAG, "onStart()");

        // データベースのセットアップ
        counter.setCallback(this);
        controller.setupDatabase(this, false);
    }

    /**
     *
     *
     */
    @Override
    public void onStop()
    {
        super.onStop();
        Log.v(TAG, "onStop()");
        if (stopTrigger != null)
        {
            stopTrigger.forceStop();
        }
        controller.exitApplication(this);
    }

    /**
     *
     *
     */
    @Override
    public void onEnterAmbient(Bundle ambientDetails)
    {
        super.onEnterAmbient(ambientDetails);
        Log.v(TAG, "onEnterAmbient()");
    }

    /**
     *
     *
     */
    @Override
    public void onExitAmbient()
    {
        super.onExitAmbient();
        Log.v(TAG, "onExitAmbient()");
        //updateTimerLabel();
    }

    /**
     *
     *
     */
    @Override
    public void onUpdateAmbient()
    {
        super.onUpdateAmbient();
        Log.v(TAG, "onUpdateAmbient()");
    }

    /**
     *
     */
    private void updateTimerLabel()
    {
        ITimerCounter timerCounter = counter;
        if (timerCounter == null)
        {
            return;
        }

        int bgColor;
        BoxInsetLayout insetLayout = findViewById(R.id.box_inset_layout);
        RelativeLayout layout = findViewById(R.id.relative_main_layout);

        ImageButton btn1 = findViewById(R.id.btn1);
        ImageButton btn2 = findViewById(R.id.btn2);
        ImageButton btn3 = findViewById(R.id.btn3);

        updateMainSubCounter();

        if (timerCounter.isStarted())
        {
            bgColor = Color.BLACK;
            insetLayout.setBackgroundColor(bgColor);
            insetLayout.invalidate();

            layout.setBackgroundColor(bgColor);
            layout.invalidate();

            btn1.setImageResource(R.drawable.ic_flag_black_24dp);
            btn1.setBackgroundColor(bgColor);
            btn1.setVisibility(View.VISIBLE);
            btn1.invalidate();

            btn2.setImageResource(R.drawable.ic_stop_black_24dp);
            btn2.setBackgroundColor(bgColor);
            btn2.setVisibility(View.VISIBLE);
            btn2.invalidate();

            btn3.setImageResource(R.drawable.ic_block_black_24dp);
            btn3.setBackgroundColor(bgColor);
            btn3.setVisibility(View.INVISIBLE);
            btn3.invalidate();
        }
        else if (timerCounter.isReset())
        {
            bgColor = Color.BLACK;
            insetLayout.setBackgroundColor(bgColor);
            insetLayout.invalidate();

            layout.setBackgroundColor(bgColor);
            layout.invalidate();

            btn1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            btn1.setBackgroundColor(bgColor);
            btn1.setVisibility(View.VISIBLE);
            btn1.invalidate();

            btn2.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp);
            btn2.setBackgroundColor(bgColor);
            btn2.setVisibility(View.VISIBLE);
            btn2.invalidate();

            btn3.setImageResource(R.drawable.ic_refresh_black_24dp);
            btn3.setBackgroundColor(bgColor);
            btn3.setVisibility(View.INVISIBLE);
            btn3.invalidate();
        }
        else
        {
            bgColor = Color.BLACK;
            insetLayout.setBackgroundColor(bgColor);
            insetLayout.invalidate();

            layout.setBackgroundColor(bgColor);
            layout.invalidate();

            btn1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            btn1.setVisibility(View.VISIBLE);
            btn1.setBackgroundColor(bgColor);
            btn1.invalidate();

            btn2.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp);
            btn2.setVisibility(View.VISIBLE);
            btn2.setBackgroundColor(bgColor);
            btn2.invalidate();

            btn3.setImageResource(R.drawable.ic_refresh_black_24dp);
            btn3.setVisibility(View.VISIBLE);
            btn3.setBackgroundColor(bgColor);
            btn3.invalidate();
        }
        updateElapsedTimes();
    }

    @Override
    public void clickedCounter()
    {
        // 表示順番を変える
        isCounterLapTime = !isCounterLapTime;
    }

    /**
     *
     */
    @Override
    public void clickedBtn1()
    {
        startTimer();
    }

    /**
     *
     *
     */
    private void startTimer()
    {
        try
        {
            ITimerCounter timerCounter = counter;
            if (timerCounter != null)
            {
                LapTimeGraphView graphView = findViewById(R.id.graph_area);
                if (timerCounter.isStarted())
                {
                    Log.v(TAG, "startTimer() LAP TIME");
                    // チャタリング防止（ラップタイムとして、３秒以内は記録しないようにする）
                    long currentElapsedTime = timerCounter.getCurrentElapsedTime();
                    if (currentElapsedTime > 3000)
                    {
                        currentLapCount++;
                        long lapTime = timerCounter.timeStamp();
                        long refLapTime = timerCounter.getReferenceLapTime(currentLapCount);
                        long diffTime = (refLapTime == 0) ? 0 :  (currentElapsedTime - refLapTime);
                        controller.vibrate(50);
                        controller.getDataEntry().appendTimeData(lapTime);
                        controller.addTimeStamp(currentLapCount, currentElapsedTime, diffTime);
                        //Log.v(TAG, " [[[ " + currentLapCount + " lap: " + currentElapsedTime + " diff:" + diffTime + " (" + refLapTime + ") ]]]");

                        if (graphView != null)
                        {
                            graphView.notifyLapTime();
                        }
                    }
                }
                else
                {
                    Log.v(TAG, "startTimer() START");
                    timerCounter.start();
                    MyTimerTrigger trigger = new MyTimerTrigger(this, 100, timerCounter);
                    trigger.startTimer();
                    currentLapCount = 0;
                    stopTrigger = trigger;
                    controller.timerStarted(true);
                    controller.vibrate(120);

                    Date date = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String title = sdf1.format(date);
                    long startTime = timerCounter.getStartTime();
                    controller.getDataEntry().createIndex(title, startTime);

                    if (graphView != null)
                    {
                        graphView.notifyStarted(startTime);
                    }

                }
                updateTimerLabel();
            }
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
    private boolean stopTimer()
    {
        boolean ret = false;
        try
        {
            ITimerCounter timerCounter = counter;
            if (timerCounter != null)
            {
                if (timerCounter.isStarted())
                {
                    timerCounter.stop();
                    controller.timerStarted(false);
                    controller.vibrate(120);
                    controller.getDataEntry().finishTimeData(timerCounter.getStartTime(), timerCounter.getStopTime());
                    ret = true;

                    LapTimeGraphView graphView = findViewById(R.id.graph_area);
                    if (graphView != null)
                    {
                        graphView.notifyStopped();
                    }
                }
                updateTimerLabel();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (ret);
    }

    /**
     *
     */
    @Override
    public void clickedBtn2()
    {
        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            if (!timerCounter.isStarted())
            {
                // 停止中は、記録一覧を呼び出す
                launchListActivity();

                // ぶるぶる
                controller.vibrate(35);
            }
        }
        updateTimerLabel();
    }

    /**
     *
     */
    @Override
    public void clickedBtn3()
    {
        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            if (!timerCounter.isStarted())
            {
                timerCounter.reset();
                controller.vibrate(50);
                controller.clearTimeStamp();
                currentLapCount = 0;
                LapTimeGraphView graphView = findViewById(R.id.graph_area);
                if (graphView != null)
                {
                    graphView.notifyReset();
                }
            }
            updateTimerLabel();
        }
    }

    @Override
    public void clickedArea()
    {
        Log.v(TAG, "clickedArea()");

    }

    @Override
    public boolean pushedBtn1()
    {
        return (false);
    }

    @Override
    public boolean pushedBtn2()
    {
        return (stopTimer());
    }

    @Override
    public boolean pushedBtn3()
    {
        return (false);
    }

    @Override
    public boolean pushedArea()
    {
        isLaptimeView = !isLaptimeView;
        controller.setDisplayMode(isLaptimeView);
        Log.v(TAG, "pushedArea() : " + isLaptimeView);
        changeGraphicView(isLaptimeView);
        updateTimerLabel();
        return (true);
    }

    /**
     *
     *
     */
    @Override
    public void timeout()
    {
        try
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateTimerLabel();
                }
            });
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
    private void updateMainSubCounter()
    {
        TextView main = findViewById(R.id.main_counter);
        TextView sub = findViewById(R.id.sub_counter1);

        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            long time1 = timerCounter.getPastTime();
            CharSequence str1 = TimeStringConvert.getTimeString(time1);
            CharSequence str2 = "";
            if (timerCounter.isStarted())
            {
                long time2 = timerCounter.getCurrentElapsedTime();
                int lapCount = timerCounter.getElapsedCount();
                if ((time2 >= 100) && (lapCount > 1))
                {
                    str2 =  "[" + lapCount + "] " + TimeStringConvert.getTimeString(time2);
                }
            }

            if ((str2.length() > 0)&&(isCounterLapTime))
            {
                // ラップタイムの方を大きく表示する
                main.setText(str2);
                sub.setText(str1);
            }
            else
            {
                main.setText(str1);
                sub.setText(str2);
            }
            main.invalidate();
            sub.invalidate();
        }
    }

    /**
     *
     *
     */
    private void updateElapsedTimes()
    {
        if (isLaptimeView)
        {
           updateElapsedTimesGraph();
        }
        else
        {
            updateElapsedTimesText();
        }
    }

    /**
     *
     *
     */
    private void updateElapsedTimesGraph()
    {
        //Log.v(TAG, "updateElapsedTimesGraph()");
        LapTimeGraphView view = findViewById(R.id.graph_area);
        view.invalidate();
    }

    /**
     *
     *
     */
    private void updateElapsedTimesText()
    {
        // Log.v(TAG, "updateElapsedTimesText()");
    }

    /**
     *  Launch ListActivity
     *
     */
    private void launchListActivity()
    {
        Log.v(TAG, "launchListActivity()");
        try
        {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
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
    @Override
    protected void onUserLeaveHint()
    {
        Log.v(TAG, "onUserLeaveHint() " );
        // ハードキー（ホームボタン）が押されたとき、これがひろえるが...
    }

    /**
     *
     *
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        Log.v(TAG, "dispatchKeyEvent() : " + event.getAction() + " (" + event.getKeyCode() + ")");

        return (super.dispatchKeyEvent(event));
    }

    /**
     *
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Log.v(TAG, "onKeyDown() : " + event.getAction() + " (" + event.getKeyCode() + ")" + keyCode);
        if (event.getRepeatCount() == 0)
        {
            if (keyCode == KeyEvent.KEYCODE_STEM_1)
            {
                startTimer();
                return (true);
            }
            else if (keyCode == KeyEvent.KEYCODE_STEM_2)
            {
                startTimer();
                return (true);
            }
            else if (keyCode == KeyEvent.KEYCODE_STEM_3)
            {
                startTimer();
                return (true);
            }
        }
        return (super.onKeyDown(keyCode, event));
    }

    /**
     *
     *
     */
    @Override
    public void counterStatusChanged(final boolean forceStartTimer)
    {
        ITimerCounter timerCounter = counter;
        if (forceStartTimer)
        {
            try
            {
                LapTimeGraphView graphView = findViewById(R.id.graph_area);
                if (timerCounter != null)
                {
                    MyTimerTrigger trigger = new MyTimerTrigger(this, 100, timerCounter);
                    trigger.startTimer();
                    stopTrigger = trigger;
                }
                if (graphView != null)
                {
                    graphView.notifyLapTime();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                // 自動スタート時の処理。。
                if (pendingStart)
                {
                    startTimer();
                    pendingStart = false;
                }

                // ラップタイム表示状態の更新
                reloadLapTimeList(forceStartTimer);

                // 表示ビューの切り替え
                changeGraphicView(isLaptimeView);

                // 表示のボタン状態を変更
                updateTimerLabel();
            }
        });
    }

    /**
     *
     *
     */
    private void reloadLapTimeList(final boolean forceStartTimer)
    {
        ITimerCounter timerCounter = counter;
        if ((!forceStartTimer)||(timerCounter == null))
        {
            return;
        }

        // Adapter と TimerCounterの整合性を確認
        try
        {
            List<Long> lapTimeList = timerCounter.getLapTimeList();
            int lapCount = lapTimeList.size();
            int listCount = controller.getLapTimeCount();
            if (lapCount != listCount)
            {
                Log.v(TAG, "LAP COUNT IS MISMATCH!!! lap:" + lapCount + " vs list:" + listCount);
                int index = 0;
                controller.clearTimeStamp();
                long prevTime = lapTimeList.get(0);
                for (long lapTime : lapTimeList)
                {
                    index++;
                    if (prevTime != lapTime)
                    {
                        long refLapTime = counter.getReferenceLapTime(index - 1);
                        long curLapTime = lapTime - prevTime;
                        long calcRefLapTime = (refLapTime == 0) ? 0 : (curLapTime - refLapTime);
                        controller.addTimeStamp((index - 1), curLapTime, calcRefLapTime);
                    }
                    prevTime = lapTime;
                }
                currentLapCount = lapCount - 1;
            }
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
    private void changeGraphicView(boolean isGraphics)
    {
        try
        {
            LapTimeGraphView graphView = findViewById(R.id.graph_area);
            ListView listView = findViewById(R.id.laptime_list_area);
            if (isGraphics)
            {
                graphView.setITimerCounter(counter);
                graphView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
            else
            {
                graphView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            //controller.vibrate(30);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
