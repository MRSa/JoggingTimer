package net.osdn.gokigen.joggingtimer;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

/**
 *
 */
public class MainActivity extends WearableActivity
{
    private final String TAG = toString();
    private final IWearableActivityControl controller = new WearableActivityController();

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        controller.setup(this);

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

}
