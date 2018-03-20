package net.osdn.gokigen.joggingtimer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex;

import static android.provider.BaseColumns._ID;

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
    private static final int REFERENCE_ICON_ID = 2;
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
        return (db.rawQuery("SELECT * FROM " + TimeEntryIndex.EntryIndex.TABLE_NAME + " ORDER BY " + TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME, null));
    }

    @Override
    public Cursor getAllDetailData(long indexId)
    {
        return (db.rawQuery("SELECT * FROM " + TimeEntryData.EntryData.TABLE_NAME + " WHERE " + TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID + " = " + indexId + " ORDER BY " + TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY, null));
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
    public void setReferenceIndexData(long indexId)
    {
        try
        {
            ContentValues clearValues = new ContentValues();
            clearValues.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, DEFAULT_ICON_ID);
            db.update(TimeEntryIndex.EntryIndex.TABLE_NAME, clearValues, TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID + " = " + REFERENCE_ICON_ID, null);

            ContentValues referenceValues = new ContentValues();
            referenceValues.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, REFERENCE_ICON_ID);
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
        Log.v(TAG, "createIndexData() [" + title +"] " + memo + " " + icon + " " + startTime);
        try
        {
            boolean ret = false;
            ContentValues valuesIndex = new ContentValues();
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE, title);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_MEMO, memo);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID, icon);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME, startTime);
            valuesIndex.put(TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION, 0);
            long indexId = db.insert(TimeEntryIndex.EntryIndex.TABLE_NAME, null, valuesIndex);
            if (indexId != -1)
            {
                ret = true;
            }
            callback.dataEntryFinished(ITimeEntryDatabaseCallback.OperationType.CREATED, ret, indexId, title);

            ContentValues valuesData = new ContentValues();
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID, indexId);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY, startTime);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_OTHER_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_GPS_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_MEMO_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_ICON_ID, 0);
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE, 0);
            long dataId = db.insert(TimeEntryData.EntryData.TABLE_NAME, null, valuesData);
            if (dataId != -1)
            {
                ret = true;
            }
            callback.timeEntryFinished(ITimeEntryDatabaseCallback.OperationType.APPENDED, ret, indexId, dataId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void appendTimeData(long indexId, long lapTime)
    {
        Log.v(TAG, "appendTimeData()  " +  lapTime);
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
            valuesData.put(TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE, 0);
            long dataId = db.insert(TimeEntryData.EntryData.TABLE_NAME, null, valuesData);
            if (dataId != -1)
            {
                ret = true;
            }
            callback.timeEntryFinished(ITimeEntryDatabaseCallback.OperationType.APPENDED, ret, indexId, dataId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void finishTimeData(long indexId, long startTime, long endTime)
    {
        Log.v(TAG, "finishTimeData()  " +  endTime);
        try
        {
            boolean ret = false;
            appendTimeData(indexId, endTime);
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
