package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.List;

public interface ITimerCounter
{
    boolean isStarted();
    boolean isReset();
    void start();
    void stop();
    long timeStamp();
    void reset();

    int getElapsedCount();
    long getPastTime();
    long getElapsedTime(int lapCount);
    long getLastElapsedTime();
    long getCurrentElapsedTime();

    long getStartTime();
    long getStopTime();

    List<Long> getLapTimeList();
    List<Long> getReferenceLapTimeList();
    long getReferenceLapTime(int position);
    void selectReferenceLapTime(int id);

    void setCallback(MyTimerCounter.ICounterStatusNotify callback);
}
