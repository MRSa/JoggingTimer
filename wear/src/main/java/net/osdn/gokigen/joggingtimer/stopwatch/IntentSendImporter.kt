package net.osdn.gokigen.joggingtimer.stopwatch

import android.content.Context
import android.content.Intent
import android.util.Log
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory
import java.lang.Exception

class IntentSendImporter(context: Context, val intent: Intent) : ITimeEntryDatabaseCallback
{
    val database =  TimeEntryDatabaseFactory(context, this).entryDatabase

    fun start()
    {
        database.prepare()
    }
    private fun handleIntent(receivedIntent: Intent)
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

            var totalTime: Long = 0
            val lapTimeList = ArrayList<Long>()
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
                    val lapTime = timeValue[3].toLong()
                    totalTime += lapTime
                    lapTimeList.add(lapTime)
                }
            }
            val dataTitle = title ?: "imported data"
            importLapTime(dataTitle, totalTime, lapTimeList)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
    private fun importLapTime(dataTitle: String, totalTime: Long, lapTimeList: ArrayList<Long>)
    {
        //  ここでデータベースにデータを入れる (入れたあとはコールバックで戻る）
        database.createImportedTimeEntryData(dataTitle, "imported data", totalTime, lapTimeList)
/*
        //  題名 : dataTitle (String型)
        //  ラップタイムの配列 : lapTimeList (Int型、単位は ms)
        Log.v(TAG, " ----- $dataTitle ----- ")
        for ((index, timeValue) in lapTimeList.withIndex())
        {
            Log.v(TAG, "  $index $timeValue")
        }
        Log.v(TAG, " ---------- ")
*/
    }

    companion object
    {
        private val TAG = IntentSendImporter::class.java.simpleName
    }

    override fun prepareFinished(isReady: Boolean)
    {
        handleIntent(intent)
    }

    override fun dataEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType?,
        result: Boolean,
        id: Long,
        title: String?
    )
    {
        Log.v(TAG, "dataEntryFinished()")
    }

    override fun timeEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType?,
        result: Boolean,
        indexId: Long,
        dataId: Long
    )
    {
        Log.v(TAG, "timeEntryFinished()")
    }

    override fun modelDataEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType?,
        result: Boolean,
        indexId: Long,
        title: String?
    )
    {
        Log.v(TAG, "modelDataEntryFinished()")
    }
}