package net.osdn.gokigen.joggingtimer;

import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 *
 *
 *
 */
class WearableActivityController implements IWearableActivityControl
{
    private final String TAG = toString();
    private final ButtonClickListener clickListener = new ButtonClickListener();

    WearableActivityController()
    {
        Log.v(TAG, "WearableActivityController()");
    }

    @Override
    public void setup(WearableActivity activity, IClickCallback callback)
    {
        setupPermissions();
        setupHardwares();
        setupScreen(activity);
        setupListeners(activity, callback);
    }


    /**
     *
     *
     */
    private void setupPermissions()
    {
/*
        // Set up permissions
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.CHANGE_NETWORK_STATE,
                            Manifest.permission.WRITE_SETTINGS,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.INTERNET,
                    },
                    REQUEST_NEED_PERMISSIONS);
        }
*/

    }

    /**
     *
     *
     */
    private void setupHardwares()
    {


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
}
