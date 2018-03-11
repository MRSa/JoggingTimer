package net.osdn.gokigen.joggingtimer.stopwatch;

interface IDataEntry
{
    //void entryData(String title, String memo, int icon, long startTime, long endTime, List<Long> elapsedTime);

    void createIndex(String title, String memo, int icon, long startTime);
    void appendTimeData(long elapsedTime);
    void finishTimeData(long startTime, long endTime);

    //void updateMemoData(String memo);
    //void updateIconData(int icon);
}
