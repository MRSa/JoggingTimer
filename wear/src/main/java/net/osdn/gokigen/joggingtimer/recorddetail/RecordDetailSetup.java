package net.osdn.gokigen.joggingtimer.recorddetail;

import android.database.Cursor;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback;
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelDataDialog;
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

import static android.provider.BaseColumns._ID;

/**
 *
 *
 */
public class RecordDetailSetup  implements ITimeEntryDatabaseCallback
{
    private final String TAG = toString();
    private final WearableActivity activity;
    private final long indexId;
    private final IDatabaseReadyNotify callback;
    private final IRecordOperation operation;
    private ITimeEntryDatabase database = null;

    /**
     *
     *
     */
    RecordDetailSetup(WearableActivity activity, long indexId, IDatabaseReadyNotify callback, IRecordOperation operation)
    {
        this.activity = activity;
        this.indexId = indexId;
        this.callback = callback;
        this.operation = operation;
    }

    /**
     *
     *
     */
    void setup()
    {
        Log.v(TAG, "setup()");
        database = new TimeEntryDatabaseFactory(activity, this).getEntryDatabase();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    database.prepare();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     *
     *
     */
    @Override
    public void prepareFinished(boolean isReady)
    {
        if (!isReady)
        {
            callback.databaseSetupFinished(false);
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                boolean ret = false;
                try
                {
                    operation.clearRecord();
                    Cursor cursor = database.getAllDetailData(indexId);
                    int index = 0;
                    long startTime = 0;
                    long previousLapTime = 0;
                    long morePreviousTime = 0;
                    while (cursor.moveToNext())
                    {
                        long dataId = cursor.getLong(cursor.getColumnIndex(_ID));
                        long entryTime = cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY));
                        int recordType = cursor.getInt(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE));

                        if (index == 0)
                        {
                            // first record
                            startTime = entryTime;
                            previousLapTime = entryTime;
                            morePreviousTime = entryTime;
                        }
                        else
                        {
                            long lapTime = entryTime - previousLapTime;
                            long overallTime = entryTime - startTime;
                            long differenceTime = (lapTime) - (previousLapTime - morePreviousTime);
                            String lapCount = " " + index;
                            String lapTimeString = TimeStringConvert.getTimeString(lapTime).toString();
                            String overallTimeString = TimeStringConvert.getTimeString(overallTime).toString() + " (" + TimeStringConvert.getDiffTimeString(differenceTime).toString() +") ";
                            operation.addRecord(new DetailRecord(dataId, recordType, lapCount, lapTimeString, overallTimeString));
                            morePreviousTime = previousLapTime;
                            previousLapTime = entryTime;
                        }
                        index++;
                    }
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            operation.dataSetChangeFinished();
                        }
                    });
                    ret = true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                callback.databaseSetupFinished(ret);
            }
        });
        thread.start();
    }

    /**
     *
     *
     */
    void setIndexData(final String title, final int icon)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                database.updateIndexData(indexId, title, icon);
                callback.updatedIndexData(false);
            }
        });
        thread.start();
    }

    /**
     *
     *
     */
    void setReferenceData()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                database.setReferenceIndexData(indexId);
                callback.updatedIndexData(true);
            }
        });
        thread.start();
    }

    /**
     *
     *
     */
    @Override
    public void dataEntryFinished(OperationType operationType, boolean result, long id, String title)
    {
         //
    }

    /**
     *
     *
     */
    @Override
    public void timeEntryFinished(OperationType operationType, boolean result, long indexId, long dataId)
    {
        //
    }

    @Override
    public void modelDataEntryFinished(OperationType operationType, boolean result, long indexId, String title)
    {
        //
        Log.v(TAG, "modelDataEntryFinished : " + result + " " + title + " " + indexId);
    }

    /**
     *
     */
    void closeDatabase()
    {
        try
        {
            database.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    CreateModelDataDialog.Callback getCreateModelDataCallback()
    {
        return (new CreateModelData(database));
    }

    /**
     *
     */
    interface IDatabaseReadyNotify
    {
        void databaseSetupFinished(boolean result);
        void updatedIndexData(boolean isIconOnly);
    }
}
