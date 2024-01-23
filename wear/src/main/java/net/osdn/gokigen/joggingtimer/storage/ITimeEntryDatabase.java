package net.osdn.gokigen.joggingtimer.storage;

import android.database.Cursor;

import java.util.ArrayList;

/**
 *    ITimeEntryDatabase
 *
 */
public interface ITimeEntryDatabase
{
    long DONT_USE_ID = -1;
    long DEFAULT_RECORD_TYPE = 0;
    long EDITABLE_RECORD_TYPE = 1;
    long PASSAGE_RECORD_TYPE = 2;

    void prepare();
    void close();

    Cursor getAllIndexData();
    Cursor getAllDetailData(long indexId);
    Cursor getAllReferenceDetailData(int id);

    Cursor getIndexdata(long indexId);

    void deleteTimeEntryData(long indexId);

    void setReferenceIndexData(int id, long indexId);
    void updateIndexData(long indexId, String title, int icon);
    void createIndexData(String title, String memo, int icon, long startTime);
    void appendTimeData(long indexId, long lapTime);
    void finishTimeData(long indexId, long startTime, long endTime);

    long createTimeEntryModelData(int lap, long totalTime, String memo);

    long createImportedTimeEntryData(String title, String memo, long totalTime, ArrayList<Long> lapTimeList);

    int updateTimeEntryData(long detailId, long totalTime);
}
