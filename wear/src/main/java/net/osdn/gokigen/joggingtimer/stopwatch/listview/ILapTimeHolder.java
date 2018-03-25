package net.osdn.gokigen.joggingtimer.stopwatch.listview;

public interface ILapTimeHolder
{
    int getLapTimeCount();
    void clearLapTime();
    void addLapTime(LapTimeItems item);
}
