package net.osdn.gokigen.joggingtimer;

import java.util.List;

public interface ITimerCounter
{
    boolean isStarted();
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
