package net.osdn.gokigen.joggingtimer.storage;

import android.database.Cursor;

/**
 *    ITimeEntryDatabase
 *
 */
public interface ITimeEntryDatabase
{
    static final long DEFAULT_RECORD_TYPE = 0;
    static final long EDITABLE_RECORD_TYPE = 1;

    void prepare();
    void close();

    Cursor getAllIndexData();
    Cursor getAllDetailData(long indexId);
    Cursor getAllReferenceDetailData();

    void deleteTimeEntryData(long indexId);

    void setReferenceIndexData(long indexId);
    void updateIndexData(long indexId, String title, int icon);
    void createIndexData(String title, String memo, int icon, long startTime);
    void appendTimeData(long indexId, long lapTime);
    void finishTimeData(long indexId, long startTime, long endTime);

    void createTimeEntryModelData(int lap, int hour, int minute, int second, String memo);

}
