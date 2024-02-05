package net.osdn.gokigen.joggingtimer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex;

import static android.provider.BaseColumns._ID;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 *
 *
 *
 */
class TimeEntryDatabase implements ITimeEntryDatabase
{
    private final String TAG = toString();
    private final TimeEntryDataOpenHelper dbHelper;
    private final ITimeEntryDatabaseCallback callback;
    private static final int REFERENCE_ICON_ID_A = 2;
    private static final int REFERENCE_ICON_ID_B = 1;
    private static final int REFERENCE_ICON_ID_C = 3;
    private static final int MODEL_DATA_ICON_ID = 4;
    private static final int DEFAULT_ICON_ID = 0;

    private SQLiteDatabase db = null;
    //private SQLiteDatabase writeDb = null;
    //private SQLiteDatabase readDb = null;

    TimeEntryDatabase(Context context, @NonNull ITimeEntryDatabaseCallback callback)
    {
        dbHelper = new TimeEntryDataOpenHelper(context);
        this.callback = callback;
    }

    @Override
    public void prepare()
    {
        Log.v(TAG, "prepareToWrite() ");
        boolean ret = false;
        try
        {
            // Gets the data repository in write mode
            db = dbHelper.getWritableDatabase();
            ret = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            db = null;
        }
        callback.prepareFinished(ret);
    }

    @Override
    public void close()
    {
        Log.v(TAG, "close()");
        try
        {
            db.close();
            db = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Cursor getAllIndexData()
    {
        return (db.rawQuery("SELECT * FROM " + TimeEntryIndex.EntryIndex.TABLE_NAME + " ORDER BY " + TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME + " DESC", null));
    }

    @Override
    public Cursor getAllDetailData(long indexId)
    {
        return (db.rawQuery("SELECT * FROM " + TimeEntryData.EntryData.TABLE_NAME + " WHERE " + TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID + " = " + indexId + " ORDER BY " + TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY, null));
    }

    @Override
    public Cursor getAllReferenceDetailData(int id)
    {
        if (db == null)
        {
            return (null);
        }
        int iconId = getReferenceIconId(id);
        String queryString = "SELECT * FROM " + TimeEntryIndex.EntryIndex.TABLE_NAME + " INNER JOIN " + TimeEntryData.EntryData.TABLE_NAME +
                " ON " + TimeEntryIndex.EntryIndex.TABLE_NAME + "." + TimeEntryIndex.EntryIndex._ID + " = " + TimeEntryData.EntryData.TABLE_NAME+ "." + TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID +
                " WHERE " + TimeEntryIndex.EntryIndex.TABLE_NAME+ "." + TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID + " = " + iconId +
                " ORDER BY " + TimeEntryData.EntryData.TABLE_NAME+ "." + TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY;
        // Log.v(TAG, "Query : " + queryString);
        return (db.rawQuery(queryString, null));
    }

    @Override
    public Cursor getIndexdata(long indexId)
    {
        return (db.rawQuery("SELECT * FROM " + TimeEntryIndex.EntryIndex.TABLE_NAME + " WHERE " + TimeEntryIndex.EntryIndex._ID + " = " + indexId + " ORDER BY " + TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME, null));
    }

    @Override
    public void deleteTimeEntryData(long indexId)
    {
        int delRecord = db.delete(TimeEntryData.EntryData.TABLE_NAME, TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID + " = " + indexId, null);
        int delIndex = db.delete(TimeEntryIndex.EntryIndex.TABLE_NAME, _ID + " = " + indexId, null);

        Log.v(TAG, "deleteTimeEntryData()  index : " + indexId + "Recs. [" + delIndex + "] (" + delRecord + ")");
    }

    /**
     *
     *
     */
    @Override
    public void updateIndexData(long indexId, String title, int icon)
    {
        try
        {
            if ((title != null)&&(!title.isEmpty()))
            {
                ContentValues titleValues = new ContentValues();
                titleValues.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE, title);
                db.update(TimeEntryIndex.EntryIndex.TABLE_NAME, titleValues, _ID + " = " + indexId, null);
            }

            ContentValues iconValues = new ContentValues();
            iconValues.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, icon);
            db.update(TimeEntryIndex.EntryIndex.TABLE_NAME, iconValues, _ID + " = " + indexId, null);
            //Log.v(TAG, "updateIndexData(" + indexId + ", " + title + ", " + icon + ")");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    @Override
    public void setReferenceIndexData(int id, long indexId)
    {
        try
        {
            int iconId = getReferenceIconId(id);
            ContentValues clearValues = new ContentValues();
            clearValues.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, DEFAULT_ICON_ID);
            db.update(TimeEntryIndex.EntryIndex.TABLE_NAME, clearValues, TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID + " = " + iconId, null);

            ContentValues referenceValues = new ContentValues();
            referenceValues.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, iconId);
            db.update(TimeEntryIndex.EntryIndex.TABLE_NAME, referenceValues, _ID + " = " + indexId, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void createIndexData(String title, String memo, int icon, long startTime)
    {
        long indexId = createIndexDataImpl(true, title, memo, icon, startTime, 0);
        Log.v(TAG, "createIndexData() [" + title +"] " + memo + " " + icon + " " + startTime + " idx: " + indexId);
    }

    private long createIndexDataImpl(boolean isCallback, String title, String memo, int icon, long startTime, long duration)
    {
        long indexId = -1;
        try
        {
            boolean ret = false;
            ContentValues valuesIndex = new ContentValues();
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE, title);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_MEMO, memo);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, icon);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME, startTime);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION, duration);
            indexId = db.insert(TimeEntryIndex.EntryIndex.TABLE_NAME, null, valuesIndex);
            if (indexId != -1)
            {
                ret = true;
            }
            if (isCallback)
            {
                callback.dataEntryFinished(ITimeEntryDatabaseCallback.OperationType.CREATED, ret, indexId, title);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        appendTimeDataImpl(true, indexId, startTime, DEFAULT_RECORD_TYPE);
        return (indexId);
    }

