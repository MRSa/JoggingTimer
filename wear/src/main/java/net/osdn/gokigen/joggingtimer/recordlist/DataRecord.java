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
    private final long dataId;
    private final IDetailLauncher launcher;
    private int positionId = -1;
    private int iconId;
    private String title;
    private String detail;

    /**
     *
     */
    DataRecord(long dataId, int iconId, String title, String detail, IDetailLauncher launcher)
    {
        this.dataId = dataId;
        this.iconId = iconId;
        this.title = title;
        this.detail = detail;
        this.launcher = launcher;
    }

    long getDataId()
    {
        return (dataId);
    }

    void setPositionId(int positionId)
    {
        this.positionId = positionId;
    }

    int getPositionId()
    {
        return (positionId);
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
        Log.v(TAG, "LONG CLICK : " + dataId + " " + title + " [" + detail + "] (" + positionId + ")");
        launcher.deleteRecord(this);
        return (true);
    }
}
