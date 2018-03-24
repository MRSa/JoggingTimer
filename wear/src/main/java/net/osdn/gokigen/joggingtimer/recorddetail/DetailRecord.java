package net.osdn.gokigen.joggingtimer.recorddetail;

import android.util.Log;
import android.view.View;

/**
 *
 *
 */
public class DetailRecord implements View.OnClickListener
{
    private final String TAG = toString();
    private final long dataId;
    private String lapCount = "";
    private String title = "";
    private String detail = "";

    /**
     *
     */
    DetailRecord(long dataId, String lapCount, String title, String detail)
    {
        this.dataId = dataId;
        this.lapCount = lapCount;
        this.title = title;
        this.detail = detail;
    }

    String getLapCount()
    {
        return (lapCount);
    }

    String getTitle()
    {
        return (title);
    }

    String getDetail()
    {
        return (detail);
    }

    @Override
    public void onClick(View v)
    {
        Log.v(TAG, "Clicked : [" + dataId + "] (" + lapCount + ") " + title + " " + detail);

    }
}
