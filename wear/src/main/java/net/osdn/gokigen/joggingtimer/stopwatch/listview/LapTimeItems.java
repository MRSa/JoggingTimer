package net.osdn.gokigen.joggingtimer.stopwatch.listview;

import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert;

import java.util.Locale;

/**
 *
 *
 */
public class LapTimeItems
{
    private String lapCount = "";
    private String mainText = "";
    private String subText = "";

    public LapTimeItems(long lapCount, long mainText, long subText)
    {
        this.lapCount = String.format(Locale.US,"%02d", lapCount);
        this.mainText = TimeStringConvert.getTimeString(mainText).toString();

        if (subText != 0)
        {
            this.subText = TimeStringConvert.getDiffTimeString(subText).toString();
        }
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
