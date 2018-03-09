package net.osdn.gokigen.joggingtimer.recordlist;

import android.util.Log;
import android.view.View;

/**
 *
 *
 */
public class DataRecord implements View.OnClickListener, View.OnLongClickListener
{
    private final String TAG = toString();
    private final int dataId;
    private final IDetailLauncher launcher;
    private int iconId = 0;
    private String title = "";
    private String detail = "";

    /**
     *
     */
    DataRecord(int dataId, int iconId, String title, String detail, IDetailLauncher launcher)
    {
        this.dataId = dataId;
        this.iconId = iconId;
        this.title = title;
        this.detail = detail;
        this.launcher = launcher;
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
        Log.v(TAG, "Clicked : " + dataId + " " + title + " " + detail);
        launcher.launchDetail(dataId);
    }

    @Override
    public boolean onLongClick(View v)
    {
        Log.v(TAG, "LONG CLICK : " + dataId + " " + title + " " + detail);
        return (true);
    }
}
