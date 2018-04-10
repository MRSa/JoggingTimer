package net.osdn.gokigen.joggingtimer.storage;

import android.database.Cursor;

/**
 *    ITimeEntryDatabase
 *
 */
public interface ITimeEntryDatabase
{
    long DONT_USE_ID = -1;
    long DEFAULT_RECORD_TYPE = 0;
    long EDITABLE_RECORD_TYPE = 1;

    void prepare();
    void close();

    Cursor getAllIndexData();
    Cursor getAllDetailData(long indexId);
    Cursor getAllReferenceDetailData();

    Cursor getIndexdata(long indexId);

    void deleteTimeEntryData(long indexId);

    void setReferenceIndexData(long indexId);
    void updateIndexData(long indexId, String title, int icon);
    void createIndexData(String title, String memo, int icon, long startTime);
    void appendTimeData(long indexId, long lapTime);
    void finishTimeData(long indexId, long startTime, long endTime);

    long createTimeEntryModelData(int lap, long totalTime, String memo);
    int updateTimeEntryData(long detailId, long totalTime);
}