    @Override
    public void appendTimeData(long indexId, long lapTime, long recordType)
    {
        Log.v(TAG, "appendTimeData()  [" +  lapTime + "] " + recordType);
        appendTimeDataImpl(true, indexId, lapTime, recordType);
    }

    /**
     *
     *
     */
    private void appendTimeDataImpl(boolean isCallback, long indexId, long lapTime, long recordType)
    {
        try
        {
            boolean ret = false;
            ContentValues valuesData = new ContentValues();
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID, indexId);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY, lapTime);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_OTHER_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_GPS_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_MEMO_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_ICON_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE, recordType);
            long dataId = db.insert(TimeEntryData.EntryData.TABLE_NAME, null, valuesData);
            if (dataId != -1)
            {
                ret = true;
            }
            if (isCallback)
            {
                callback.timeEntryFinished(ITimeEntryDatabaseCallback.OperationType.APPENDED, ret, indexId, dataId);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *
     *
     */
    @Override
    public void finishTimeData(long indexId, long startTime, long endTime)
    {
        Log.v(TAG, "finishTimeData()  " +  endTime);
        try
        {
            boolean ret = false;
            appendTimeData(indexId, endTime, DEFAULT_RECORD_TYPE);
            long elapsedTime = endTime - startTime;
            if (elapsedTime < 0)
            {
                elapsedTime = 0;
            }
            ContentValues valuesIndex = new ContentValues();
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION, elapsedTime);
            int rows = db.update(TimeEntryIndex.EntryIndex.TABLE_NAME, valuesIndex, "_id=" + indexId, null);
            if (rows > 0)
            {
                ret = true;
            }
            callback.dataEntryFinished(ITimeEntryDatabaseCallback.OperationType.FINISHED, ret, indexId, "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    @Override
    public long createTimeEntryModelData(int lap, long totalTime, @NonNull String memo)
    {
        long diffTime = totalTime / (long) lap;
        String title = " " + lap + " LAPs Model";
        Log.v(TAG, "ENTRY : '" + lap + " " + title + "' mills : " + "(" + diffTime + ") " + memo);

        try
        {
            long lapTime = 0;
            long indexId = createIndexDataImpl(false, title, memo, MODEL_DATA_ICON_ID, lapTime, totalTime);
            for (int index = 0; index < lap; index++)
            {
                lapTime = lapTime + diffTime;
                appendTimeDataImpl(false, indexId, lapTime, EDITABLE_RECORD_TYPE);
            }
            callback.modelDataEntryFinished(ITimeEntryDatabaseCallback.OperationType.FINISHED, true, indexId, title);
            return (indexId);
       }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        callback.modelDataEntryFinished(ITimeEntryDatabaseCallback.OperationType.FINISHED, false, -1, title);
        return (-1);
    }

    @Override
    public long createImportedTimeEntryData(String title, String memo, long totalTime, ArrayList<Long> lapTimeList)
    {
        try
        {
            long lapTime = 0;
            long indexId = createIndexDataImpl(false, title, memo, MODEL_DATA_ICON_ID, 0, totalTime);
            for (long currentTime :lapTimeList)
            {
                lapTime = lapTime + currentTime;
                appendTimeDataImpl(false, indexId, lapTime, EDITABLE_RECORD_TYPE);
            }
            callback.modelDataEntryFinished(ITimeEntryDatabaseCallback.OperationType.FINISHED, true, indexId, title);
            return (indexId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        callback.modelDataEntryFinished(ITimeEntryDatabaseCallback.OperationType.FINISHED, false, -1, title);
        return (-1);
    }

    /**
     *
     *
     */
    @Override
    public int  updateTimeEntryData(long detailId, long totalTime)
    {
        int rows = 0;
        Log.v(TAG, "updateTimeEntryData (" + detailId + ") : " + totalTime);
        try
        {
            ContentValues timeValue = new ContentValues();
            timeValue.put(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY, totalTime);
           rows =  db.update(TimeEntryData.EntryData.TABLE_NAME, timeValue, _ID + " = " + detailId, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (rows);
     }

     private int getReferenceIconId(int id)
     {
         if (id == 0)
         {
             return (REFERENCE_ICON_ID_A);
         }
         else if (id == 1)
         {
             return (REFERENCE_ICON_ID_B);
         }
         return (REFERENCE_ICON_ID_C);
     }
/*
    public boolean prepareToRead()
    {
        Log.v(TAG, "prepareToRead() ");
        boolean ret = false;
        try
        {
            // Gets the data repository in read mode
            readDb = dbHelper.getReadableDatabase();
            ret = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (ret);
    }

    public boolean prepareToWrite()
    {
        Log.v(TAG, "prepareToWrite() ");
        boolean ret = false;
        try
        {
            // Gets the data repository in write mode
            writeDb = dbHelper.getWritableDatabase();
            ret = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (ret);
    }
*/

}
