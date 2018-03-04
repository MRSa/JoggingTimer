package net.osdn.gokigen.joggingtimer.storage.contract;

import android.provider.BaseColumns;

/**
 *   TimeEntryData
 *
 */
public final class TimeEntryData
{
    private TimeEntryData() { }

    /* Inner class that defines the table contents */
    public static class EntryData implements BaseColumns
    {
        public static final String TABLE_NAME = "time_entry";
        public static final String COLUMN_NAME_INDEX_ID = "index_id";         // integer
        public static final String COLUMN_NAME_ICON_ID = "icon_id";           // integer
        public static final String COLUMN_NAME_MEMO_ID = "memo_id";           // integer
        public static final String COLUMN_NAME_GPS_ID = "gps_id";             // integer
        public static final String COLUMN_NAME_ORDER = "order";               // integer
        public static final String COLUMN_NAME_RECORD_TYPE = "record_type"; // integer
        public static final String COLUMN_NAME_TIME_ENTRY = "time_entry";   // integer
    }
}
