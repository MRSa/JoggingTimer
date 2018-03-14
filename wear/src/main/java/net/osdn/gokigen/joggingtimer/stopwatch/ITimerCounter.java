package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.ArrayList;
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
    int getElapsedCount();
    long getPastTime();
    long getLastElapsedTime();
    long getCurrentElapsedTime();

    long getStartTime();
    long getStopTime();

    void reloadTimerCounter(long startTime, ArrayList<Long> timelist);
}
