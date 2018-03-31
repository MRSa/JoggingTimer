package net.osdn.gokigen.joggingtimer.utilities;

import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;

public class CreateModelData implements CreateModelDataDialog.Callback
{
    private final String TAG = toString();
    private final ITimeEntryDatabase database;

    public CreateModelData(ITimeEntryDatabase database)
    {
        this.database = database;

    }

    @Override
    public void dataCreated(final int lap, final int hour, final int minute, final int second)
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
                        if (lap > 0)
                        {
                            database.createTimeEntryModelData(lap, hour, minute, second, "");
                        }
                        else
                        {
                            Log.v(TAG, "MODIFIED TO : " + hour + ":" + minute + ":" + second);
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
}
