package net.osdn.gokigen.joggingtimer.stopwatch;

/**
 *
 *
 */
public class LapTimeItems
{
    private String lapCount = "";
    private String mainText = "";
    private String subText = "";

    public LapTimeItems(String lapCount, String mainText, String subText)
    {
        this.lapCount = lapCount;
        this.mainText = mainText;
        this.subText = subText;
    }

    String getLapCount()
    {
        return (lapCount);
    }

    String getMainText()
    {
        return (mainText);
    }

    String getSubText()
    {
        return (subText);
    }

}
