package net.osdn.ja.gokigen.wearos.timerapp.counter

import java.util.Locale

object TimeStringConvert {
    /**
     * Converts time value from long to string
     * @param millis  time(milliseconds)
     * @return  hh:mm:ss.S
     */
    fun getTimeString(millis: Long): CharSequence
    {
        val ms = (millis % 1000 / 100).toInt()
        val sec = (millis / 1000).toInt() % 60
        val minutes = (millis / (1000 * 60) % 60).toInt()
        val hours = (millis / (1000 * 60 * 60) % 24).toInt()
        return (if (hours < 1)
                {
                    // １時間経過していない時は、時間表示は省略する
                    String.format(Locale.US, "%02d'%02d\"%d", minutes, sec, ms)
                }
                else
                {
                    String.format(Locale.US, "%d:%02d'%02d\"%d", hours, minutes, sec, ms)
                })
    }

    /**
     *  差分の時間を文字列に変換する
     *
     */
    fun getDiffTimeString(millis: Long): CharSequence
    {
        val ms = Math.abs((millis % 1000 / 100).toInt())
        val sec = Math.abs((millis / 1000).toInt() % 60)
        val minutes = Math.abs((millis / (1000 * 60)).toInt())
        var retString = if (millis > 0) "+" else "-"
        if (Math.abs(millis) <= 99)
        {
            // 表示限界よりも小さい場合には正負の記号は表示しない
            retString = ""
        }
        retString = if (minutes < 1) {
            retString + String.format(Locale.US, "%d\"%d", sec, ms)
        } else {
            retString + String.format(Locale.US, "%d'%d\"%d", minutes, sec, ms)
        }
        return (retString)
    }
}
