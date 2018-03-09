package net.osdn.gokigen.joggingtimer.stopwatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 *
 *
 *
 */
class WearableActivityController implements IWearableActivityControl
{
    private final String TAG = toString();
    private final ButtonClickListener clickListener = new ButtonClickListener();
    private Vibrator vibrator = null;
    //private PowerManager powerManager = null;


    WearableActivityController()
    {
        Log.v(TAG, "WearableActivityController()");
    }

    @Override
    public void setup(WearableActivity activity, IClickCallback callback)
    {
        setupPermissions(activity);
        setupHardwares(activity);
        setupScreen(activity);
        setupListeners(activity, callback);
    }


    /**
     *
     *
     */
    private void setupPermissions(WearableActivity activity)
    {
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.VIBRATE,
                            Manifest.permission.WAKE_LOCK,
                    },
                    100);
        }
    }

    /**
     *
     *
     */
    private void setupHardwares(WearableActivity activity)
    {
        // バイブレータをつかまえる
        vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);

        // パワーマネージャをつかまえる
        // powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
    }

    /**
     *
     *
     */
    private void setupScreen(WearableActivity activity)
    {
        TextView mTextView = activity.findViewById(R.id.text);
        if (mTextView != null)
        {
            mTextView.setText(R.string.app_name);
        }
    }

    /**
     *   リスナのセットアップ
     *
     */
    private void setupListeners(WearableActivity activity, IClickCallback callback)
    {
        try
        {
            clickListener.setCallback(callback);
            ImageButton btn1 = activity.findViewById(R.id.btn1);
            btn1.setOnClickListener(clickListener);
            btn1.setOnLongClickListener(clickListener);

            ImageButton btn2 = activity.findViewById(R.id.btn2);
            btn2.setOnClickListener(clickListener);
            btn2.setOnLongClickListener(clickListener);

            ImageButton btn3 = activity.findViewById(R.id.btn3);
            btn3.setOnClickListener(clickListener);
            btn3.setOnLongClickListener(clickListener);

            TextView main = activity.findViewById(R.id.main_counter);
            main.setOnClickListener(clickListener);
            main.setOnLongClickListener(clickListener);
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
    public void exitApplication(WearableActivity activity)
    {
        Log.v(TAG, "exitApplication()");

        //finish();
        //finishAndRemoveTask();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void vibrate(final int duration)
    {
        try
        {
            if ((vibrator == null)||(!vibrator.hasVibrator()))
            {
                return;
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    vibrator.vibrate(duration);
                }
            });
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
