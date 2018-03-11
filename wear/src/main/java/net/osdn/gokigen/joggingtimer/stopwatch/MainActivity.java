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
import android.widget.RelativeLayout;
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
public class MainActivity extends WearableActivity implements IClickCallback, MyTimerTrigger.ITimeoutReceiver
{
    private final String TAG = toString();
    private final IWearableActivityControl controller = new WearableActivityController();
    private MyTimerCounter counter = new MyTimerCounter();
    private boolean isCounterLapTime = false;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        controller.setup(this, this);

        // Enables Always-on
        setAmbientEnabled();
    }

    /**
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        /* ここで状態を保存 */
        outState.putParcelable("timerCounter", counter);
    }

    /**
     *
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        /* ここで保存した状態を読み出して設定 */
        counter = savedInstanceState.getParcelable("timerCounter");
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
        if (isStartTimer)
        {
            // start a timer!
            startTimer();
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
        updateTimerLabel();
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
    public void updateTimerLabel()
    {
        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
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

                updateElapsedTimes();
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

                updateElapsedTimes();
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

                updateElapsedTimes();
            }
        }
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
                    controller.vibrate(120);

                    Date date = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String title = sdf1.format(date);
                    controller.getDataEntry().createIndex(title, "", 0, timerCounter.getStartTime());
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
        String dummy = "";
        TextView area1 = findViewById(R.id.sub_counter2);
        TextView area2 = findViewById(R.id.sub_counter3);
        TextView area3 = findViewById(R.id.sub_counter4);

        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            List<Long> elapsedTimes = timerCounter.getTimerList();
            int indexSize = elapsedTimes.size();
            if (indexSize <= 1)
            {
                // ラップの記録がないので表示しません
                area1.setText(dummy);
                area1.invalidate();
                area2.setText(dummy);
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                return;
            }
            if (indexSize <= 2)
            {
                // ラップが１つとれた場合
                long time = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
                String elapsedTime = "[" + (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time);
                area1.setText(elapsedTime);
                area1.invalidate();
                area2.setText(dummy);
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                return;
            }
            if (indexSize <= 3)
            {
                // ラップが３つとれた場合
                long time1 = (elapsedTimes.get(indexSize - 2) - elapsedTimes.get(indexSize - 3));
                long time2 = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
                String elapsedTime1 = "[" +  (timerCounter.getElapsedCount() - 2) + "] " + TimeStringConvert.getTimeString(time1);
                String elapsedTime2 = "[" +  (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time2);
                area1.setText(elapsedTime1);
                area1.invalidate();
                area2.setText(elapsedTime2);
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                return;
            }

            // ラップが４つ以上ある場合
            long time1 = (elapsedTimes.get(indexSize - 3) - elapsedTimes.get(indexSize - 4));
            long time2 = (elapsedTimes.get(indexSize - 2) - elapsedTimes.get(indexSize - 3));
            long time3 = (elapsedTimes.get(indexSize - 1) - elapsedTimes.get(indexSize - 2));
            String elapsedTime1 = "[" +  (timerCounter.getElapsedCount() - 3) + "] " + TimeStringConvert.getTimeString(time1);
            String elapsedTime2 = "[" +  (timerCounter.getElapsedCount() - 2) + "] " + TimeStringConvert.getTimeString(time2);
            String elapsedTime3 = "[" +  (timerCounter.getElapsedCount() - 1) + "] " + TimeStringConvert.getTimeString(time3);
            area1.setText(elapsedTime1);
            area1.invalidate();
            area2.setText(elapsedTime2);
            area2.invalidate();
            area3.setText(elapsedTime3);
            area3.invalidate();
        }
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
    public boolean dispatchKeyEvent(KeyEvent e)
    {
        Log.v(TAG, "dispatchKeyEvent() : " + e.getAction() + " " + e.getKeyCode());

        // DOWNとUPが取得できるのでログの2重表示防止のため
        if (e.getAction() == KeyEvent.ACTION_DOWN)
        {
            //キーコード表示
            Log.d("KeyCode","KeyCode:"+ e.getKeyCode());
        }
        return (super.dispatchKeyEvent(e));
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
