package net.osdn.gokigen.joggingtimer.storage.contract;


import android.provider.BaseColumns;

public final class TimeEntryGps
{
    private TimeEntryGps() { }

    /* Inner class that defines the table contents */
    public static class EntryGps implements BaseColumns
    {
        public static final String TABLE_NAME = "gps";
        public static final String COLUMN_NAME_DATETIME = "datetime";    // integer(long)
        public static final String COLUMN_NAME_LONGITUDE = "longitude";  // real(double)
        public static final String COLUMN_NAME_LATITUDE = "latitude";    // real(double)
        public static final String COLUMN_NAME_ALTITUDE = "altitude";    // real(double)
        public static final String COLUMN_NAME_SPEED = "speed";           // real(float)
        public static final String COLUMN_NAME_MEMO = "memo";             // text(string)
    }
}
