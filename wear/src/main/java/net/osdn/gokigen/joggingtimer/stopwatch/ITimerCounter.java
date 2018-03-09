package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.List;

public interface ITimerCounter
{
    boolean isStarted();
    boolean isReset();
    void start();
    void stop();
    void timeStamp();
    void reset();

    List<Long> getTimerList();
    int getElapsedCount();
    long getPastTime();
    long getLastElapsedTime();
    long getCurrentElapsedTime();

}
