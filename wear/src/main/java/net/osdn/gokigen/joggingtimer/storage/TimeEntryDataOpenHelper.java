package net.osdn.gokigen.joggingtimer.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryGps;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryMemo;

/**
 *  TimeEntryDataOpenHelper : to manage database creation and version management.
 *      - https://developer.android.com/training/basics/data-storage/databases.html
 *
 *        > TimeEntryDataOpenHelper mDbHelper = new TimeEntryDataOpenHelper(getContext());
 *
 */
class TimeEntryDataOpenHelper extends SQLiteOpenHelper
{
    private final String TAG = toString();
    private static final String DATABASE_NAME = "TimeEntryData.db";
    private static final int DATABASE_VERSION = 2;

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRY_ENTRYDATA =
            "CREATE TABLE " + TimeEntryData.EntryData.TABLE_NAME + " (" +
                    TimeEntryData.EntryData._ID + " INTEGER PRIMARY KEY," +
                    TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryData.EntryData.COLUMN_NAME_ICON_ID + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryData.EntryData.COLUMN_NAME_MEMO_ID + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryData.EntryData.COLUMN_NAME_GPS_ID + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryData.EntryData.COLUMN_NAME_OTHER_ID + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_ENTRY_ENTRYINDEX =
            "CREATE TABLE " + TimeEntryIndex.EntryIndex.TABLE_NAME + " (" +
                    TimeEntryIndex.EntryIndex._ID + " INTEGER PRIMARY KEY," +
                    TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TimeEntryIndex.EntryIndex.COLUMN_NAME_MEMO + TEXT_TYPE + COMMA_SEP +
                    TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_ENTRY_MEMO =
            "CREATE TABLE " + TimeEntryMemo.EntryMemo.TABLE_NAME + " (" +
                    TimeEntryMemo.EntryMemo._ID + " INTEGER PRIMARY KEY," +
                    TimeEntryMemo.EntryMemo.COLUMN_NAME_DATETIME + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryMemo.EntryMemo.COLUMN_NAME_ICON + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryMemo.EntryMemo.COLUMN_NAME_DATA_MEMO + TEXT_TYPE + " )";

    private static final String SQL_CREATE_ENTRY_GPS =
            "CREATE TABLE " + TimeEntryGps.EntryGps.TABLE_NAME + " (" +
                    TimeEntryGps.EntryGps._ID + " INTEGER PRIMARY KEY," +
                    TimeEntryGps.EntryGps.COLUMN_NAME_DATETIME + INTEGER_TYPE + COMMA_SEP +
                    TimeEntryGps.EntryGps.COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
                    TimeEntryGps.EntryGps.COLUMN_NAME_LATITUDE + REAL_TYPE + COMMA_SEP +
                    TimeEntryGps.EntryGps.COLUMN_NAME_ALTITUDE + REAL_TYPE + COMMA_SEP +
                    TimeEntryGps.EntryGps.COLUMN_NAME_SPEED + REAL_TYPE + COMMA_SEP +
                    TimeEntryGps.EntryGps.COLUMN_NAME_MEMO + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRY_ENTRYDATA =
            "DROP TABLE IF EXISTS " + TimeEntryData.EntryData.TABLE_NAME;

    private static final String SQL_DELETE_ENTRY_ENTRYINDEX =
            "DROP TABLE IF EXISTS " + TimeEntryIndex.EntryIndex.TABLE_NAME;

    private static final String SQL_DELETE_ENTRY_MEMO =
            "DROP TABLE IF EXISTS " + TimeEntryMemo.EntryMemo.TABLE_NAME;

    private static final String SQL_DELETE_ENTRY_GPS =
            "DROP TABLE IF EXISTS " + TimeEntryGps.EntryGps.TABLE_NAME;

    /**
     *   TimeEntryDataOpenHelper constructor
     *
     * @param context  context
     */
    TimeEntryDataOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v(TAG, "onCreate()");
        try
        {
            db.execSQL(SQL_CREATE_ENTRY_ENTRYDATA);
            db.execSQL(SQL_CREATE_ENTRY_ENTRYINDEX);
            db.execSQL(SQL_CREATE_ENTRY_MEMO);
            db.execSQL(SQL_CREATE_ENTRY_GPS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.v(TAG, "onCreate()");
        try
        {
            db.execSQL(SQL_DELETE_ENTRY_ENTRYDATA);
            db.execSQL(SQL_DELETE_ENTRY_ENTRYINDEX);
            db.execSQL(SQL_DELETE_ENTRY_MEMO);
            db.execSQL(SQL_DELETE_ENTRY_GPS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        onCreate(db);
    }

    /**
     *
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     *
     */
    static void deleteDatabase(Context context)
    {
        context.deleteDatabase(DATABASE_NAME);
    }
}
