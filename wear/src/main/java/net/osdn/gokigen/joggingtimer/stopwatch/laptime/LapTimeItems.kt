package net.osdn.gokigen.joggingtimer.stopwatch.laptime

import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert.getDiffTimeString
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert.getTimeString
import java.util.Locale

/**
 *
 *
 */
public class LapTimeItems(lapCount: Long, mainText: Long, subText: Long)
{
    private val lapCount: String
    private val mainText: String
    private val subText: String

    init
    {
        this.lapCount = String.format(Locale.US, "%02d", lapCount)
        this.mainText = getTimeString(mainText).toString()
        this.subText = if (subText != 0L) getDiffTimeString(subText).toString() else ""
    }

    fun getLapCount(): String
    {
        return lapCount
    }

    fun getMainText(): String
    {
        return mainText
    }

    fun getSubText(): String
    {
        return subText
    }
}
