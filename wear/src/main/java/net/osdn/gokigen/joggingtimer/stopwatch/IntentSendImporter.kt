package net.osdn.gokigen.joggingtimer.stopwatch

import android.content.Intent
import android.util.Log
import java.lang.Exception

class IntentSendImporter
{
    fun handleIntent(receivedIntent: Intent)
    {
        try
        {
            val title = receivedIntent.getStringExtra(Intent.EXTRA_SUBJECT)
            val data = receivedIntent.getStringExtra(Intent.EXTRA_TEXT)
            if (data == null)
            {
                Log.v(TAG, " ===== handleIntent: data is null... do not process")
                return
            }
            Log.v(TAG, " ===== handleIntent: ${receivedIntent.action} : $title")

            var nofLapTime = 0
            val timeValueList = ArrayList<String>()
            val lines = data.split("\r?\n".toRegex())
            for ((currentLine, line) in lines.withIndex())
            {
                if (currentLine == 0)
                {
                    if (!line.contains("JoggingTimer"))
                    {
                        //  imported data is not valid... return
                        Log.v(TAG, "Imported data is not valid...")
                        return
                    }
                }
                if (currentLine > 3)
                {
                    val timeValue = line.split(",".toRegex())
                    if (timeValue.size < 3)
                    {
                        // 正当なデータがないので抜ける
                        break
                    }
                    timeValueList.add(timeValue[3])
                    nofLapTime++
                }
            }
            val dataTitle = title ?: "imported data"
            importLapTime(dataTitle, timeValueList)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun importLapTime(title: String, lapTimeList: ArrayList<String>)
    {
        //  ここでデータベースにデータを入れる
        //  題名 : title (String型)
        //  ラップタイムの配列 : timeValueList (Int型、単位は ms)
        Log.v(TAG, " ----- $title ----- ")
        for ((index, timeValue) in lapTimeList.withIndex())
        {
            Log.v(TAG, "  $index $timeValue")
        }
        Log.v(TAG, " ---------- ")

    }

    companion object
    {
        private val TAG = IntentSendImporter::class.java.simpleName
    }
}