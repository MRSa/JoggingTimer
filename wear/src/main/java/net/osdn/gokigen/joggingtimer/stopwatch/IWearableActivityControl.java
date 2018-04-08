package net.osdn.gokigen.joggingtimer.stopwatch;

import android.support.wearable.activity.WearableActivity;

/**
 *
 *
 *
 */
public interface IWearableActivityControl
{
    void setup(WearableActivity activity, IClickCallback callback, IDatabaseReloadCallback dbCallback);
    void setupDatabase(WearableActivity activity, boolean isReset);
    void exitApplication(WearableActivity activity);

    void vibrate(int duration);

    IDataEntry getDataEntry();

    boolean getDisplayMode();
    void timerStarted(boolean isStarted);
    void setDisplayMode(boolean displayLapTime);
    void addTimeStamp(long count, long lapTime, long diffTime);
    void clearTimeStamp();
    int getLapTimeCount();

    void setupReferenceData();
}
