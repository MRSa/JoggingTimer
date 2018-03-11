package net.osdn.gokigen.joggingtimer.storage.contract;

import android.provider.BaseColumns;

public final class TimeEntryMemo
{
    private TimeEntryMemo() { }

    /* Inner class that defines the table contents */
    public static class EntryMemo implements BaseColumns
    {
        public static final String TABLE_NAME = "memo_entry";
        public static final String COLUMN_NAME_DATETIME = "datetime";   // integer(long)
        public static final String COLUMN_NAME_ICON = "icon_id";         // integer
        public static final String COLUMN_NAME_DATA_MEMO = "memo_data"; // text(string)
    }
}
