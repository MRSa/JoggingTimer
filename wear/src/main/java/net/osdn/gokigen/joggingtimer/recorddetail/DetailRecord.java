package net.osdn.gokigen.joggingtimer.recorddetail;

import android.util.Log;
import android.view.View;

import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

/**
 *
 *
 */
public class DetailRecord implements View.OnClickListener, View.OnLongClickListener
{
    private final String TAG = toString();
    private final long indexId;
    private final long dataId;
    private final int recordType;
    private final int lapCount;
    private final IDetailEditor editorLauncher;
    private long lapTime;
    private long overallTime;
    private final long differenceTime;

    /**
     *
     */
    DetailRecord(long indexId, long dataId, int recordType, int lapCount, long lapTime, long overallTime, long differenceTime, IDetailEditor editorLauncher)
    {
        this.indexId = indexId;
        this.dataId = dataId;
        this.recordType = recordType;
        this.lapCount = lapCount;
        this.lapTime = lapTime;
        this.overallTime = overallTime;
        this.differenceTime = differenceTime;
        this.editorLauncher = editorLauncher;
    }

    String getLapCount()
    {
        return ("" + lapCount);
    }

    String getTitle()
    {
        return (TimeStringConvert.getTimeString(lapTime).toString());
    }

    String getDetail()
    {
        return (TimeStringConvert.getTimeString(overallTime) + " (" + TimeStringConvert.getDiffTimeString(differenceTime) +") ");
    }

    String getOverallTime()
    {
        return (TimeStringConvert.getTimeString(overallTime).toString());
    }

    long getDataId()
    {
        return (dataId);
    }

    long getTotalTime()
    {
        return (overallTime);
    }

    long getLapTime()
    {
        return (lapTime);
    }

    long addModifiedTime(long modifiedTime, long overallTime)
    {
        lapTime = lapTime + modifiedTime;
        this.overallTime = overallTime + lapTime;
        return (this.overallTime);
    }

    @Override
    public void onClick(View v)
    {
        Log.v(TAG, "Clicked : [" + dataId + "] (" + lapCount + ") " + getTitle() + " " + getDetail());
    }

    @Override
    public boolean onLongClick(View v)
    {
        Log.v(TAG, "LONG Clicked : [" + dataId + "] (" + lapCount + ") " + getTitle() + " " + getDetail() + " TYPE : " + recordType);

        if (recordType == ITimeEntryDatabase.EDITABLE_RECORD_TYPE)
        {
            // Edit Laptime Record...
            editorLauncher.editDetailData(indexId, dataId, lapCount, lapTime);
        }
        return (false);
    }
}
