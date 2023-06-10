package net.osdn.gokigen.joggingtimer.recorddetail

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.BaseColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback.OperationType
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex.EntryIndex
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData.IEditedModelDataCallback
import net.osdn.gokigen.joggingtimer.utilities.CreateModelDataDialog
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert


class RecordDetailSetup internal constructor(
    private val activity: AppCompatActivity,
    private val indexId: Long,
    private val callback: IDatabaseReadyNotify,
    private val operation: IRecordOperation?,
    private val editCallback: IEditedModelDataCallback
) : ITimeEntryDatabaseCallback, IDetailEditor
{
    private var database: ITimeEntryDatabase? = null
    fun setup()
    {
        Log.v(TAG, "setup()")
        database = TimeEntryDatabaseFactory(activity, this).entryDatabase
        val thread = Thread {
            try
            {
                database?.prepare()
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    override fun prepareFinished(isReady: Boolean)
    {
        if (!isReady)
        {
            callback.databaseSetupFinished(false)
            return
        }
        val editor: IDetailEditor = this
        val thread = Thread {
            var ret = false
            try
            {
                operation?.clearRecord()
                val cursor = database?.getAllDetailData(indexId)
                var index = 0
                var startTime: Long = 0
                var previousLapTime: Long = 0
                var morePreviousTime: Long = 0
                while (cursor?.moveToNext() == true)
                {
                    val dataIdIndex = cursor.getColumnIndex(BaseColumns._ID)
                    val dataId = cursor.getLong(dataIdIndex)

                    val indexIdIndex = cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_INDEX_ID)
                    val indexId = cursor.getLong(indexIdIndex)

                    val entryTimeIndex = cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)
                    val entryTime = cursor.getLong(entryTimeIndex)

                    val recordTypeIndex = cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE)
                    val recordType = cursor.getInt(recordTypeIndex)
                    if (index == 0)
                    {
                        // first record
                        startTime = entryTime
                        previousLapTime = entryTime
                        morePreviousTime = entryTime
                    }
                    else
                    {
                        val lapTime = entryTime - previousLapTime
                        val overallTime = entryTime - startTime
                        val differenceTime =
                            lapTime - (previousLapTime - morePreviousTime)
                        operation?.addRecord(
                            DetailRecord(
                                indexId,
                                dataId,
                                recordType,
                                index,
                                lapTime,
                                overallTime,
                                differenceTime,
                                editor
                            )
                        )
                        morePreviousTime = previousLapTime
                        previousLapTime = entryTime
                    }
                    index++
                }
                activity.runOnUiThread { operation?.dataSetChangeFinished() }
                ret = true
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            callback.databaseSetupFinished(ret)
        }
        thread.start()
    }

    fun setEditIndexData(title: String, icon: Int)
    {
        val data = EditIndexData(title, icon)
        val thread = Thread {
            database?.updateIndexData(indexId, data.title(), data.icon())
            callback.updatedIndexData(false)
        }
        thread.start()
    }

    fun getEditIndexData(): EditIndexData?
    {
        var title = ""
        var iconId: Int = R.drawable.ic_android_black_24dp
        try
        {
            val cursor = database?.getIndexdata(indexId)
            while (cursor?.moveToNext() == true)
            {
                val titleIndex = cursor.getColumnIndex(EntryIndex.COLUMN_NAME_TITLE)
                title = cursor.getString(titleIndex)

                val iconIdIndex = cursor.getColumnIndex(EntryIndex.COLUMN_NAME_ICON_ID)
                iconId = cursor.getInt(iconIdIndex)
            }
            return EditIndexData(title, iconId)
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
        return (null)
    }

    fun setReferenceData(id: Int)
    {
        val thread = Thread {
            database?.setReferenceIndexData(id, indexId)
            callback.updatedIndexData(true)
        }
        thread.start()
    }

    override fun dataEntryFinished(operationType: OperationType, result: Boolean, id: Long, title: String) { }
    override fun timeEntryFinished(operationType: OperationType, result: Boolean, indexId: Long, dataId: Long) { }
    override fun modelDataEntryFinished(operationType: OperationType, result: Boolean, indexId: Long, title: String)
    {
        Log.v(TAG, "modelDataEntryFinished : $result $title $indexId")
    }

    fun closeDatabase()
    {
        try
        {
            database?.close()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * IDetailEditor.editDetailData()
     */
    override fun editDetailData(indexId: Long, dataId: Long, count: Int, defaultMillis: Long)
    {
        activity.runOnUiThread {
            val dialog2 = CreateModelDataDialog.newInstance(
                false,
                activity.getString(R.string.information_modify_time),
                count,
                CreateModelData(
                    database,
                    editCallback, null, indexId, dataId
                ),
                defaultMillis
            )
            dialog2.showNow(activity.supportFragmentManager, "dialog2")
        }
    }

    fun updateDatabaseRecord(adapter: RecordDetailAdapter)
    {
        try
        {
            val count = adapter.itemCount
            if (count > 1)
            {
                for (index in 0 until count)
                {
                    val record = adapter.getRecord(index)
                    val id = record.dataId
                    val time = record.totalTime
                    database!!.updateTimeEntryData(id, time)
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * 現在のデータを共有する
     *
     */
    fun shareTheData(adapter: RecordDetailAdapter?)
    {
        Log.v(TAG, "shareTheData()")
        if (adapter == null || adapter.itemCount <= 0 )
        {
            // データがない場合は、何もしない
            Log.v(TAG, "DATA IS NONE, NOT EXPORTED")
            return
        }
        shareDetailIntent(adapter)
    }

    @SuppressLint("Range")
    private fun shareDetailIntent(adapter: RecordDetailAdapter)
    {
        var title = ""
        val dataCount = adapter.itemCount
        val dataToExport = StringBuilder()
        dataToExport.append("; ")
        dataToExport.append(activity.getString(R.string.app_name))
        dataToExport.append("\r\n")
        try
        {
            val cursor = database?.getIndexdata(indexId)
            while (cursor?.moveToNext() == true) {
                dataToExport.append("; ")
                title = cursor.getString(cursor.getColumnIndex(EntryIndex.COLUMN_NAME_TITLE))
                dataToExport.append(title)
                dataToExport.append(",")
                dataToExport.append(
                    TimeStringConvert.getTimeString(
                        cursor.getLong(
                            cursor.getColumnIndex(
                                EntryIndex.COLUMN_NAME_TIME_DURATION
                            )
                        )
                    )
                )
                dataToExport.append(",")
                dataToExport.append(cursor.getLong(cursor.getColumnIndex(EntryIndex.COLUMN_NAME_START_TIME)))
                dataToExport.append(",")
                dataToExport.append(cursor.getString(cursor.getColumnIndex(EntryIndex.COLUMN_NAME_MEMO)))
                dataToExport.append(",")
                dataToExport.append("\r\n")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            dataToExport.append("\r\n")
        }
        dataToExport.append("; \r\n")
        dataToExport.append("; LapCount,LapTime,TotalTime,LapTime(ms),TotalTime(ms),;\r\n")
        for (index in 0 until dataCount)
        {
            try
            {
                val record = adapter.getRecord(index)
                dataToExport.append(record.lapCount)
                dataToExport.append(",")
                dataToExport.append(record.title)
                dataToExport.append(",")
                dataToExport.append(record.overallTime)
                dataToExport.append(",")
                dataToExport.append(record.lapTime)
                dataToExport.append(",")
                dataToExport.append(record.totalTime)
                dataToExport.append(",")
                dataToExport.append(";")
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                dataToExport.append(";;\r\n")
                break
            }
            dataToExport.append("\r\n")
        }

        // Intent発行(ACTION_SEND)
        shareIntentActionSend(title, dataToExport.toString())
        //shareIntentActionCreateNote(title, dataToExport.toString())
    }

    private fun shareIntentActionSend(title: String, message: String)
    {
        try
        {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND  // NoteIntents.ACTION_CREATE_NOTE
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            activity.startActivity(sendIntent)
            Log.v(TAG, "<<< SEND INTENT >>> : $title")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

/*
    private fun shareIntentActionCreateNote(title: String, message: String)
    {
        try
        {
            val sendIntent = Intent().apply {
                action = NoteIntents.ACTION_CREATE_NOTE
                type = "text/plain"
                putExtra(NoteIntents.EXTRA_NAME, title)
                putExtra(NoteIntents.EXTRA_TEXT, message)
            }
            activity.startActivity(sendIntent)
            Log.v(TAG, "<<< SEND INTENT(NOTE) >>> : $title")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
*/
    class EditIndexData(val title: String, val icon: Int)
    {
        fun title(): String
        {
            return (title)
        }

        fun icon(): Int
        {
            return (icon)
        }
    }

    internal interface IDatabaseReadyNotify
    {
        fun databaseSetupFinished(result: Boolean)
        fun updatedIndexData(isIconOnly: Boolean)
    }

    companion object {
        private val TAG = RecordDetailSetup::class.java.simpleName
    }
}
