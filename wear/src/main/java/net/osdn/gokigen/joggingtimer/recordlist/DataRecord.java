package net.osdn.gokigen.joggingtimer.recordlist;

/**
 *
 *
 */
public class DataRecord
{
    private int iconId = 0;
    private String title = "";
    private String detail = "";

    /**
     *
     */
    public DataRecord(int iconId, String title, String detail)
    {
        this.iconId = iconId;
        this.title = title;
        this.detail = detail;
    }

    public void setIconId(int iconId)
    {
        this.iconId = iconId;
    }

    public int getIconId()
    {
        return (iconId);
    }

    public String getTitle()
    {
        return (title);
    }

    public String getDetail()
    {
        return (detail);
    }
}
