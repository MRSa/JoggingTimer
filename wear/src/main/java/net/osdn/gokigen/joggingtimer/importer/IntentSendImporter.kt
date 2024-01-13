package net.osdn.gokigen.joggingtimer.importer

import android.content.Context
import android.content.Intent
import android.util.Log
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory
import java.lang.Exception

class IntentSendImporter(context: Context, private val intent: Intent) : ITimeEntryDatabaseCallback
{
    private val database = TimeEntryDatabaseFactory(context, this).entryDatabase

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
                        // データは無効だった...インポートを中止する
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
    }

    override fun prepareFinished(isReady: Boolean)
    {
        // データベースの登録準備完了
        handleIntent(intent)
    }

    override fun dataEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType?,
        result: Boolean,
        id: Long,
        title: String?
    )
    {
        Log.v(TAG, "dataEntryFinished($result) : $title")
    }

    override fun timeEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType?,
        result: Boolean,
        indexId: Long,
        dataId: Long
    )
    {
        Log.v(TAG, "timeEntryFinished($result) : $indexId")
    }

    override fun modelDataEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType?,
        result: Boolean,
        indexId: Long,
        title: String?
    )
    {
        Log.v(TAG, "modelDataEntryFinished($result) : $title")
    }

    companion object
    {
        private val TAG = IntentSendImporter::class.java.simpleName
    }
}
