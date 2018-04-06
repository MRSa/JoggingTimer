package net.osdn.gokigen.joggingtimer.utilities;

import java.util.Locale;

import static java.lang.Math.abs;

/**
 *
 *
 */
public class TimeStringConvert
{

    /**
     *  Converts time value from long to string
     * @param millis  time(milliseconds)
     * @return  hh:mm:ss.S
     */
    public static CharSequence getTimeString(long millis)
    {
        int ms = (int) ((millis % 1000) / 100);
        int sec = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) ((millis / (1000 * 60 * 60)) % 24);

        if (hours < 1)
        {
            // １時間経過していない時は、時間表示は省略する
            return (String.format(Locale.US,"%02d'%02d\"%d", minutes, sec, ms));
        }
        return (String.format(Locale.US,"%d:%02d'%02d\"%d", hours, minutes, sec, ms));
    }

    /**
     *
     *
     */
    public static CharSequence getDiffTimeString(long millis)
    {
        int ms = abs((int) ((millis % 1000) / 100));
        int sec = abs((int) (millis / 1000) % 60);
        int minutes = abs((int) ((millis / (1000 * 60))));
        String retString = (millis > 0) ? "+" : "-";
        if (minutes < 1)
        {
            retString = retString + String.format(Locale.US,"%d\"%d", sec, ms);
        }
        else
        {
            retString = retString + String.format(Locale.US,"%d'%d\"%d", minutes, sec, ms);
        }
        return (retString);
    }
}
