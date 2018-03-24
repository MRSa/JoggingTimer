package net.osdn.gokigen.joggingtimer.stopwatch;

interface IDataEntry
{
    void createIndex(String title, long startTime);
    void appendTimeData(long elapsedTime);
    void finishTimeData(long startTime, long endTime);
}
