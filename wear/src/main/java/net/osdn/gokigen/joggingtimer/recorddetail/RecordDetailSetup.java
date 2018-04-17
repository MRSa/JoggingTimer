package net.osdn.gokigen.joggingtimer.recorddetail;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback;
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelDataDialog;
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

import static android.provider.BaseColumns._ID;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_MEMO;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION;
import static net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE;

/**
 *
 *
 */
public class RecordDetailSetup  implements ITimeEntryDatabaseCallback, IDetailEditor
{
    private final String TAG = toString();
    private final WearableActivity activity;
    private final long indexId;
    private final IDatabaseReadyNotify callback;
    private final IRecordOperation operation;
    private final CreateModelData.IEditedModelDataCallback editCallback;
    private ITimeEntryDatabase database = null;

    /**
     *
     *
     */
    RecordDetailSetup(WearableActivity activity, long indexId, IDatabaseReadyNotify callback, IRecordOperation operation, CreateModelData.IEditedModelDataCallback  editCallback)
    {
        this.activity = activity;
        this.indexId = indexId;
        this.callback = callback;
        this.operation = operation;
        this.editCallback = editCallback;
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
        final IDetailEditor editor = this;
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
                        long indexId = cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID));
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
                            operation.addRecord(new DetailRecord(indexId, dataId, recordType, index, lapTime, overallTime, differenceTime,  editor));
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
    void setEditIndexData(@NonNull final String title, final int icon)
    {
        final EditIndexData data = new EditIndexData(title, icon);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                database.updateIndexData(indexId, data.getTitle(), data.getIcon());
                callback.updatedIndexData(false);
            }
        });
        thread.start();
    }

    /**
     *
     *
     */
    EditIndexData getEditIndexData()
    {
        String title = "";
        int iconId = R.drawable.ic_android_black_24dp;
        try
        {
            Cursor cursor = database.getIndexdata(indexId);
            while (cursor.moveToNext())
            {
                title = cursor.getString(cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE));
                iconId = cursor.getInt(cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID));
            }
            return (new EditIndexData(title, iconId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (null);
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
     *    IDetailEditor.editDetailData()
     */
    @Override
    public void editDetailData(final long indexId, final long dataId, final int count, final long defaultMillis)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                CreateModelDataDialog dialog2 = CreateModelDataDialog.newInstance(false, activity.getString(R.string.information_modify_time), count, new CreateModelData(database, editCallback, null, indexId, dataId), defaultMillis);
                dialog2.show(activity.getFragmentManager(), "dialog2");
            }
        });
    }

    /**
     *
     */
    public void updateDatabaseRecord(@NonNull RecordDetailAdapter adapter)
    {
        try
        {
            int count = adapter.getItemCount();
            if (count > 1)
            {
                for (int index = 0; index < count; index++)
                {
                    DetailRecord record = adapter.getRecord(index);
                    long id = record.getDataId();
                    long time = record.getTotalTime();
                    database.updateTimeEntryData(id, time);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *   現在のデータを共有する
     *
     */
    public void shareTheData(final RecordDetailAdapter adapter)
    {
        Log.v(TAG, "shareTheData()");
        if ((adapter == null) || (adapter.getItemCount() <= 0) || (activity == null))
        {
            // データがない場合は、何もしない
            return;
        }
        shareDetailIntent(adapter);
    }


    /**
     *
     *
     */
    private void shareDetailIntent(RecordDetailAdapter adapter)
    {
        String title = "";
        int dataCount = adapter.getItemCount();
        StringBuilder dataToExport = new StringBuilder("");
        dataToExport.append("; ");
        dataToExport.append(activity.getString(R.string.app_name));
        dataToExport.append("\r\n");
        try
        {
            Cursor cursor = database.getIndexdata(indexId);
            while (cursor.moveToNext())
            {
                dataToExport.append("; ");
                title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                dataToExport.append(title);
                dataToExport.append(",");
                dataToExport.append(TimeStringConvert.getTimeString(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME_DURATION))));
                dataToExport.append(",");
                dataToExport.append(cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_START_TIME)));
                dataToExport.append(",");
                dataToExport.append(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MEMO)));
                dataToExport.append(",");
                dataToExport.append("\r\n");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            dataToExport.append("\r\n");
        }
        dataToExport.append("; \r\n");
        dataToExport.append("; LapCount,LapTime,TotalTime,LapTime(ms),TotalTime(ms),;\r\n");

        for (int index = 0; index < dataCount; index++)
        {
            try
            {
                DetailRecord record = adapter.getRecord(index);
                dataToExport.append(record.getLapCount());
                dataToExport.append(",");
                dataToExport.append(record.getTitle());
                dataToExport.append(",");
                dataToExport.append(record.getOverallTime());
                dataToExport.append(",");
                dataToExport.append(record.getLapTime());
                dataToExport.append(",");
                dataToExport.append(record.getTotalTime());
                dataToExport.append(",");
                dataToExport.append(";");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                dataToExport.append(";;\r\n");
                break;
            }
            dataToExport.append("\r\n");
        }

        // Intent発行(ACTION_SEND)
        try
        {
            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, dataToExport.toString());
            activity.startActivity(sendIntent);

            Log.v(TAG, "<<< SEND INTENT >>> : " + title);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    class EditIndexData
    {
        final String title;
        final int icon;

        EditIndexData(String title, int icon)
        {
            this.title = title;
            this.icon = icon;
        }

        String getTitle()
        {
            return (title);
        }

        int getIcon()
        {
            return (icon);
        }
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
