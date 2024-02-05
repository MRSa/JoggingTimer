package net.osdn.gokigen.joggingtimer.stopwatch

import androidx.activity.ComponentActivity
import net.osdn.gokigen.joggingtimer.ResultListData

data class LapTimeRecord(val recordType: Int, val recordTime: Long, val recordIndexId: Long)

/**
 *
 */
interface IWearableActivityControl
{
    fun setup(
        activity: ComponentActivity,
        dbCallback: IDatabaseReloadCallback
    )

    fun setupDatabase(activity: ComponentActivity, isReset: Boolean)
    fun exitApplication(activity: ComponentActivity)
    fun vibrate(duration: Int)
    fun getDataEntry(): IDataEntry
    fun getDisplayMode(): Boolean
    fun setDisplayMode(displayLapTime: Boolean)
    fun timerStarted(isStarted: Boolean)
    fun getReferenceTimerSelection(): Int
    fun setReferenceTimerSelection(id: Int)
    fun setupReferenceData()
    fun getCounterRecordList(): List<ResultListData>
    fun isEditableRecord(id: Int): Boolean
    fun getRecordItem(id: Int): ResultListData
    fun getLapTimeList(id: Int): List<LapTimeRecord>
    fun deleteRecord(id: Int)
    fun updateIndexRecord(id: Int, title: String, iconId: Int)
    fun updateRecord(id: Int, title: String, iconId: Int)
    fun setReferenceIndexData(id: Int, iconId: Int)
    fun setReferenceIconId(id: Int, iconId: Int)
    fun launchNotify(isShow: Boolean)
    fun updateTimeEntryData(detailId: Long, totalTime: Long) : Boolean
    fun createTimeEntryModelData(lap: Int, totalLapTime: Long, memo: String): Long
}
