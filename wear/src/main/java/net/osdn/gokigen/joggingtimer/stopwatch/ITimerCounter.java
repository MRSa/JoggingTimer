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

    List<Long> getTimerList();
    List<Long> getReferenceTimeList();
    int getElapsedCount();
    long getPastTime();
    long getLastElapsedTime();
    long getCurrentElapsedTime();

    long getStartTime();
    long getStopTime();

    void setCallback(MyTimerCounter.ICounterStatusNotify callback);
}
