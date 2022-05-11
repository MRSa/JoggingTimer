package net.osdn.gokigen.joggingtimer.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

/**
 *
 *
 *
 */
public interface IWearableActivityControl
{
    void setup(AppCompatActivity activity, IClickCallback callback, IDatabaseReloadCallback dbCallback);
    void setupDatabase(AppCompatActivity activity, boolean isReset);
    void exitApplication(AppCompatActivity activity);

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
