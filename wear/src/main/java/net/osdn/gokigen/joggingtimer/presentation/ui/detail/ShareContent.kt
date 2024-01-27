package net.osdn.gokigen.joggingtimer.presentation.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.ResultListData
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

object ShareContent
{
    @SuppressLint("WearRecents")
    fun shareLapTimeData(context: Context, dataItem: ResultListData, lapData: ArrayList<LapTimeDataItem>)
    {
        val dataToExport = StringBuilder()
        dataToExport.append("; ")
        dataToExport.append(context.getString(R.string.app_name))
        dataToExport.append("\r\n")
        try {
            dataToExport.append(dataItem.title)
            dataToExport.append(",")
            dataToExport.append(TimeStringConvert.getTimeString(dataItem.duration))
            dataToExport.append(",")
            dataToExport.append(dataItem.startTime) // 時刻データなので本来は TimeStringConvert すべき
            dataToExport.append(",")
            dataToExport.append(dataItem.memo)
            dataToExport.append(",")
            dataToExport.append("\r\n")
            dataToExport.append("; \r\n")
            dataToExport.append("; LapCount,LapTime,TotalTime,LapTime(ms),TotalTime(ms),recordType,;\r\n")

            for ((index, lapItem) in lapData.withIndex())
            {
                if (index > 0)
                {
                    val totalTime = lapItem.absoluteTime - dataItem.startTime
                    dataToExport.append(index)
                    dataToExport.append(",")
                    dataToExport.append(TimeStringConvert.getTimeString(lapItem.lapTime)) // ラップタイム
                    dataToExport.append(",")
                    dataToExport.append(TimeStringConvert.getTimeString(totalTime)) // 開始からの時間
                    dataToExport.append(",")
                    dataToExport.append(lapItem.lapTime)  // ラップタイム
                    dataToExport.append(",")
                    dataToExport.append(totalTime) // 開始からの時間
                    dataToExport.append(",")
                    dataToExport.append(lapItem.recordType)  // レコードデータのタイプ
                    dataToExport.append(",")
                    dataToExport.append(";")
                    dataToExport.append("\r\n")
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            dataToExport.append("\r\n")
        }
        try
        {
            // Share the content.
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND  // NoteIntents.ACTION_CREATE_NOTE
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, dataItem.title)
                putExtra(Intent.EXTRA_TEXT, dataToExport.toString())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(sendIntent)
            Log.v("DetailData", "<<< SEND INTENT >>> : ${dataItem.title}")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
