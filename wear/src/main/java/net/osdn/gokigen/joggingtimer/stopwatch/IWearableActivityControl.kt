package net.osdn.gokigen.joggingtimer.stopwatch

import androidx.activity.ComponentActivity
import net.osdn.gokigen.joggingtimer.presentation.ui.list.ResultListItems

/**
 *
 *
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
    //fun addTimeStamp(count: Long, lapTime: Long, diffTime: Long)
    //fun clearTimeStamp()
    //fun getLapTimeCount(): Int
    fun getReferenceTimerSelection(): Int
    fun setReferenceTimerSelection(id: Int)
    fun setupReferenceData()

    fun getCounterRecordList(): List<ResultListItems>

}
