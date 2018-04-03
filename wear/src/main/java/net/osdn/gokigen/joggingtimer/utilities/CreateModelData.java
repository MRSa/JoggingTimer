package net.osdn.gokigen.joggingtimer.utilities;

import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;

public class CreateModelData implements CreateModelDataDialog.Callback
{
    private final String TAG = toString();
    private final ITimeEntryDatabase database;
    private final IEditedModelDataCallback editCallback;
    private final long indexId;
    private final long detailId;

    public CreateModelData(ITimeEntryDatabase database, IEditedModelDataCallback editCallback, long indexId, long detailId)
    {
        this.database = database;
        this.editCallback = editCallback;
        this.indexId = indexId;
        this.detailId = detailId;
    }

    @Override
    public void dataCreated(final boolean isLap, final int lap, final long prevValue, final long newValue)
    {
        try
        {
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if (isLap)
                        {
                            database.createTimeEntryModelData(lap, newValue, "");
                        }
                        else
                        {
                            Log.v(TAG, "[" + lap + "] " + "MODIFIED FROM  " + prevValue + " TO " + newValue + " indexId: " + indexId + "  dataId: " + detailId);
                            if (editCallback != null)
                            {
                                editCallback.editedModelData(indexId, detailId, lap, prevValue, newValue);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void dataCreateCancelled()
    {
        Log.v(TAG, "dataCreateCancelled()");

    }

    public interface IEditedModelDataCallback
    {
        void editedModelData(long indexId, long detailId, int lapCount, long prevValue, long newValue);
    }
}
