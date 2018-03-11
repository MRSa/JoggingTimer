package net.osdn.gokigen.joggingtimer.storage.contract;

import android.provider.BaseColumns;

public final class TimeEntryIndex
{
    private TimeEntryIndex() { }

    /* Inner class that defines the table contents */
    public static class EntryIndex implements BaseColumns
    {
        public static final String TABLE_NAME = "time_data_index";
        public static final String COLUMN_NAME_TITLE = "title";               // text(string)
        public static final String COLUMN_NAME_MEMO  = "memo";                // text(string)
        public static final String COLUMN_NAME_ICON_ID = "icon_id";          // integer(int)
        public static final String COLUMN_NAME_START_TIME = "start_time";   // integer(long)
        public static final String COLUMN_NAME_TIME_DURATION = "duration";  // integer(long)
    }

}
