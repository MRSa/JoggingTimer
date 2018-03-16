package net.osdn.gokigen.joggingtimer.stopwatch;

import android.support.wearable.activity.WearableActivity;

/**
 *
 *
 *
 */
public interface IWearableActivityControl
{
    void setup(WearableActivity activity, IClickCallback callback);
    void setupDatabase(WearableActivity activity, boolean isReset);
    void exitApplication(WearableActivity activity);

    void vibrate(int duration);

    IDataEntry getDataEntry();

    void timerStarted(boolean isStarted);
}
