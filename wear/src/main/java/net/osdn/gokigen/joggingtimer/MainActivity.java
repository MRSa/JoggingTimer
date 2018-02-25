package net.osdn.gokigen.joggingtimer;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 *
 *
 */
public class MainActivity extends WearableActivity implements IClickCallback, MyTimerTrigger.ITimeoutReceiver
{
    private final String TAG = toString();
    private final IWearableActivityControl controller = new WearableActivityController();
    private MyTimerCounter counter = new MyTimerCounter();

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
        Log.v(TAG, "onResume()");
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
            TextView main = findViewById(R.id.main_counter);
            ImageButton btn1 = findViewById(R.id.btn1);
            ImageButton btn2 = findViewById(R.id.btn2);
            ImageButton btn3 = findViewById(R.id.btn3);
            if (timerCounter.isStarted())
            {
                btn1.setImageResource(R.drawable.ic_flag_black_24dp);
                btn1.setVisibility(View.VISIBLE);
                btn1.invalidate();

                btn2.setImageResource(R.drawable.ic_stop_black_24dp);
                btn2.setVisibility(View.VISIBLE);
                btn2.invalidate();

                btn3.setImageResource(R.drawable.ic_block_black_24dp);
                btn3.setVisibility(View.INVISIBLE);
                btn3.invalidate();

                main.setText(MyTimerCounter.getTimeString(timerCounter.getPastTime()));
                main.invalidate();

                updateElapsedTimes();
            }
            else
            {
                btn1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                btn1.setVisibility(View.VISIBLE);
                btn1.invalidate();

                btn2.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp);
                btn2.setVisibility(View.VISIBLE);
                btn2.invalidate();

                btn3.setImageResource(R.drawable.ic_refresh_black_24dp);
                btn3.setVisibility(View.VISIBLE);
                btn3.invalidate();

                main.setText(MyTimerCounter.getTimeString(timerCounter.getPastTime()));
                main.invalidate();

                updateElapsedTimes();
            }
        }
    }

    /**
     *
     */
    @Override
    public void clickedBtn1()
    {
        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            if (timerCounter.isStarted())
            {
                timerCounter.timeStamp();
            }
            else
            {
                timerCounter.start();
                MyTimerTrigger trigger = new MyTimerTrigger(this, 100, timerCounter);
                trigger.startTimer();
            }
            updateTimerLabel();
        }
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
            if (timerCounter.isStarted())
            {
                timerCounter.stop();
            }
            updateTimerLabel();
        }
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
            }
            updateTimerLabel();
        }
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

    private void updateElapsedTimes()
    {
        String dummy = "";
        TextView area1 = findViewById(R.id.sub_counter1);
        TextView area2 = findViewById(R.id.sub_counter2);
        TextView area3 = findViewById(R.id.sub_counter3);

        ITimerCounter timerCounter = counter;
        if (timerCounter != null)
        {
            List<Long> elapsedTimes = timerCounter.getTimerList();
            if (elapsedTimes.size() <= 1)
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
            if (elapsedTimes.size() <= 2)
            {
                // ラップが２つとれた場合
                long time = (elapsedTimes.get(elapsedTimes.size() - 1) - elapsedTimes.get(elapsedTimes.size() - 2));
                String elapsedTime = "[" + (timerCounter.getElapsedCount() - 1) + "] " + MyTimerCounter.getTimeString(time);
                area1.setText(elapsedTime);
                area1.invalidate();
                area2.setText(getElapsedTime());
                area2.invalidate();
                area3.setText(dummy);
                area3.invalidate();
                return;
            }

            // ラップが３つ以上ある場合
            long time1 = (elapsedTimes.get(elapsedTimes.size() - 2) - elapsedTimes.get(elapsedTimes.size() - 3));
            long time2 = (elapsedTimes.get(elapsedTimes.size() - 1) - elapsedTimes.get(elapsedTimes.size() - 2));
            String elapsedTime1 = "[" + (timerCounter.getElapsedCount() - 2) + "] " + MyTimerCounter.getTimeString(time1);
            String elapsedTime2 = "[" + (timerCounter.getElapsedCount() - 1) + "] " + MyTimerCounter.getTimeString(time2);
            area1.setText(elapsedTime1);
            area1.invalidate();
            area2.setText(elapsedTime2);
            area2.invalidate();
            area3.setText(getElapsedTime());
            area3.invalidate();
        }
    }


    /**
     *
     *
     */
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

}
