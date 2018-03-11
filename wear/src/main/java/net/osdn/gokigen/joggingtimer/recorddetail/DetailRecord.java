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
    private int iconId = 0;
    private String title = "";
    private String detail = "";

    /**
     *
     */
    DetailRecord(long dataId, int iconId, String title, String detail)
    {
        this.dataId = dataId;
        this.iconId = iconId;
        this.title = title;
        this.detail = detail;
    }

    void setIconId(int iconId)
    {
        this.iconId = iconId;
    }

    int getIconId()
    {
        return (iconId);
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
        Log.v(TAG, "Clicked : [" + dataId + "] " + title + " " + detail);

    }
}
