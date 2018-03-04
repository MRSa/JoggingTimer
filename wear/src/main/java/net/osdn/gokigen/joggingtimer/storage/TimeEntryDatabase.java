package net.osdn.gokigen.joggingtimer.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 *
 *
 *
 */
public class TimeEntryDatabase implements ITimeEntryDatabase
{
    private final String TAG = toString();
    private final TimeEntryDataOpenHelper dbHelper;
    private final ITimeEntryDatabaseCallback callback;
    private SQLiteDatabase db = null;
    //private SQLiteDatabase writeDb = null;
    //private SQLiteDatabase readDb = null;

    public TimeEntryDatabase(Context context, ITimeEntryDatabaseCallback callback)
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
