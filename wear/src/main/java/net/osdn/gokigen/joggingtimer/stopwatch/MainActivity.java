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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.recordlist.ListActivity;
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
    private boolean isCounterLapTime = false;
    private boolean isLaptimeView = true;
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
        controller.setupReferenceData();

        if (isStartTimer)
        {
            // start a timer!
            startTimer();
        }

        // 表示ビューの切り替え
        changeGraphicView(isLaptimeView);
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
                if (timerCounter.isStarted())
                {
                    Log.v(TAG, "startTimer() LAP TIME");
                    // チャタリング防止（ラップタイムとして、３秒以内は記録しないようにする）
                    if (timerCounter.getCurrentElapsedTime() > 3000)
                    {
                        long lapTime = timerCounter.timeStamp();
                        controller.vibrate(50);
                        controller.getDataEntry().appendTimeData(lapTime);
                    }
                }
                else
                {
                    Log.v(TAG, "startTimer() START");
                    timerCounter.start();
                    MyTimerTrigger trigger = new MyTimerTrigger(this, 100, timerCounter);
                    trigger.startTimer();
                    stopTrigger = trigger;
                    controller.timerStarted(true);
                    controller.vibrate(120);

                    Date date = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String title = sdf1.format(date);
                    controller.getDataEntry().createIndex(title, timerCounter.getStartTime());
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
        Log.v(TAG, "updateElapsedTimesGraph()");

        LapTimeGraphView view = findViewById(R.id.graph_area);
        view.invalidate();
    }

    /**
     *
     *
     */
    private void updateElapsedTimesText()
    {
/*
        String dummy = "";
        TextView area1 = findViewById(R.id.sub_counter2);
        TextView area2 = findViewById(R.id.sub_counter3);
        TextView area3 = findViewById(R.id.sub_counter4);
        TextView area4 = findViewById(R.id.sub_counter5);
        TextView area5 = findViewById(R.id.sub_counter6);

        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            List<Long> elapsedTimes = timerCounter.getTimerList();
            List<Long> referenceTimes = timerCounter.getReferenceTimeList();
            int indexSize = elapsedTimes.size();
            int referenceSize = referenceTimes.size();
            if (indexSize <= 1)
            {
                // ラップの記録がないので表示しません
                area1.setText(dummy);
                area1.invalidate();
                area2.setText(dummy);
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                area4.setText(dummy);
                area4.invalidate();
                area5.setText(dummy);
                area5.invalidate();
                return;
            }
            if (indexSize <= 2)
            {
                // ラップが１つとれた場合
                long time = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
                long refTime = (referenceSize >= indexSize) ? (referenceTimes.get(indexSize - 1) - referenceTimes.get(indexSize - 2)) : 0;
                String elapsedTime = "[" + (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time);
                if (refTime > 0)
                {
                    elapsedTime = elapsedTime + " ( " + TimeStringConvert.getDiffTimeString( time - refTime) + " )";
                }
                area1.setText(elapsedTime);
                area1.invalidate();
                area2.setText(dummy);
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                area4.setText(dummy);
                area4.invalidate();
                area5.setText(dummy);
                area5.invalidate();
                return;
            }

            if (indexSize <= 3)
            {
                // ラップが３つとれた場合
                long time1 = (elapsedTimes.get(indexSize - 2) - elapsedTimes.get(indexSize - 3));
                long refTime1 = (referenceSize >= (indexSize - 1)) ? (referenceTimes.get(indexSize - 2) - referenceTimes.get(indexSize - 3)) : 0;
                long time2 = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
                long refTime2 = (referenceSize >= indexSize) ? (referenceTimes.get(indexSize - 1) - referenceTimes.get(indexSize - 2)) : 0;
                String elapsedTime1 = "[" +  (timerCounter.getElapsedCount() - 2) + "] " + TimeStringConvert.getTimeString(time1);
                if (refTime1 > 0)
                {
                    elapsedTime1 = elapsedTime1 + " ( " + TimeStringConvert.getDiffTimeString( time1 - refTime1) + " )";
                }
                String elapsedTime2 = "[" +  (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time2);
                if (refTime2 > 0)
                {
                    elapsedTime2 = elapsedTime2 + " ( " + TimeStringConvert.getDiffTimeString( time2 - refTime2) + " )";
                }
                area1.setText(elapsedTime1);
                area1.invalidate();
                area2.setText(elapsedTime2);
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                area4.setText(dummy);
                area4.invalidate();
                area5.setText(dummy);
                area5.invalidate();
                return;
            }

            if (indexSize <= 4)
            {
                // ラップが４つとれた場合
                long time1 = (elapsedTimes.get(indexSize - 3) - elapsedTimes.get(indexSize - 4));
                long refTime1 = (referenceSize >= (indexSize - 2)) ? (referenceTimes.get(indexSize - 3) - referenceTimes.get(indexSize - 4)) : 0;

                long time2 = (elapsedTimes.get(indexSize - 2) - elapsedTimes.get(indexSize - 3));
                long refTime2 = (referenceSize >= (indexSize - 1)) ? (referenceTimes.get(indexSize - 2) - referenceTimes.get(indexSize - 3)) : 0;

                long time3 = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
                long refTime3 = (referenceSize >= indexSize) ? (referenceTimes.get(indexSize - 1) - referenceTimes.get(indexSize - 2)) : 0;

                String elapsedTime1 = "[" +  (timerCounter.getElapsedCount() - 3) + "] " + TimeStringConvert.getTimeString(time1);
                if (refTime1 > 0)
                {
                    elapsedTime1 = elapsedTime1 + " ( " + TimeStringConvert.getDiffTimeString( time1 - refTime1) + " )";
                }

                String elapsedTime2 = "[" +  (timerCounter.getElapsedCount() - 2) + "] " + TimeStringConvert.getTimeString(time2);
                if (refTime2 > 0)
                {
                    elapsedTime2 = elapsedTime2 + " ( " + TimeStringConvert.getDiffTimeString( time2 - refTime2) + " )";
                }

                String elapsedTime3 = "[" +  (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time3);
                if (refTime3 > 0)
                {
                    elapsedTime3 = elapsedTime3 + " ( " + TimeStringConvert.getDiffTimeString( time3 - refTime3) + " )";
                }
                area1.setText(elapsedTime1);
                area1.invalidate();
                area2.setText(elapsedTime2);
                area2.invalidate();
                area3.setText(elapsedTime3);
                area3.invalidate();
                area4.setText(dummy);
                area4.invalidate();
                area5.setText(dummy);
                area5.invalidate();
                return;
            }

            if (indexSize <= 5)
            {
                // ラップが５つとれた場合
                long time1 = (elapsedTimes.get(indexSize - 4) - elapsedTimes.get(indexSize - 5));
                long refTime1 = (referenceSize >= (indexSize - 3)) ? (referenceTimes.get(indexSize - 4) - referenceTimes.get(indexSize - 5)) : 0;

                long time2 = (elapsedTimes.get(indexSize - 3) - elapsedTimes.get(indexSize - 4));
                long refTime2 = (referenceSize >= (indexSize - 2)) ? (referenceTimes.get(indexSize - 3) - referenceTimes.get(indexSize - 4)) : 0;

                long time3 = (elapsedTimes.get(indexSize - 2) - elapsedTimes.get(indexSize - 3));
                long refTime3 = (referenceSize >= indexSize) ? (referenceTimes.get(indexSize - 2) - referenceTimes.get(indexSize - 3)) : 0;

                long time4 = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
                long refTime4 = (referenceSize >= indexSize) ? (referenceTimes.get(indexSize - 1) - referenceTimes.get(indexSize - 2)) : 0;

                String elapsedTime1 = "[" +  (timerCounter.getElapsedCount() - 4) + "] " + TimeStringConvert.getTimeString(time1);
                if (refTime1 > 0)
                {
                    elapsedTime1 = elapsedTime1 + " ( " + TimeStringConvert.getDiffTimeString( time1 - refTime1) + " )";
                }

                String elapsedTime2 = "[" +  (timerCounter.getElapsedCount() - 3) + "] " + TimeStringConvert.getTimeString(time2);
                if (refTime2 > 0)
                {
                    elapsedTime2 = elapsedTime2 + " ( " + TimeStringConvert.getDiffTimeString( time2 - refTime2) + " )";
                }

                String elapsedTime3 = "[" +  (timerCounter.getElapsedCount() - 2) + "] " + TimeStringConvert.getTimeString(time3);
                if (refTime3 > 0)
                {
                    elapsedTime3 = elapsedTime3 + " ( " + TimeStringConvert.getDiffTimeString( time3 - refTime3) + " )";
                }

                String elapsedTime4 = "[" +  (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time3);
                if (refTime4 > 0)
                {
                    elapsedTime4 = elapsedTime4 + " ( " + TimeStringConvert.getDiffTimeString( time4 - refTime4) + " )";
                }

                area1.setText(elapsedTime1);
                area1.invalidate();
                area2.setText(elapsedTime2);
                area2.invalidate();
                area3.setText(elapsedTime3);
                area3.invalidate();
                area4.setText(elapsedTime4);
                area4.invalidate();
                area5.setText(dummy);
                area5.invalidate();
                return;
            }

            // ラップが６つ以上ある場合
            long time1 = (elapsedTimes.get(indexSize - 5) - elapsedTimes.get(indexSize - 6));
            long refTime1 = (referenceSize >= (indexSize - 4)) ? (referenceTimes.get(indexSize - 5) - referenceTimes.get(indexSize - 6)) : 0;

            long time2 = (elapsedTimes.get(indexSize - 4) - elapsedTimes.get(indexSize - 5));
            long refTime2 = (referenceSize >= (indexSize - 3)) ? (referenceTimes.get(indexSize - 4) - referenceTimes.get(indexSize - 5)) : 0;

            long time3 = (elapsedTimes.get(indexSize - 3) - elapsedTimes.get(indexSize - 4));
            long refTime3 = (referenceSize >= indexSize - 2) ? (referenceTimes.get(indexSize - 3) - referenceTimes.get(indexSize - 4)) : 0;

            long time4 = (elapsedTimes.get(indexSize - 2) - elapsedTimes.get(indexSize - 3));
            long refTime4 = (referenceSize >= indexSize - 1) ? (referenceTimes.get(indexSize - 2) - referenceTimes.get(indexSize - 3)) : 0;

            long time5 = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
            long refTime5 = (referenceSize >= indexSize) ? (referenceTimes.get(indexSize - 1) - referenceTimes.get(indexSize - 2)) : 0;

            String elapsedTime1 = "[" +  (timerCounter.getElapsedCount() - 5) + "] " + TimeStringConvert.getTimeString(time1);
            if (refTime1 > 0)
            {
                elapsedTime1 = elapsedTime1 + " ( " + TimeStringConvert.getDiffTimeString( time1 - refTime1) + " )";
            }

            String elapsedTime2 = "[" +  (timerCounter.getElapsedCount() - 4) + "] " + TimeStringConvert.getTimeString(time2);
            if (refTime2 > 0)
            {
                elapsedTime2 = elapsedTime2 + " ( " + TimeStringConvert.getDiffTimeString( time2 - refTime2) + " )";
            }

            String elapsedTime3 = "[" +  (timerCounter.getElapsedCount() - 3) + "] " + TimeStringConvert.getTimeString(time3);
            if (refTime3 > 0)
            {
                elapsedTime3 = elapsedTime3 + " ( " + TimeStringConvert.getDiffTimeString( time3 - refTime3) + " )";
            }

            String elapsedTime4 = "[" +  (timerCounter.getElapsedCount() - 2) + "] " + TimeStringConvert.getTimeString(time4);
            if (refTime4 > 0)
            {
                elapsedTime4 = elapsedTime4 + " ( " + TimeStringConvert.getDiffTimeString( time4 - refTime4) + " )";
            }

            String elapsedTime5 = "[" +  (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time5);
            if (refTime5 > 0)
            {
                elapsedTime5 = elapsedTime5 + " ( " + TimeStringConvert.getDiffTimeString( time5 - refTime5) + " )";
            }

            area1.setText(elapsedTime1);
            area1.invalidate();
            area2.setText(elapsedTime2);
            area2.invalidate();
            area3.setText(elapsedTime3);
            area3.invalidate();
            area4.setText(elapsedTime4);
            area4.invalidate();
            area5.setText(elapsedTime5);
            area5.invalidate();
        }
*/
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

    /*
     *
     *
     */
    @Override
    protected void onUserLeaveHint ()
    {
        Log.v(TAG, "onUserLeaveHint() " );
        // ハードキー（ホームボタン）が押されたとき、これがひろえるが...
    }

    /*
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
    public void counterStatusChanged(boolean forceStartTimer)
    {
        if (forceStartTimer)
        {
            try
            {
                ITimerCounter timerCounter = counter;
                if (timerCounter != null)
                {
                    MyTimerTrigger trigger = new MyTimerTrigger(this, 100, timerCounter);
                    trigger.startTimer();
                    stopTrigger = trigger;
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
                updateTimerLabel();
            }
        });
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

/*
            TextView area1 = findViewById(R.id.sub_counter2);
            TextView area2 = findViewById(R.id.sub_counter3);
            TextView area3 = findViewById(R.id.sub_counter4);
            ScrollView scr = findViewById(R.id.scroll_area);
            LinearLayout lap = findViewById(R.id.lap_time_area);
*/
            if (isGraphics)
            {
                graphView.setITimerCounter(counter);
                graphView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
/*
                scr.setVisibility(View.GONE);
                lap.setVisibility(View.GONE);
                area1.setVisibility(View.GONE);
                area2.setVisibility(View.GONE);
                area3.setVisibility(View.GONE);
*/
            }
            else
            {
                graphView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
/*
                scr.setVisibility(View.VISIBLE);
                lap.setVisibility(View.VISIBLE);
                area1.setVisibility(View.VISIBLE);
                area2.setVisibility(View.VISIBLE);
                area3.setVisibility(View.VISIBLE);
*/
            }
            controller.vibrate(30);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
     *
     *
     */
/*
    private String getElapsedTime()
    {
        String elapsedTime = "";
        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            int count = timerCounter.getElapsedCount();
            if (count > 0)
            {
                elapsedTime = "[" + timerCounter.getElapsedCount() + "] " + MyTimerCounter.getTimeString(timerCounter.getCurrentElapsedTime());
            }
        }
        return (elapsedTime);
    }
*/
}
