package net.osdn.gokigen.joggingtimer.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

public class TimeEntryDatabaseFactory
{
    private final String TAG = toString();
    private final TimeEntryDatabase database;

    /**
     *
     */
    public TimeEntryDatabaseFactory(@NonNull Context context, @NonNull ITimeEntryDatabaseCallback callback)
    {
        //
        database = new TimeEntryDatabase(context, callback);
    }

    /**
     *
     */
    public ITimeEntryDatabase getEntryDatabase()
    {
        Log.v(TAG, "getEntryDatabase()");
        return (database);
    }

    /**
     *
     */
    public static void deleteDatabase(@NonNull Context context)
    {
        Log.v("DatabaseFactory", "deleteDatabase()");
        String dbList[] = context.databaseList();
        for (String databaseName : dbList)
        {
            Log.v("DB Name(before):", databaseName);
        }

        TimeEntryDataOpenHelper.deleteDatabase(context);

        String dbListAfter[] = context.databaseList();
        for (String databaseName : dbListAfter)
        {
            Log.v("DB Name(after):", databaseName);
        }
    }
}
