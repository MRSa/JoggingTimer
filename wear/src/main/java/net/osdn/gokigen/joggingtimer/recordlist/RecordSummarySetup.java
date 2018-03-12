package net.osdn.gokigen.joggingtimer.recordlist;

import android.database.Cursor;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback;
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory;
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider;
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

import static android.provider.BaseColumns._ID;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE;

/**
 *
 *
 */
class RecordSummarySetup implements ITimeEntryDatabaseCallback
{
    private final String TAG = toString();
    private final WearableActivity activity;
    private final IDatabaseReadyNotify callback;
    private final IDetailLauncher detailLauncher;
    private final IRecordOperation operation;
    private ITimeEntryDatabase database = null;

    RecordSummarySetup(WearableActivity activity, IDatabaseReadyNotify callback, IDetailLauncher detailLauncher, IRecordOperation operation)
    {
        this.activity = activity;
        this.callback = callback;
        this.detailLauncher = detailLauncher;
        this.operation = operation;
    }

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
                    Cursor cursor = database.getAllIndexData();
                    while (cursor.moveToNext())
                    {
                        long dataId = cursor.getLong(cursor.getColumnIndex(_ID));
                        String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                        //String memo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MEMO));
                        int iconId = IconIdProvider.getIconResourceId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ICON_ID)));
                        //long startTime = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_START_TIME));
                        long duration = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME_DURATION));
                        String memo = TimeStringConvert.getTimeString(duration).toString();
                        operation.addRecord(new DataRecord(dataId, iconId, title, memo, detailLauncher));
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

    @Override
    public void dataEntryFinished(OperationType operationType, boolean result, long id, String title)
    {
        //
    }

    @Override
    public void timeEntryFinished(OperationType operationType, boolean result, long indexId, long dataId)
    {
        //
    }

    /**
     *
     */
    void deleteTimeEntryData(long indexId)
    {
        Log.v(TAG, "deleteTimeEntryData() : " + indexId);
        try
        {
            database.deleteTimeEntryData(indexId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    interface IDatabaseReadyNotify
    {
        void databaseSetupFinished(boolean result);
    }
}